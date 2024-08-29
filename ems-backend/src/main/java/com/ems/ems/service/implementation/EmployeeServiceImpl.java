package com.ems.ems.service.implementation;

import com.ems.ems.dto.EmployeeDto;
import com.ems.ems.entity.Employee;

import com.ems.ems.exception.ResourceAlreadyExistsExemption;
import com.ems.ems.exception.ResourceNotFound;

import com.ems.ems.mapper.EmployeeMapper;
import com.ems.ems.repository.EmployeeRepository;
import com.ems.ems.service.EmployeeService;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public EmployeeDto createEmployee(EmployeeDto employeeDto) {
        Optional<Employee> existingEmployee = employeeRepository.findByEmail(employeeDto.getEmail());
        if(existingEmployee.isPresent()){
            throw new ResourceAlreadyExistsExemption("Employee with this Email already exists!");
        }
        Employee employee = EmployeeMapper.mapToEmployee(employeeDto);
        Employee savedEmployee = employeeRepository.save(employee);
        return EmployeeMapper.mapToEmployeeDto(savedEmployee);
    }
    @Override
    public EmployeeDto getEmployeeById(Long employeeId) {
        Employee fetchedEmployee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFound("Employee does not exist with given ID: "+ employeeId));
        return EmployeeMapper.mapToEmployeeDto(fetchedEmployee);
    }
    @Override
    public List<EmployeeDto> getAllEmployees() {
        List <Employee> allEmployees = employeeRepository.findAll();
        return allEmployees.stream().map((employee) -> EmployeeMapper.mapToEmployeeDto(employee))
                .collect(Collectors.toList());
    }
    @Override
    public EmployeeDto updateEmployee(Long employeeId, EmployeeDto updatedEmployee) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(
                () -> new ResourceNotFound("Employee does not exist with given Id: "+employeeId)
        );
        employee.setFirstName(updatedEmployee.getFirstName());
        employee.setLastName(updatedEmployee.getLastName());
        employee.setEmail(updatedEmployee.getEmail());
        Employee updatedEmployeeObj = employeeRepository.save(employee);
        return EmployeeMapper.mapToEmployeeDto(updatedEmployeeObj);
    }
    @Override
    public void deleteEmployee(Long employeeId) {
        Employee fetchedEmployee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFound("Employee does not exist with given ID: "+ employeeId));
        employeeRepository.deleteById(employeeId);
    }
}