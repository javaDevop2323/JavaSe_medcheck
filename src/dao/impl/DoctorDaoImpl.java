package dao.impl;

import dao.DoctorDao;
import dao.db.DataBase;
import dao.exceptions.NotFoundException;
import model.Department;
import model.Doctor;

import java.util.List;
import java.util.stream.Collectors;

import static model.GenerateId.departmentId;

public class DoctorDaoImpl implements DoctorDao {
    @Override
    public Doctor findDoctorById(Long id) {
        return DataBase.hospitals.stream()
                .flatMap(hospital -> hospital.getDoctors().stream())
                .filter(doctor -> doctor.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format("Doctor With id %d not found", id)));

    }

    @Override
    public String assignDoctorToDepartment(Long departmentId, List<Long> doctorsId) {
        Department department = DataBase.hospitals.stream()
                .flatMap(hospital -> hospital.getDepartments().stream())
                .filter(dep -> dep.getId().equals(departmentId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format("Department with ID %d not found", departmentId)));

        List<Doctor> doctors = DataBase.doctors.stream()
                .filter(doctor -> doctorsId.contains(doctor.getId()))
                .collect(Collectors.toList());
        if (doctors.size() != doctorsId.size()) {
            throw new NotFoundException("One or more doctors not found.");
        }

        department.setDoctors(doctors);

        return "Doctors successfully assigned to the department.";
    }

    @Override
    public List<Doctor> getAllDoctorsByHospitalId(Long id) {
        return DataBase.hospitals.stream()
                .filter(hospital -> hospital.getId().equals(id))
                .flatMap(hospital -> hospital.getDoctors().stream())
                .collect(Collectors.toList());

    }

    @Override
    public List<Doctor> getAllDoctorsByDepartmentId(Long id) {
        return DataBase.hospitals.stream()
                .flatMap(hospital -> hospital.getDepartments().stream())
                .filter(department -> department.getId().equals(id))
                .flatMap(department -> department.getDoctors().stream())
                .collect(Collectors.toList());
    }
}
