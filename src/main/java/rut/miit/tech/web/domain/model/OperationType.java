package rut.miit.tech.web.domain.model;

public enum OperationType {
    WITHDRAW("Снять средства"),
    DEPOSIT("Пополнить баланс"),
    CHECK_BALANCE("Проверить баланс");

    private String name;

    OperationType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
