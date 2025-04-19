package rut.miit.tech.web.service.supportTicket;

import rut.miit.tech.web.service.util.Order;

public class SupportSort {
    private SupportField sortingField = SupportField.ID;
    private Order orderBy = Order.ASC;
}
enum SupportField {
    ID,CREATED_DATE,STATUS,DESCRIPTION,CLOSED_DATE
}

