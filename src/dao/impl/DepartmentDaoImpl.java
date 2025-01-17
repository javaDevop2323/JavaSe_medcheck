package dao.impl;

import dao.DepartmentDao;
import dao.db.DataBase;
import dao.exceptions.NotFoundException;
import model.Department;

import java.util.List;

public class DepartmentDaoImpl implements DepartmentDao {
    @Override
    public List<Department> getAllDepartmentByHospital(Long id) {
        return DataBase.hospitals.stream()
                .filter(hospital -> hospital.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException
                        (String.format("Hospital with id %d not found", id))).getDepartments();
    }

    @Override
    public Department findDepartmentByName(String name) {
        return DataBase.hospitals.stream()
                .flatMap(hospital -> hospital.getDepartments().stream())
                .filter(department -> department.getDepartmentName().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format("Department with name %s not found", name)));
    }

}

