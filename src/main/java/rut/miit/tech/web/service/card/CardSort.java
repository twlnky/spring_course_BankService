package rut.miit.tech.web.service.card;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CardSort {
    private CardField sortingField = CardField.ID;
    private Order orderBy = Order.ASCENDING;
}

enum Order{
    ASCENDING,DESCENDING
}

enum CardField{
    ID,ISSUE_DATE,EXPIRATION_DATE
}