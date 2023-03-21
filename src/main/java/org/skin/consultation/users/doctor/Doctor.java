package org.skin.consultation.users.doctor;

import org.skin.consultation.users.general.Person;

import java.io.Serializable;
import java.time.LocalDate;

public final class Doctor extends Person implements Serializable {
    private String license_number;
    private String specialisation;

    public Doctor(String name, String surname, LocalDate DOB, String mobile_no, String license_number, String specialisation) {
        super(name, surname, DOB, mobile_no);
        setLicense_number(license_number);
        setSpecialisation(specialisation);
    }

    // Implement getter methods
    public String getLicense_number() {
        return license_number;
    }
    public String getSpecialisation() {
        return specialisation;
    }
    public String getFullName() {
        return super.getName() + " " + super.getSurname();
    }

    // Implement setter methods
    public void setLicense_number(String license_number) {
        this.license_number = license_number;
    }
    public void setSpecialisation(String specialisation) {
        this.specialisation = specialisation;
    }
}