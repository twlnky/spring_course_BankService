package rut.miit.tech.web.service.account;

import rut.miit.tech.web.service.util.Order;

public class AccountSort {
    AccountField bankField = AccountField.ID;
    Order order = Order.ASC;
}
enum AccountField{
    ID, ACCOUNT_NUMBER,TYPE,OPEN_DATE
}
