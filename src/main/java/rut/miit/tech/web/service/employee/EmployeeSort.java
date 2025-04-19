package rut.miit.tech.web.service.employee;

import rut.miit.tech.web.service.util.Order;

public class EmployeeSort {
    private EmployeeField sortingField = EmployeeField.ID;
    private Order orderBy = Order.ASC;
}
enum EmployeeField {
    ID,FULL_NAME,LOGIN,POSITION,BANK_CODE
}
