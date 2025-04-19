package rut.miit.tech.web.service.atm;

import rut.miit.tech.web.service.util.Order;

public class AtmSort {
    AtmField bankField = AtmField.CODE;
    Order order = Order.ASC;
}
enum AtmField {
    CODE, ADDRESS,STATUS
}

