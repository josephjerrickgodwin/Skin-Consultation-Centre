package org.skin.consultation.management.gui.controller.availability;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import com.github.lgooddatepicker.components.TimePicker;
import com.github.lgooddatepicker.components.TimePickerSettings;
import org.skin.consultation.management.gui.UserPanel;
import org.skin.consultation.storage.Data;
import org.skin.consultation.users.doctor.availability.DoctorAvailability;
import org.skin.consultation.users.doctor.Doctor;
import org.skin.consultation.utils.Colors;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.Objects;

public final class availabilityInformation extends JFrame implements UserPanel {
    public availabilityInformation() {

//region SECTION 1
        // Create a header panel
        JPanel top = new JPanel();
        top.setLayout(null);
        top.setBackground(new Color(50, 50, 57));
        top.setSize(1020, 167);

        // Create a header label
        JLabel label = new JLabel("Doctor Availability Subsystem", SwingConstants.CENTER);
        label.setBounds(0, 64, 1020,36);
        label.setForeground(Colors.getWhite());
        label.setFont(new Font("Inter", Font.BOLD, 25));
        top.add(label);
        add(top);

        // Make panel absolute positioning
        Container body = getContentPane();
        body.setLayout(null);
        body.setBackground(Colors.getPitchGray());

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(null);
        infoPanel.setBackground(Colors.getLightGray());
        infoPanel.setBounds(27, 200, 949, 530);

        // doctor name label
        addComponent(new JLabel("Select doctor", SwingConstants.LEFT), 65, 40, 130,25,
                Colors.getWhite(), Colors.getLightGray(), 0, 17, Colors.getFixedGray(),
                0, 0, infoPanel);

        // Add a comboBox containing all the available doctor names
        String[] doctorNames = new String[Data.getInstance().getNoOfDoctors()];
        for (int i = 0; i<Data.getInstance().getNoOfDoctors(); i++) {
            doctorNames[i] = Data.getInstance().getDoctorByIndex(i).getFullName();
        }
        JComboBox<String> doctorList = new JComboBox<>(doctorNames);
        doctorList.setEditable(false);

        // Add patient info components
        JPanel patientInfoPanel = new JPanel();
        patientInfoPanel.setLayout(null);
        addComponent(patientInfoPanel, 63, 100, 420, 390, Colors.getWhite(),
                Colors.getMidGray(), 0, 0, Colors.getBorderWhite(), 2, 0, infoPanel);

        // Add header panel
        JPanel patientHeaderPanel = new JPanel();
        patientHeaderPanel.setLayout(null);
        addComponent(patientHeaderPanel, 0, 0, 420, 50, Colors.getWhite(),
                Colors.getBorderWhite(), 0, 0, Colors.getBorderWhite(), 0, 0, patientInfoPanel);

        // Add title to doctor Info
        addComponent(new JLabel("Doctor Details", SwingConstants.CENTER), 5, 0, 420,50, Color.BLACK,
                Colors.getBorderWhite(), 0, 17, Colors.getSoftGray(), 0, 0, patientHeaderPanel);

        // Add doctor Name
        addComponent(new JLabel("Name"), 25, 67,120,37, Colors.getWhite(),
                Colors.getMidGray(), 0, 15, Colors.getMidGray(), 2, 5, patientInfoPanel);
        JTextField textName = new JTextField();
        textName.setEditable(false);
        addComponent(textName, 160, 67,228,35, Colors.getWhite(),
                Colors.getMidGray(), 1, 13, Colors.getWhite(), 2, 5, patientInfoPanel);

        // Add doctor Name
        addComponent(new JLabel("Surname"), 25, 112, 120,37, Colors.getWhite(),
                Colors.getMidGray(), 0, 15, Colors.getMidGray(), 2, 5, patientInfoPanel);
        JTextField textSurname = new JTextField();
        textSurname.setEditable(false);
        addComponent(textSurname, 160, 112, 228,35, Colors.getWhite(),
                Colors.getMidGray(), 1, 13, Colors.getWhite(), 2, 5, patientInfoPanel);

        // Add doctor Specialisation
        addComponent(new JLabel("Specialisation"), 25, 157, 120,37, Colors.getWhite(),
                Colors.getMidGray(), 0, 15, Colors.getMidGray(), 2, 5, patientInfoPanel);
        JTextField textSpecialisation = new JTextField();
        textSpecialisation.setEditable(false);
        addComponent(textSpecialisation, 160, 157, 228,35, Colors.getWhite(),
                Colors.getMidGray(), 1, 13, Colors.getWhite(), 2, 5, patientInfoPanel);

        // Add doctor Check-in Date
        addComponent(new JLabel("Check-in Date"), 25, 204, 120,37, Colors.getWhite(),
                Colors.getMidGray(), 0, 15, Colors.getMidGray(), 2, 5, patientInfoPanel);

        // Date range is between today and the upcoming next year
        DatePickerSettings dateSettings = new DatePickerSettings();
        DatePicker checkInDate = new DatePicker(dateSettings);
        LocalDate dates = LocalDate.now();
        dateSettings.setDateRangeLimits(dates, dates.plusDays(365));
        addComponent(checkInDate, 160, 204, 228,35, Colors.getWhite(),
                Colors.getMidGray(), 1, 13, Colors.getWhite(), 2, 5, patientInfoPanel);

        // Add doctor Check-in Time
        addComponent(new JLabel("Check-in Time"), 25, 250, 120,37, Colors.getWhite(),
                Colors.getMidGray(), 0, 15, Colors.getMidGray(), 2, 5, patientInfoPanel);

        // Time format is 24.00h
        TimePickerSettings timeSettings = new TimePickerSettings();
        timeSettings.use24HourClockFormat();
        TimePicker checkInTime = new TimePicker(timeSettings);
        addComponent(checkInTime, 160, 250, 228,35, Colors.getWhite(),
                Colors.getMidGray(), 1, 13, Colors.getWhite(), 2, 5, patientInfoPanel);

        // Add doctor Check-out Time
        addComponent(new JLabel("Check-out Time"), 25, 296, 135,37, Colors.getWhite(),
                Colors.getMidGray(), 0, 15, Colors.getMidGray(), 2, 5, patientInfoPanel);
        TimePicker checkOutTime = new TimePicker(timeSettings);
        addComponent(checkOutTime, 160, 296, 228,35, Colors.getWhite(),
                Colors.getMidGray(), 1, 13, Colors.getWhite(), 2, 5, patientInfoPanel);

        // Add doctor Contact No.
        addComponent(new JLabel("Contact No"), 25, 342, 120,37, Colors.getWhite(),
                Colors.getMidGray(), 0, 15, Colors.getMidGray(), 2, 5, patientInfoPanel);
        JTextField textPhone = new JTextField();
        textPhone.setEditable(false);
        addComponent(textPhone, 160, 342, 228,35, Colors.getWhite(),
                Colors.getMidGray(), 1, 13, Colors.getWhite(), 2, 5, patientInfoPanel);
//endregion

//region SECTION 2

        // Add Additional info components
        JPanel additionalInfoPanel = new JPanel();
        additionalInfoPanel.setLayout(null);
        addComponent(additionalInfoPanel, 500, 100, 390, 350, Colors.getWhite(),
                Colors.getMidGray(), 0, 0, Colors.getBorderWhite(), 2, 0, infoPanel);

        // Add header panel
        JPanel additionalInfoHeaderPanel = new JPanel();
        additionalInfoHeaderPanel.setLayout(null);
        addComponent(additionalInfoHeaderPanel, 0, 0, 390, 50, Colors.getWhite(),
                Colors.getBorderWhite(), 0, 0, Colors.getBorderWhite(), 0, 0, additionalInfoPanel);

        // Add title to Additional info Panel
        addComponent(new JLabel("Doctor Availability Information", SwingConstants.CENTER), -5, 0,
                420,50, Color.BLACK, Colors.getBorderWhite(), 0, 17,
                Colors.getSoftGray(), 0, 0, additionalInfoHeaderPanel);

        // Initialize the Table & data
        String[] columnNames = { "Available Date", "Check-in", "Check-out"};

        // Create a table
        JTable table = new JTable();
        DefaultTableModel tableModel =  new DefaultTableModel() {
            // Set cell editable to false - cannot modify its data
            @Override
            public boolean isCellEditable(int row, int column) {return false;}
        };

        // Add column title
        tableModel.setColumnIdentifiers(columnNames);
        table.setModel(tableModel);

        // adding ScrollPane to Table
        JScrollPane jScrollPane = new JScrollPane(table);

        // Update fields
        Doctor doctor = Data.getInstance().getDoctorByIndex(doctorList.getSelectedIndex());
        textName.setText(doctor.getName());
        textSurname.setText(doctor.getSurname());
        textSpecialisation.setText(doctor.getSpecialisation());
        textPhone.setText(doctor.getMobile_number());

        // Update Table
        updateTable(tableModel, doctorList);

        // Set Components on the panel
        addComponent(jScrollPane, 3, 51,384,296, Colors.getWhite(),
                Colors.getMidGray(), 0, 15, Colors.getMidGray(), 0, 0, additionalInfoPanel);

        // Add Clear Button
        JButton clearButton = new JButton("Clear");
        clearButton.setFocusPainted(false);
        addComponent(clearButton, 500, 460, 100,30, Colors.getWhite(),
                Colors.getMidGray(), 1, 13, Colors.getWhite(), 2, 15, infoPanel);

        // Add Confirm Button
        JButton submitButton = new JButton("Confirm");
        submitButton.setFocusPainted(false);

        // Add corresponding components to the infoPanel
        addComponent(submitButton, 790,460, 100,30, Colors.getWhite(),
                Colors.getMidGray(), 1, 13, Colors.getWhite(), 2, 15, infoPanel);
        addComponent(doctorList, 200, 35, 210, 35, Colors.getWhite(), Colors.getFixedGray(),
                0, 13, Colors.getSoftGray(), 2, 5, infoPanel);

        // Add all components to the JPanel
        add(infoPanel);
//endregion

//region EVENT LISTENERS

        // Add an event listener to the clear button
        clearButton.addActionListener(e -> {
            textName.setText("");
            textSurname.setText("");
            textSpecialisation.setText("");
            checkInDate.setText("");
            checkInTime.setText("");
            checkOutTime.setText("");
            textPhone.setText("");
            clearTable(table);
        });

        // Add an action event listener to the confirm button
        submitButton.addActionListener(e -> {
            try {
                switch (convertToIntResult(!checkInDate.getText().isEmpty() || !checkInTime.getText().isEmpty() ||
                        !checkOutTime.getText().isEmpty())) {

                    case 0 -> throw new RuntimeException("Please fill all the required fields!");
                    case 1 -> {
                        LocalTime inTime = checkInTime.getTime();
                        LocalTime outTime = checkOutTime.getTime();
                        switch (convertToIntResult(outTime.isAfter(inTime) && !Objects.equals(inTime, outTime))) {

                            case 0 -> throw new RuntimeException("Invalid time range!");
                            case 1 -> {
                                LocalDate date = checkInDate.getDate();
                                Doctor currentDoctor = Data.getInstance().getDoctorByIndex(doctorList.getSelectedIndex());

                                // Check appointments with the same doctor at the same given time
                                switch (convertToIntResult(Data.getInstance().isDoctorAvailabilityUpdated
                                        (currentDoctor.getLicense_number(), date, inTime, outTime))) {

                                    case 0 -> {
                                        DoctorAvailability availability = new DoctorAvailability(
                                                currentDoctor.getName(), currentDoctor.getSurname(), date, inTime, outTime,
                                                currentDoctor.getLicense_number()
                                        );
                                        Data.getInstance().addDoctorAvailability(availability);
                                        JOptionPane.showMessageDialog(infoPanel, "Availability has been updated",
                                                "Success", JOptionPane.INFORMATION_MESSAGE);
                                        updateTable(tableModel, doctorList);

                                        // Clear data fields
                                        checkInDate.clear();
                                        checkInTime.clear();
                                        checkOutTime.clear();
                                    }
                                    case 1 -> JOptionPane.showMessageDialog(infoPanel,
                                            "The doctor is already available on the given date and time!", "Warning",
                                            JOptionPane.WARNING_MESSAGE);
                                }
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(infoPanel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Add an event Listener to the ComboBox
        doctorList.addActionListener (e -> {

            // Clear all data from the table
            clearTable(table);

            // Update fields
            Doctor currentDoctor = Data.getInstance().getDoctorByIndex(doctorList.getSelectedIndex());
            textName.setText(currentDoctor.getName());
            textSurname.setText(currentDoctor.getSurname());
            textSpecialisation.setText(currentDoctor.getSpecialisation());
            textPhone.setText(currentDoctor.getMobile_number());

            // Update Table
            updateTable(tableModel, doctorList);
        });
//endregion
    }

//region SUPPLEMENTAL METHODS

    // Update the table data
    private void updateTable(DefaultTableModel tableModel, JComboBox<String> doctorList) {

        // Clear the table
        tableModel.setRowCount(0);

        // Update the table data
        if (Data.getInstance().getNoOfAppointments() > 0) {
            LinkedList<org.skin.consultation.users.doctor.availability.DoctorAvailability> doctorAvailabilities = Data.getInstance().getSortedDoctorAvailabilityInformation();
            for (org.skin.consultation.users.doctor.availability.DoctorAvailability Availability : doctorAvailabilities) {
                String selectedDoctor = Objects.requireNonNull(doctorList.getSelectedItem()).toString().toLowerCase();
                String nextDoctor = Availability.getFullName().toLowerCase();
                if (Objects.equals(selectedDoctor, nextDoctor)) {
                    tableModel.addRow(new Object[]{Availability.getDate(), Availability.getCheckInTime().toString(),
                            Availability.getCheckOutTime().toString()
                    });
                }
            }
        }
    }

    // Clear the table data
    private void clearTable(JTable table) {((DefaultTableModel)table.getModel()).setRowCount(0);}
//endregion
}