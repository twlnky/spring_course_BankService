package rut.miit.tech.web.service.client;

import rut.miit.tech.web.service.util.Order;

public class ClientSort {
    private ClientField sortingField = ClientField.ID;
    private Order orderBy = Order.ASC;
}

enum ClientField {
    ID,FULL_NAME,PASSPORT,SNILS,EMAIL,LOGIN
}
