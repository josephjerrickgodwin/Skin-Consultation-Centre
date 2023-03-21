package org.skin.consultation.storage;

import org.skin.consultation.users.doctor.availability.DoctorAvailability;
import org.skin.consultation.users.doctor.consultation.Consultation;
import org.skin.consultation.users.doctor.Doctor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public final class Data {

    // Declare variables
    private final LinkedHashMap<String, Doctor> doctorData = new LinkedHashMap<>();
    private final LinkedList<Consultation> consultationData = new LinkedList<>();
    private final LinkedList<DoctorAvailability> doctorAvailabilityData = new LinkedList<>();
    private static Data data;

    // Default constructor
    private Data() {}

    // Singleton method
    public static Data getInstance() {if (Objects.isNull(data)) {data = new Data();} return data;}

//region DOCTOR INFORMATION
    public LinkedHashMap<String, Doctor> getDoctorData() {
        return this.doctorData;
    }
    public int getNoOfDoctors() {return this.doctorData.size();}
    public Doctor getDoctorByIndex(int i) {
        return this.doctorData.get(new ArrayList<>(this.doctorData.keySet()).get(i));
    }
    public Doctor getDoctor(String license) {return this.doctorData.get(license);}
    public void addDoctor(String license, Doctor doctor) {
        this.doctorData.put(license, doctor);
    }
    public void removeDoctor(String license) {
        this.doctorData.remove(license);
    }
    public boolean isDoctorExists(String license) {return this.doctorData.containsKey(license);}
    public LinkedList<Doctor> sortDoctorInformation(String sortType) {

        // Create an empty LinkedHashMap to store the sorted data
        LinkedList<Doctor> cloned_doctor_list = new LinkedList<>();
        switch (sortType) {
            case "name" -> ((Map<String, Doctor>) doctorData).entrySet()
                    .stream()
                    .sorted(Comparator.comparing(o -> o.getValue().getName()))
                    .forEach((doctor) -> {
                        Doctor newDoctor = new Doctor(
                                doctor.getValue().getName(),
                                doctor.getValue().getSurname(),
                                doctor.getValue().getDate_of_birth(),
                                doctor.getValue().getMobile_number(),
                                doctor.getValue().getLicense_number(),
                                doctor.getValue().getSpecialisation()
                        ); cloned_doctor_list.add(newDoctor);
                    });
            case "surname" ->
                    ((Map<String, Doctor>) doctorData).entrySet()
                    .stream()
                    .sorted(Comparator.comparing(o -> o.getValue().getSurname()))
                    .forEach((doctor) -> {
                        Doctor newDoctor = new Doctor(
                                doctor.getValue().getName(),
                                doctor.getValue().getSurname(),
                                doctor.getValue().getDate_of_birth(),
                                doctor.getValue().getMobile_number(),
                                doctor.getValue().getLicense_number(),
                                doctor.getValue().getSpecialisation()
                        ); cloned_doctor_list.add(newDoctor);
                    });
            case "specialisation" -> ((Map<String, Doctor>) doctorData).entrySet()
                    .stream()
                    .sorted(Comparator.comparing(o -> o.getValue().getSpecialisation()))
                    .forEach((doctor) -> {
                        Doctor newDoctor = new Doctor(
                                doctor.getValue().getName(),
                                doctor.getValue().getSurname(),
                                doctor.getValue().getDate_of_birth(),
                                doctor.getValue().getMobile_number(),
                                doctor.getValue().getLicense_number(),
                                doctor.getValue().getSpecialisation()
                        ); cloned_doctor_list.add(newDoctor);
                    });
        } return cloned_doctor_list;
    }
//endregion

//region CONSULTATION DATA
    public void addConsultation(Consultation consultation) {this.consultationData.add(consultation);}
    public Consultation getConsultation(String license) {
        for (Consultation consultation : consultationData) {
            if (Objects.equals(consultation.getDoctor().getLicense_number(), license)) {
                return consultation;
            }
        } return null;
    }
    public LinkedList<Consultation> getConsultationData() {return this.consultationData;}
    public int getConsultationListLength() {return this.consultationData.size();}
    public boolean isPatientExists(String NIC) {
        for (Consultation consultation : consultationData) {
            if (Objects.equals(consultation.getPatient().getNIC(), NIC)) {
                return true;
            }
        } return false;
    }
    public boolean checkPreviousConsultation(String license, LocalDate date, LocalTime starTime, LocalTime endTime) {
        for (Consultation consultation : consultationData) {
            if (Objects.equals(consultation.getDoctor().getLicense_number(), license) &&
                    consultation.getDate().equals(date)) {

                // if the doctor has a consultation, return false for indicating that he is not available
                if ((consultation.getStartTime().equals(starTime) || consultation.getEndTime().equals(endTime))) {
                    return false;
                }
                if (consultation.getStartTime().isAfter(starTime) || consultation.getEndTime().isBefore(endTime)) {
                    return false;
                }
            }
        } return true;
    }
//endregion

//region DOCTOR AVAILABILITY
    public int getNoOfAppointments() {return this.doctorAvailabilityData.size();}
    public boolean isDoctorAvailable(String license, LocalDate date, LocalTime starTime, LocalTime endTime) {
        for (DoctorAvailability currentDoctor : doctorAvailabilityData) {
            if (Objects.equals(currentDoctor.getLicenseNumber(), license) && currentDoctor.getDate().equals(date)) {

                // Check all the available time for the doctor
                if ((currentDoctor.getCheckInTime().equals(starTime) &&
                        currentDoctor.getCheckOutTime().equals(endTime)) &&
                        checkPreviousConsultation(currentDoctor.getLicenseNumber(), date, starTime, endTime)) {
                    return true;
                }
                if ((currentDoctor.getCheckInTime().isBefore(starTime) &&
                        currentDoctor.getCheckOutTime().isAfter(endTime)) &&
                        checkPreviousConsultation(currentDoctor.getLicenseNumber(), date, starTime, endTime)) {
                    return true;
                }
            }
        } return false;
    }
    public boolean isDoctorAvailabilityUpdated(String license, LocalDate date, LocalTime starTime, LocalTime endTime) {
        for (DoctorAvailability currentDoctor : doctorAvailabilityData) {
            if (Objects.equals(currentDoctor.getLicenseNumber(), license) && currentDoctor.getDate().equals(date)) {

                // Check whether the time range matches previously updated availability information
                if ((currentDoctor.getCheckInTime().equals(starTime) ||
                        currentDoctor.getCheckOutTime().equals(endTime)) ||
                        (currentDoctor.getCheckInTime().isBefore(starTime) &&
                                currentDoctor.getCheckOutTime().isAfter(endTime)) ||
                        starTime.isBefore(currentDoctor.getCheckOutTime())) {
                    return true;
                }
            }
        } return false;
    }
    public LinkedList<DoctorAvailability> getSortedDoctorAvailabilityInformation() {
        LinkedList<DoctorAvailability> doctorAvailability = doctorAvailabilityData;
        doctorAvailability.sort((o1, o2) -> Integer.compare(o1.getFullName().compareTo(o2.getFullName()), 0));
        return doctorAvailability;
    }
    public void addDoctorAvailability(DoctorAvailability doctorAvailability) {
        doctorAvailabilityData.add(doctorAvailability);}
    public DoctorAvailability getDoctorAvailability(String license) {
        for (DoctorAvailability availability : doctorAvailabilityData) {
            if (Objects.equals(availability.getLicenseNumber(), license)) {
                return availability;
            }
        } return null;
    }
//endregion
}