package org.skin.consultation.users.doctor.consultation;

import org.skin.consultation.users.doctor.Doctor;
import org.skin.consultation.users.patient.Patient;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public final class Consultation implements Serializable {
    private final Doctor doctor;
    private final Patient patient;
    private final LocalDate date;
    private final LocalTime startTime;
    private final LocalTime endTime;
    private final BigDecimal cost;
    private String note;
    private String filePath;


    // Constructor for consultation type that have both an additional note and file
    public Consultation (Doctor doctor, Patient patient, LocalDate date, LocalTime start_time,
                         LocalTime end_time, String note, String filePath, BigDecimal cost) {
        this.doctor = doctor;
        this.patient = patient;
        this.date = date;
        this.startTime = start_time;
        this.endTime = end_time;
        this.note = note;
        this.filePath = filePath;
        this.cost = cost;
    }

    // Constructor for consultation type that does not have an additional note
    public Consultation (Doctor doctor, Patient patient, LocalDate date, LocalTime start_time,
                         LocalTime end_time, String filePath, BigDecimal cost) {
        this.doctor = doctor;
        this.patient = patient;
        this.date = date;
        this.startTime = start_time;
        this.endTime = end_time;
        this.filePath = filePath;
        this.cost = cost;
    }

    // Constructor for consultation type that does not have an additional file
    public Consultation (Doctor doctor, Patient patient, LocalDate date, LocalTime start_time,
                         LocalTime end_time, BigDecimal cost, String note) {
        this.doctor = doctor;
        this.patient = patient;
        this.date = date;
        this.startTime = start_time;
        this.endTime = end_time;
        this.note = note;
        this.cost = cost;
    }

    // Constructor for consultation type that does not have an additional note and file
    public Consultation (Doctor doctor, Patient patient, LocalDate date, LocalTime start_time,
                         LocalTime end_time, BigDecimal cost) {
        this.doctor = doctor;
        this.patient = patient;
        this.date = date;
        this.startTime = start_time;
        this.endTime = end_time;
        this.cost = cost;
    }

    public Doctor getDoctor() {
        return this.doctor;
    }

    public Patient getPatient() {
        return this.patient;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public LocalTime getStartTime() {
        return this.startTime;
    }

    public LocalTime getEndTime() {
        return this.endTime;
    }

    public BigDecimal getCost() {
        return this.cost;
    }

    public String getNote() {
        return this.note;
    }

    public String getFilePath() {return this.filePath;}

    public boolean isNoteAvailable() {return this.note != null;}

    public boolean isFileAvailable() {return this.filePath != null;}
}