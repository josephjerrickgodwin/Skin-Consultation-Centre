package org.skin.consultation.users.doctor.availability;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public final class DoctorAvailability implements Serializable {
    private final String name;
    private final String surname;
    private final LocalDate date;
    private final LocalTime checkInTime;
    private final LocalTime checkOutTime;
    private final String license;


    // Constructor for class DoctorAvailability
    public DoctorAvailability(String name, String surname, LocalDate date, LocalTime checkInTime,
                              LocalTime checkOutTime, String license) {
        this.name = name;
        this.surname = surname;
        this.date = date;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
        this.license = license;
    }

    public String getName() {
        return this.name;
    }

    public String getSurname() {
        return this.surname;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public LocalTime getCheckInTime() {
        return this.checkInTime;
    }

    public LocalTime getCheckOutTime() {
        return this.checkOutTime;
    }

    public String getFullName() {
        return this.name + " " + this.surname;
    }

    public String getLicenseNumber() {return this.license;}
}
