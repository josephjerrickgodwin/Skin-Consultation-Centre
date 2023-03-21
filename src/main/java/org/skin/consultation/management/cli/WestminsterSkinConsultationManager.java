package org.skin.consultation.management.cli;

import org.skin.consultation.Main;
import org.skin.consultation.storage.Data;
import org.skin.consultation.users.doctor.Doctor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.charset.IllegalCharsetNameException;
import java.time.LocalDate;
import java.time.Year;
import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Pattern;

public final class WestminsterSkinConsultationManager implements SkinConsultationManager {

    // Add a doctor
    @Override
    public void addDoctor() {
        try {
            switch (convertBoolToInt(Data.getInstance().getNoOfDoctors() < 11)) {
                case 0 -> throw new IndexOutOfBoundsException("Error: Maximum no. of doctors reached!");
                case 1 -> {

                    // Gather input from the user. Input Mode indicates the input type and process methodology.
                    String name = addStringQuery("Enter doctor's Name: ", 1);
                    String surname = addStringQuery("Enter doctor's Surname: ", 1);

                    System.out.println("Enter doctor's Date of Birth");
                    int date = addIntQuery("      Date  : ", 1);
                    int month = addIntQuery("      Month : ", 2);
                    int year = addIntQuery("      Year  : ", 3);
                    LocalDate dob = LocalDate.of(year, month, date);

                    String mobile = addStringQuery("Enter doctor's Mobile Number: ", 3);
                    String license = addStringQuery("Enter doctor's License Number: ", 2).toLowerCase();

                    // Check whether doctor already exists
                    switch (convertBoolToInt(Data.getInstance().isDoctorExists(license))) {
                        case 0 -> {
                            String specialisation = addStringQuery("Enter doctor's Specialisation: ", 1);

                            // Create an instance of the class doctor & push to LinkedHashMap
                            Doctor doctor = new Doctor(name, surname, dob, mobile, license, specialisation);
                            Data.getInstance().addDoctor(license, doctor);

                            System.out.println("\nSuccess: doctor " + name + " has been added");
                        }
                        case 1 -> throw new NullPointerException("Error: doctor already exists!");
                    }
                }
            }
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        } finally {
            Main.Continue();
        }
    }

    // Delete a doctor
    @Override
    public void deleteDoctor() {
        try {
            if (Data.getInstance().getNoOfDoctors() > 0) {
                String license_number = addStringQuery("Enter doctor's License Number: ", 2).toLowerCase();
                if (Data.getInstance().isDoctorExists(license_number)) {
                    Doctor doctor = Data.getInstance().getDoctor(license_number);
                    Data.getInstance().removeDoctor(license_number);
                    System.out.println("doctor's information are as follows," +
                            "\nName           : " + doctor.getName() +
                            "\nSurname        : " + doctor.getSurname() +
                            "\nDate of Birth  : " + doctor.getDate_of_birth() +
                            "\nMobile Number  : " + doctor.getMobile_number() +
                            "\nLicense Number : " + doctor.getLicense_number() +
                            "\nSpecialisation : " + doctor.getSpecialisation().toUpperCase() +
                            "\n\nSuccess: Dr." + doctor.getName() + " has been removed." +
                            "\nThere are " + Data.getInstance().getNoOfDoctors() + " Doctors currently available");
                } else {
                    throw new NullPointerException("Error: Invalid license number or the doctor does not exist!");
                }
            } else {
                throw new NullPointerException("Attention: No doctors exist!");
            }
        } catch (NullPointerException npe) {
            System.out.println(npe.getMessage());
        } catch (Exception exception) {
            System.out.println("Error: An unknown error occurred!");
        } finally {
            Main.Continue();
        }
    }

    // Print a list of doctors' information
    @Override
    public void printDoctor() {
        try {
            if (Data.getInstance().getNoOfDoctors() > 0) {
                for (Doctor doctor : Data.getInstance().sortDoctorInformation("surname")) {
                    System.out.println("Name:           " + doctor.getName() +
                            "\nSurname:        " + doctor.getSurname() +
                            "\nDate of Birth:  " + doctor.getDate_of_birth() +
                            "\nSpecialisation: " + doctor.getSpecialisation() +
                            "\nContact Number: " + doctor.getMobile_number() +
                            "\nLicense Number: " + doctor.getLicense_number().toUpperCase() + "\n");
                }
            } else {
                throw new NullPointerException();
            }
        } catch (NullPointerException npe) {
            System.out.println("Attention: No records found!");
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        } finally {
            Main.Continue();
        }
    }

