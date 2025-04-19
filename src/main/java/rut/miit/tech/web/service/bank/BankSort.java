package rut.miit.tech.web.service.bank;
import rut.miit.tech.web.service.util.Order;

public class BankSort {
    BankField bankField = BankField.CODE;
    Order order = Order.ASC;
}
enum BankField {
    CODE,NAME,REGISTRATION_DATE
}
