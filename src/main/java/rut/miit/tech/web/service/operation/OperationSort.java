package rut.miit.tech.web.service.operation;

import rut.miit.tech.web.service.util.Order;

public class OperationSort {
    private OperationField sortingField = OperationField.ID;
    private Order orderBy = Order.ASC;
}
enum OperationField{
    ID,SEND_DATETIME,STATUS,TEXT,TICKET_ID
}
