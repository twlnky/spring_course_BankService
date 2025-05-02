package rut.miit.tech.web.service.employee;

import rut.miit.tech.web.domain.model.Client;
import rut.miit.tech.web.domain.model.Employee;
import rut.miit.tech.web.service.util.CriteriaFilter;
import rut.miit.tech.web.service.util.FilterUnit;
import rut.miit.tech.web.service.util.PageResult;
import rut.miit.tech.web.service.util.SortUnit;

import java.util.List;

public interface EmployeeService {
    Employee getById(Long id);
    PageResult<List<Employee>> getAll(int page, int pageSize, List<FilterUnit> filters, SortUnit sort);
    PageResult<List<Employee>> getAll(int page, int pageSize, CriteriaFilter<Employee> filter, SortUnit sort);
    Employee create(Employee card);
    Employee update(Employee card);
    void delete(Long id);
}
