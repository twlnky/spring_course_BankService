package rut.miit.tech.web.service.employee;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rut.miit.tech.web.domain.model.Employee;
import rut.miit.tech.web.repository.EmployeeRepository;
import rut.miit.tech.web.service.util.*;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class EmployeeServiceImp implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final QueryBuilder queryBuilder;

    @Override
    public Employee getById(Long id) {
        return employeeRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    @Override
    public PageResult<List<Employee>> getAll(int page, int pageSize, List<FilterUnit> filters, SortUnit sort) {

        return PageResult.of(queryBuilder.getAll(page, pageSize, filters, sort, Employee.class),
                queryBuilder.getPageCount(pageSize, filters, Employee.class));
    }

    @Override
    public PageResult<List<Employee>> getAll(int page, int pageSize, CriteriaFilter<Employee> filter, SortUnit sort) {
        return PageResult.of(queryBuilder.getAll(page, pageSize, filter, sort, Employee.class),
                queryBuilder.getPageCount(pageSize, filter, Employee.class));
    }

    @Override
    public Employee create(Employee card) {
        return employeeRepository.save(card);
    }

    @Override
    public Employee update(Employee card) {
        return employeeRepository.save(card);
    }

    @Override
    public void delete(Long id) {
        employeeRepository.deleteById(id);
    }
}
