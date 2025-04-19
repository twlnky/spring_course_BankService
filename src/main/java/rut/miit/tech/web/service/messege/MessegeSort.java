package rut.miit.tech.web.service.messege;

import rut.miit.tech.web.service.util.Order;

public class MessegeSort {
    private MessegeField sortingField = MessegeField.ID;
    private Order orderBy = Order.ASC;
}
enum MessegeField {
    ID,FULL_NAME,LOGIN,POSITION,BANK_CODE
}
