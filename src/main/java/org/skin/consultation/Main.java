package org.skin.consultation;

import org.skin.consultation.management.cli.WestminsterSkinConsultationManager;
import org.skin.consultation.management.gui.Menu;
import org.skin.consultation.storage.Data;
import org.skin.consultation.users.doctor.availability.DoctorAvailability;
import org.skin.consultation.users.doctor.consultation.Consultation;
import org.skin.consultation.users.doctor.Doctor;

import java.io.*;
import java.util.InputMismatchException;
import java.util.Scanner;

import static java.lang.System.exit;

/**
 * The main class of the Skin consultation management application
 *
 * @author Joseph Jerrick Godwin - w1899291
 * @since 1.0
 */
public final class Main {
    public static void main(String[] args) {

        // Load previous data
        loadData();
        System.out.println("""
                -----------------------------------------
                |  Welcome to Skin consultation Centre  |
                -----------------------------------------
                Please select an option below
                """);
        Scanner scanner = new Scanner(System.in);

        // Create an instance of the class WestminsterSkinConsultationManager
        WestminsterSkinConsultationManager Manager = new WestminsterSkinConsultationManager();
        primaryLoop: while (true) {
            System.out.println("""
                    100 or CLI: CLI Version
                    101 or GUI: GUI Version
                    999 or EXT: Exit""");
            System.out.print("\nChoose your option: ");
            try {
                String option = scanner.next().toLowerCase();
                switch (option) {
                    case "100", "cli" -> {
                        System.out.println("\nPlease select an option below\n");
                        secondaryLoop: while (true) {
                            System.out.println("""
                    100 or ADR: Add a doctor
                    101 or DDR: Delete a doctor
                    102 or PDL: Print the List of Doctors
                    103 or SPD: Store Program Data into file
                    999 or BCK: Back""");
                            System.out.print("\nChoose your option: ");
                            try {
                                switch (scanner.next().toLowerCase()) {
                                    case "100", "adr" -> Manager.addDoctor();
                                    case "101", "ddr" -> Manager.deleteDoctor();
                                    case "102", "pdl" -> Manager.printDoctor();
                                    case "103", "spd" -> Manager.saveData(1);
                                    case "999", "bck" -> {

                                        // Break the current loop and return to the primary loop
                                        System.out.print("\n");
                                        break secondaryLoop;

                                    } default -> throw new InputMismatchException("Invalid option! Please try again.");
                                }
                            } catch (Exception Ex) {
                                System.out.println(Ex.getMessage());
                                Continue();
                            }
                        }
                    }
                    case "101", "gui" -> {
                        Menu menu = new Menu();
                        menu.setTitle("Main Menu");
                        menu.setResizable(false);
                        menu.setSize(1020, 800);
                        menu.setVisible(true);
                    }
                    case "999", "ext" -> {

                        // save data & exit
                        Manager.saveData(0);
                        exit(0);

                        break primaryLoop;
                    }
                    default -> throw new InputMismatchException("Invalid option! Please try again.");
                }
            } catch (InputMismatchException Ex) {
                System.out.println(Ex.getMessage());
            } catch (Exception ex) {
                System.out.println("Error: An unknown error has occurred!");
            } finally {
                Continue();
            }
        }
    }

    // Supplemental method to loadData function
    private static void retrieveData(String fileName, int type) {
        try {
            FileInputStream fileInputStream = new FileInputStream(fileName);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

            // Read and save objects
            while (true) {
                try {
                    switch (type) {
                        case 0 -> {
                            Doctor doctorObj = (Doctor) objectInputStream.readObject();

                            // Retrieve & update stored information
                            Data.getInstance().addDoctor(doctorObj.getLicense_number(), doctorObj);
                        }
                        case 1 -> {
                            Consultation consultation = (Consultation) objectInputStream.readObject();
                            Data.getInstance().addConsultation(consultation);
                        }
                        case 2 -> {
                            DoctorAvailability availability = (DoctorAvailability) objectInputStream.readObject();
                            Data.getInstance().addDoctorAvailability(availability);
                        }
                    }
                } catch (EOFException eofException) {
                    break; // Break the loop when it reaches the end of file
                } catch (ClassNotFoundException cnfException) {
                    throw new RuntimeException("Invalid class");
                }
            }

            // Close object input stream
            objectInputStream.close();
            objectInputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // Retrieve data from the text file(s)
    public static void loadData() {
        try {
            File doctorList = new File("doctor.txt");
            File consultationList = new File("consultation.txt");
            File availabilityList = new File("availability.txt");

            // Check whether the corresponding file exists
            if (doctorList.exists()) {
                retrieveData("doctor.txt", 0);
            }
            if (consultationList.exists()) {
                retrieveData("consultation.txt", 1);
            }
            if (availabilityList.exists()) {
                retrieveData("availability.txt", 2);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // Continue the program
    public static void Continue() {
        try {
            System.out.print("\nPress any key to continue...");
            System.in.read();
        } catch (Exception ignored) {
        } finally {
            System.out.print("\n");
        }
    }
}