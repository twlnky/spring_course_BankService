package rut.miit.tech.web.domain.util.commands;

import io.github.EgorKor.console.kernel.annotations.CommandComponent;
import io.github.EgorKor.console.kernel.annotations.ConsoleMethod;
import io.github.EgorKor.console.kernel.annotations.ConsoleParam;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import java.util.*;


@CommandComponent
@RequiredArgsConstructor
public class Commands {
    private final EntityManager entityManager;


    @ConsoleMethod(command = "sql", hint = "Выполнить запрос к БД")
    @Transactional
    public void executeSQL(@ConsoleParam(defaultValue = "false", required = false) Boolean help,
                           String query,
                           String type) {
        if (help) {
            System.out.println("""
                    -type - тип запроса (update или query)
                    -query - текст запрос""");
            return;
        }
        Set<String> allowedTypes = Set.of("update", "query");
        Set<String> nonAllowedDDL = Set.of("drop","create","truncate","alter");
        List<String> queryTokens = Arrays.stream(query.replaceAll(" +"," ")
                .toLowerCase()
                .trim().split(" ")).toList();
        for (String queryToken : queryTokens) {
            if(nonAllowedDDL.contains(queryToken)) {
                System.err.println("Not allowed sql token " + queryToken.toUpperCase());
                return;
            }
        }
        if (!allowedTypes.contains(type)) {
            System.err.println("Неправильный тип запроса. Допустимые типы:\n" + allowedTypes);
            return;
        }

        Query hibernateQuery = entityManager.createNativeQuery(query);

        switch (type) {
            case "update" -> System.out.println("Изменено строк: " + hibernateQuery.executeUpdate());
            case "query" -> {
                NativeQuery nativeQuery = hibernateQuery.unwrap(NativeQuery.class);
                nativeQuery.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
                List<Map<String,Object>> rows = nativeQuery.getResultList();
                rows = rows.stream()
                        .map((map) -> {
                            Map<String,Object> object = new TreeMap<>(
                                    (h1,h2) -> {
                                        if(h1.equalsIgnoreCase("id")){
                                            return -1;
                                        }
                                        return h1.compareToIgnoreCase(h2);
                                    }
                            );
                            object.putAll(map);
                            return object;
                        }).toList();
                List<Integer> maxLength = new ArrayList<>();
                if (rows.isEmpty()) {
                    System.out.println("Нет результатов...");
                    return;
                }
                for(var row: rows){
                    int colIndex = 0;
                    for(var col: row.values()){
                        if(maxLength.size() < row.values().size()){
                            maxLength.add(0);
                        }
                        if(col.toString().length() > maxLength.get(colIndex)){
                            maxLength.set(colIndex, col.toString().length());
                        }
                        colIndex++;
                    }
                }
                int colIndex = 0;
                for (var col: rows.get(0).keySet()){
                    if(col.length() > maxLength.get(colIndex)){
                        maxLength.set(colIndex, col.length());
                    }
                    System.out.printf("%-" + (maxLength.get(colIndex) + 1) + "s",col);
                    colIndex++;
                }
                System.out.println();
                for (var row: rows){
                    colIndex = 0;
                    for(var col: row.values()){
                        System.out.printf("%-" + (maxLength.get(colIndex) + 1) + "s",col);
                        colIndex++;
                    }
                    System.out.println();
                }




            }
        }
    }
}
