package org.skin.consultation.users.patient;

import org.skin.consultation.users.general.Person;

import java.time.LocalDate;

public final class Patient extends Person {
    private String uniqueId;
    private String gender;
    private String NIC;

    public Patient(String uid, String name, String surname, String gender, LocalDate date_of_birth,
                   String mobile_number, String NIC) {
        super(name, surname, date_of_birth, mobile_number);
        setUniqueId(uid);
        setGender(gender);
        setNIC(NIC);
    }


    // Implement getter methods
    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getGender() {
        return gender;
    }


    // Implement setter methods
    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getNIC() {
        return NIC;
    }

    public void setNIC(String NIC) {
        this.NIC = NIC;
    }
}
