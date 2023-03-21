package org.skin.consultation.users.general;

import java.io.Serializable;
import java.time.LocalDate;

public class Person implements Serializable {
    String name, surname, mobile_number;
    LocalDate date_of_birth;

    // Custom constructor
    public Person(String name, String surname, LocalDate date_of_birth, String mobile_number) {
        this.name = name;
        this.surname = surname;
        this.date_of_birth = date_of_birth;
        this.mobile_number = mobile_number;
    }

    // Implement getter methods
    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public LocalDate getDate_of_birth() {
        return date_of_birth;
    }

    public String getMobile_number() {
        return mobile_number;
    }
}