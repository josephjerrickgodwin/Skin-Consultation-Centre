package org.skin.consultation.management.gui;

import org.skin.consultation.management.gui.component.RoundedJBorder;
import org.skin.consultation.management.gui.controller.appointment.AppointmentCreation;
import org.skin.consultation.management.gui.controller.appointment.AppointmentInformation;
import org.skin.consultation.management.gui.controller.availability.availabilityInformation;
import org.skin.consultation.management.gui.controller.information.DoctorInformation;
import org.skin.consultation.storage.Data;
import org.skin.consultation.utils.Colors;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalTime;

public final class Menu extends JFrame implements UserPanel {
    private final Container body = getContentPane();
    private JLabel doctors = new JLabel("", SwingConstants.CENTER);
    private JLabel appointments = new JLabel("", SwingConstants.CENTER);
    private JLabel sessions = new JLabel("", SwingConstants.CENTER);

    public Menu() {

        // Create a header panel
        JPanel top = new JPanel();
        top.setLayout(null);
        top.setBackground(Colors.getSemiGray());
        top.setSize(1020, 167);

        // Create a header label
        JLabel label = new JLabel("Skin consultation management System", SwingConstants.CENTER);
        label.setBounds(0, 64, 1020,36);
        label.setForeground(Colors.getWhite());
        label.setFont(new Font("Inter", Font.BOLD, 25));
        top.add(label);
        add(top);

        // Create an absolute positioning
        body.setLayout(null);
        body.setBackground(Colors.getPitchGray());

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(null);
        infoPanel.setBackground(Colors.getLightGray());
        infoPanel.setBounds(28, 200, 949, 530);

        JPanel banner = new JPanel();
        banner.setLayout(null);
        banner.setBackground(Colors.getPitchGray());
        banner.setBorder(new RoundedJBorder(Colors.getLightGray(), 4, 0));
        banner.setBounds(0, 0, 949, 180);
        infoPanel.add(banner);

        // Create a greeting message
        JLabel footerLabel = new JLabel(getDayTime(), SwingConstants.CENTER);
        footerLabel.setBounds(0, 15, 949,50);
        footerLabel.setForeground(Colors.getWhite());
        footerLabel.setFont(new Font("Inter", Font.BOLD, 23));
        banner.add(footerLabel);


        // Add no. of doctors joined
        addComponent(doctors, 90, 110, 230,37, Colors.getWhite(), Colors.getMidGray(),
                0, 15, Colors.getMidGray(), 2, 25, banner);

        // Add no. of appointments made
        addComponent(appointments, 350, 110, 250,37, Colors.getWhite(), Colors.getMidGray(),
                0, 15, Colors.getMidGray(), 2, 25, banner);

        // Add no. of Doctors available
        addComponent(sessions, 630, 110, 230,37, Colors.getWhite(), Colors.getMidGray(),
                0, 15, Colors.getMidGray(), 2, 25, banner);

        // Construct buttons
        JButton doctorInfoBtn = new JButton("<html> <center> Display Doctor <br/> Information </center> </html>");
        doctorInfoBtn.setBounds (75, 440, 200, 230);
        doctorInfoBtn.addActionListener(e -> {

            if (Data.getInstance().getNoOfDoctors() > 0) {
                DoctorInformation doctorInformation = new DoctorInformation();
                doctorInformation.setTitle("Doctor Information Subsystem");
                doctorInformation.setResizable(false);
                doctorInformation.setSize(1020, 800);
                doctorInformation.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(body, "No doctors exist! Contact your system administrator.",
                        "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });

        JButton doctorAvailabilityBtn = new JButton("<html> <center> Manage Doctor <br/> Availability </center> </html>");
        doctorAvailabilityBtn.setBounds (295, 440, 200, 230);
        doctorAvailabilityBtn.addActionListener(e -> {

            if (Data.getInstance().getNoOfDoctors() > 0) {
                availabilityInformation doctorAvailability = new availabilityInformation();
                doctorAvailability.setTitle("Doctor Availability Subsystem");
                doctorAvailability.setResizable(false);
                doctorAvailability.setSize(1020, 800);
                doctorAvailability.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(body, "No doctors exist! Contact your system administrator.",
                        "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });

        JButton makeAppointmentBtn = new JButton("<html> <center> Book an <br/> Appointment </center> </html>");
        makeAppointmentBtn.setBounds (515, 440, 200, 230);
        makeAppointmentBtn.addActionListener(e -> {

            if (Data.getInstance().getNoOfDoctors() > 0 && Data.getInstance().getNoOfAppointments() > 0) {
                AppointmentCreation appointment = new AppointmentCreation();
                appointment.setTitle("Doctor Appointment Subsystem");
                appointment.setResizable(false);
                appointment.setSize(1020, 800);
                appointment.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(body, "Cannot make an appointment, " +
                        "no doctors available at the moment! \nPlease contact your system administrator.",
                        "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });

        JButton displayAppointmentsBtn = new JButton("<html> <center> Display <br/> Appointments </center> </html>");
        displayAppointmentsBtn.setBounds (735, 440, 200, 230);
        displayAppointmentsBtn.addActionListener(e -> {

            if (Data.getInstance().getConsultationListLength() > 0) {
                AppointmentInformation appointmentInformation = new AppointmentInformation();
                appointmentInformation.setTitle("Appointment Information Subsystem");
                appointmentInformation.setResizable(false);
                appointmentInformation.setSize(1020, 800);
                appointmentInformation.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(body, "No appointments found!",
                        "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });

        // Add components
        setButton(doctorInfoBtn, doctorAvailabilityBtn, makeAppointmentBtn, displayAppointmentsBtn);
        add(infoPanel);

        // Update appointment information
        updateInformation();

        // Update appointment related information in every 5 seconds
        Timer timer = new Timer(5000, e -> updateInformation());
        timer.setRepeats(true);
        timer.start();
    }

    // Add Buttons to the parent container
    private void setButton(JButton... components) {
        for (JButton button : components) {
            button.setBackground(Colors.getDarkgray());
            button.setForeground(Colors.getWhite());
            button.setFont(new Font("Inter", Font.BOLD, 16));
            button.setBorder(new RoundedJBorder(Colors.getWhite(), 1, 0));
            button.setFocusPainted(false);
            button.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent evt) {
                    button.setBorder(new RoundedJBorder(Colors.getWhite(), 3, 0));
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    button.setBorder(new RoundedJBorder(Colors.getWhite(), 1, 0));
                }
            });
            add(button);
        }
    }

    // Retrieve the day time
    private String getDayTime() {
        int time = LocalTime.now().getHour();
        switch (convertToIntResult(time <= 11)) {
            case 0 -> {
                switch (convertToIntResult(time <= 15)) {
                    case 0 -> {
                        return "Good Evening!";
                    }
                    case 1 -> {
                        return "Good Afternoon!";
                    }
                }
            }
            case 1 -> {
                return "Good Morning!";
            }
        } return null;
    }

    // Update labels
    private void updateInformation() {
        doctors.setText(Data.getInstance().getNoOfDoctors() + " Doctors Joined");
        appointments.setText(Data.getInstance().getConsultationListLength() + " Appointments Made");
        sessions.setText(Data.getInstance().getNoOfAppointments() + " Sessions Available");
    }
}