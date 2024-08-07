package dao.impl;

import dao.HospitalDao;
import dao.db.DataBase;
import dao.exceptions.NotFoundException;
import model.Hospital;
import model.Patient;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HospitalDaoImpl implements HospitalDao {
    @Override
    public String addHospital(Hospital hospital) {
        boolean isExists = DataBase.hospitals.stream()
                .anyMatch(hos -> hos.getHospitalName()
                        .equalsIgnoreCase(hospital.getHospitalName()));

        if (isExists) {
            throw new IllegalArgumentException("Hospital with this name already exists.");
        }
        DataBase.hospitals.add(hospital);
        return "Hospital added successfully.";
    }

    @Override
    public Hospital findHospitalById(Long id) {
        return DataBase.hospitals.stream()
                .filter(h -> h.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format("Hospital With id %d not found", id)));

    }

    @Override
    public List<Hospital> getAllHospital() {
        return DataBase.hospitals.stream()
                .sorted((h1, h2) -> h1.getHospitalName()
                        .compareToIgnoreCase(h2.getHospitalName()))
                .collect(Collectors.toList());

    }

    @Override
    public List<Patient> getAllPatientFromHospital(Long id) {
        Hospital hospital = DataBase.hospitals.stream()
                .filter(hos -> hos.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format("Hospital with ID %d not found", id)));

        return hospital.getPatients();
    }


    @Override
    public String deleteHospitalById(Long id) {
        Hospital hospital = DataBase.hospitals.stream()
                .filter(hos -> hos.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format("Hospital With id %d not found", id)));
        boolean removed = DataBase.hospitals.remove(hospital);

        if (removed) {
            return "Hospital successfully deleted.";
        } else {
            throw new RuntimeException("Failed to delete hospital, it might have been removed by another process.");
        }
    }

    @Override
    public Map<String, List<Hospital>> getAllHospitalByAddress(String address) {
        List<Hospital> hospitals = DataBase.hospitals.stream()
                .filter(hos -> hos.getAddress().equalsIgnoreCase(address))
                .toList();

        if (hospitals.isEmpty()) {
            throw new NotFoundException(String.format("Hospital with address %s not found", address));
        }
            Map<String, List<Hospital>> hospitalMap = new HashMap<>();
            hospitalMap.put(address, hospitals);
            return hospitalMap;
        }
    }