    // Save data to a file
    @Override
    public void saveData(int mode) {
        try {

            // Check added doctors
            if (Data.getInstance().getNoOfDoctors() > 0) {
                LinkedList<Doctor> doctorList = new LinkedList<>();
                Data.getInstance().getDoctorData().forEach((license, doctor) -> doctorList.add(doctor));
                saveObjToFile("doctor.txt", doctorList, mode);
            }

            // Check added consultations
            if (Data.getInstance().getConsultationListLength() > 0) {
                saveObjToFile("consultation.txt", Data.getInstance().getConsultationData(), mode);
            }

            // Check doctor availability
            if (Data.getInstance().getNoOfAppointments() > 0) {
                saveObjToFile("availability.txt", Data.getInstance()
                        .getSortedDoctorAvailabilityInformation(), mode);
            }
        } catch (Exception e) {

            // Error prints only on Non-silent mode
            if (mode == 1) {
                System.out.println(e.getMessage());
            }
        }
    }


//region SUPPLEMENTAL METHODS

    // Save objects to file(s)
    private void saveObjToFile(String fileName, LinkedList listToSave, int mode) {

        // Create a new text file, delete if exists
        File file = new File(fileName);

        // Delete previously written data
        if (file.exists()) {file.delete();}

        FileOutputStream fileOut = null;
        ObjectOutputStream objectOut = null;

        try {
            fileOut = new FileOutputStream(fileName);
            objectOut = new ObjectOutputStream(fileOut);

            ObjectOutputStream finalObjectOut = objectOut;
            listToSave.forEach((doctor) -> {
                try {
                    // Write an object to file
                    finalObjectOut.writeObject(doctor);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (Exception exception) {
            throw new RuntimeException(exception.getMessage());

        } finally {
            try {

                // Make sure the object output stream is not null
                assert objectOut != null;

                // Close the object stream
                objectOut.close();
                fileOut.close();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // Silent mode does not print the message below
            if (mode == 1) {System.out.println("Successfully wrote to file : " + file.getName());}
        }
    }

    // Convert boolean value to an integer value
    private int convertBoolToInt(Boolean value) {return value ? 1 : 0;}

    // Prompt the user whether terminates the process
    private boolean requestQuit() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("\nWould you like to terminate the process? (Y|N): ");
        String result = scanner.next().toLowerCase().strip();
        return Objects.equals(result, "y");
    }

    // Print questions and validate String input
    private String addStringQuery(String question, int mode) {
        while (true) {
            try {
                System.out.print(question);
                return checkIllegalCharacters(mode, new Scanner(System.in).next().strip());
            } catch (Exception exception) {
                System.out.println(exception.getMessage());
                if (requestQuit()) {throw new RuntimeException("\nAttention: Process terminated!");}
            }
        }
    }

    // Print questions and validate Integer input
    private int addIntQuery(String question, int mode) {
        while (true) {
            try {
                System.out.print(question);
                return validateDob(mode, new Scanner(System.in).nextInt());
            } catch (Exception exception) {
                System.out.println("Error: Invalid input!");
                if (requestQuit()) {throw new RuntimeException("\nAttention: Process terminated!");}
            }
        }
    }

    // Validate Date of Birth of the doctor
    public int validateDob(int type, int dob) {
        switch (type) {
            // The date should be between 1 and 31
            case 1 -> {
                switch (convertBoolToInt(dob <= 31 && dob > 0)) {
                    case 0 -> throw new InputMismatchException("Error: Invalid date!");
                    case 1 -> {return dob;}
                }
            }
            // The month should be between 1 and 12
            case 2 -> {
                switch (convertBoolToInt(dob <= 12 && dob > 0)) {
                    case 0 -> throw new InputMismatchException("Error: Invalid month!");
                    case 1 -> {return dob;}
                }
            }
            // The doctor should be aged between 18 and 63
            case 3 -> {
                int current_year = Year.now().getValue();
                switch (convertBoolToInt(dob <= (current_year - 18) && (current_year - dob) < 64)) {
                    case 0 -> throw new InputMismatchException("Error: Invalid year!");
                    case 1 -> {return dob;}
                }
            }
        } return 0;
    }

    // Validate String input
    public String checkIllegalCharacters(int mode, String data) {
        Pattern characters = null;
        switch (mode) {
            // Validate Name, Specialisation
            case 1 -> characters = Pattern.compile("[0-9!@#$%&*()_+=|<>?^{}\\[\\]~-]", Pattern.CASE_INSENSITIVE);

            // Validate License number
            case 2 -> characters = Pattern.compile("[!@#$%&*()_+=|<>?^{}\\[\\]~]", Pattern.CASE_INSENSITIVE);

            // Validate Mobile number
            case 3 -> characters = Pattern.compile("[a-zA-Z!@#$%&*()_+=|<>?^{}\\[\\]~]", Pattern.CASE_INSENSITIVE);
        }
        switch (convertBoolToInt(Objects.requireNonNull(characters).matcher(data).find())) {
            case 0 -> {return data;}
            case 1 -> throw new IllegalCharsetNameException("Error: Illegal characters found!");
        } return null;
    }
//endregion
}