package org.skin.consultation.management.gui.controller.appointment;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import com.github.lgooddatepicker.components.TimePicker;
import com.github.lgooddatepicker.components.TimePickerSettings;
import org.skin.consultation.management.gui.calculator.Cost;
import org.skin.consultation.management.gui.cryptography.data.EncDecString;
import org.skin.consultation.management.gui.cryptography.files.EncDecFile;
import org.skin.consultation.management.gui.UserPanel;
import org.skin.consultation.storage.Data;
import org.skin.consultation.users.doctor.availability.DoctorAvailability;
import org.skin.consultation.users.doctor.consultation.Consultation;
import org.skin.consultation.users.doctor.Doctor;
import org.skin.consultation.users.patient.Patient;
import org.skin.consultation.utils.Colors;
import org.skin.consultation.utils.SysUID;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.io.EOFException;
import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public final class AppointmentCreation extends JFrame implements UserPanel {

//region DECLARE VARIABLES AND COMPONENTS
    private double fee;
    private final ArrayList<Integer> count = new ArrayList<>();

    private final JPanel additionalInfoPanel;
    private final JPanel patientInfoPanel;
    private final JPanel infoPanel;

    private final JTextArea textNotes;
    private final JTextField NICText;
    private final DatePicker appointmentDate;
    private final TimePicker appointmentStartTime;
    private final TimePicker appointmentEndTime;
    private final JTextField textId;
    private final JTextField textName;
    private final JTextField textSurname;
    private final DatePicker textDob;
    private final JTextField textPhone;
    private final JTextField textFilename;
    private final JTextField textDuration;
    private final JTextField textFee;
    private final JComboBox<String> doctorList;
    private final JButton checkButton;
//endregion

    public AppointmentCreation() {

        // Create a header panel
        JPanel top = new JPanel();
        top.setLayout(null);
        top.setBackground(Colors.getSemiGray());
        top.setSize(1020, 167);

        // Create a header label
        JLabel label = new JLabel("Doctor Appointment Subsystem", SwingConstants.CENTER);
        label.setBounds(0, 64, 1020,36);
        label.setForeground(Colors.getWhite());
        label.setFont(new Font("Inter", Font.BOLD, 25));
        top.add(label);
        add(top);

        // Make panel absolute positioning
        Container body = getContentPane();
        body.setLayout(null);
        body.setBackground(Colors.getPitchGray());

        infoPanel = new JPanel();
        infoPanel.setLayout(null);
        infoPanel.setBackground(Colors.getFixedGray());
        infoPanel.setBounds(27, 200, 949, 530);

        // Add doctor name
        addComponent(new JLabel("Select doctor", SwingConstants.LEFT), 65, 18, 130,25,
                Colors.getWhite(), Colors.getFixedGray(), 0, 17, Colors.getFixedGray(), 0,
                0, infoPanel);

        // Add a comboBox containing all the available doctor names
        String[] doctorNames = new String[Data.getInstance().getNoOfAppointments()];
        for (int i = 0; i<Data.getInstance().getNoOfAppointments(); i++) {
            DoctorAvailability availability = Data.getInstance().getSortedDoctorAvailabilityInformation().get(i);
            doctorNames[i] = availability.getName() + " " + availability.getSurname();
        }

        doctorList = new JComboBox<>(doctorNames);
        doctorList.setEditable(false);
        addComponent(doctorList, 200, 18, 200, 35, Colors.getWhite(), Colors.getFixedGray(),
                0, 13, Colors.getSoftGray(), 2, 5, infoPanel);

        // Add patient NIC
        addComponent(new JLabel("Patient NIC"), 60, 62, 120,25, Colors.getWhite(),
                Colors.getMidGray(), 0, 16, Colors.getFixedGray(), 2, 5, infoPanel);
        NICText = new JTextField();
        addComponent(NICText, 200, 60,200,30, Colors.getWhite(),
                Colors.getMidGray(), 1, 13, Colors.getWhite(), 2, 5, infoPanel);

        // Add Check Button
        checkButton = new JButton("Check");
        checkButton.setFocusPainted(false);
        addComponent(checkButton, 412,60, 70,30, Colors.getWhite(),
                Colors.getMidGray(), 1, 13, Colors.getWhite(), 2, 10, infoPanel);

        // Add appointment Date
        addComponent(new JLabel("Select Your Date"), 495, 18, 160,25, Colors.getWhite(),
                Colors.getMidGray(), 0, 16, Colors.getFixedGray(), 2, 5, infoPanel);

        // Date range is between today and the upcoming next year
        DatePickerSettings dateSettings = new DatePickerSettings();
        appointmentDate = new DatePicker(dateSettings);
        LocalDate date = LocalDate.now();
        dateSettings.setDateRangeLimits(date, date.plusDays(31));
        addComponent(appointmentDate, 670, 15, 220,35, Colors.getWhite(),
                Colors.getMidGray(), 1, 13, Colors.getWhite(), 2, 5, infoPanel);

        // Add appointment Time Range
        addComponent(new JLabel("Select Time Range"), 495, 57, 160,25, Colors.getWhite(),
                Colors.getMidGray(), 0, 16, Colors.getFixedGray(), 2, 5, infoPanel);

        // Time format is 24.00h
        TimePickerSettings timeSettings = new TimePickerSettings();
        timeSettings.use24HourClockFormat();
        appointmentStartTime = new TimePicker(timeSettings);
        addComponent(appointmentStartTime, 670, 55, 105,35, Colors.getWhite(),
                Colors.getMidGray(), 1, 13, Colors.getWhite(), 2, 5, infoPanel);

        appointmentEndTime = new TimePicker(timeSettings);
        addComponent(appointmentEndTime, 785, 55, 105,35, Colors.getWhite(),
                Colors.getMidGray(), 1, 13, Colors.getWhite(), 2, 5, infoPanel);

        // Add patient info components
        patientInfoPanel = new JPanel();
        patientInfoPanel.setLayout(null);
        addComponent(patientInfoPanel, 63, 100, 420, 390, Colors.getWhite(),
                Colors.getMidGray(), 0, 0, Colors.getBorderWhite(), 2, 0, infoPanel);

        // Add header panel
        JPanel patientHeaderPanel = new JPanel();
        patientHeaderPanel.setLayout(null);
        addComponent(patientHeaderPanel, 0, 0, 420, 50, Colors.getWhite(),
                Colors.getBorderWhite(), 0, 0, Colors.getBorderWhite(), 0, 0,
                patientInfoPanel);

        // Add title to patient Info
        addComponent(new JLabel("Patient Details", SwingConstants.CENTER), 5, 0, 420,50,
                Color.BLACK, Colors.getBorderWhite(), 0, 17, Colors.getSoftGray(), 0,
                0, patientHeaderPanel);

        // Add patient ID
        addComponent(new JLabel("Patient ID"), 25, 67,120,37, Colors.getWhite(),
                Colors.getMidGray(), 0, 15, Colors.getMidGray(), 2, 5,
                patientInfoPanel);
        textId = new JTextField();
        textId.setEditable(false);
        addComponent(textId, 160, 67,228,35, Colors.getWhite(),
                Colors.getMidGray(), 1, 13, Colors.getWhite(), 2, 5,
                patientInfoPanel);

        // Add patient Name
        addComponent(new JLabel("Name"), 25, 112, 120,37, Colors.getWhite(),
                Colors.getMidGray(), 0, 15, Colors.getMidGray(), 2, 5,
                patientInfoPanel);
        textName = new JTextField();
        addComponent(textName, 160, 112, 228,35, Colors.getWhite(),
                Colors.getMidGray(), 1, 13, Colors.getWhite(), 2, 5,
                patientInfoPanel);

        // Add patient Surname
        addComponent(new JLabel("Surname"), 25, 157, 120,37, Colors.getWhite(),
                Colors.getMidGray(), 0, 15, Colors.getMidGray(), 2, 5,
                patientInfoPanel);
        textSurname = new JTextField();
        addComponent(textSurname, 160, 157, 228,35, Colors.getWhite(),
                Colors.getMidGray(), 1, 13, Colors.getWhite(), 2, 7,
                patientInfoPanel);

        // Add patient Gender
        addComponent(new JLabel("Gender"), 25, 204, 120,37, Colors.getWhite(),
                Colors.getMidGray(), 0, 15, Colors.getMidGray(), 2, 5,
                patientInfoPanel);

        // Declare & add gender array with its values
        String[] genderList = {"Male", "Female"};
        JComboBox<String> genderBox = new JComboBox<>(genderList);
        addComponent(genderBox, 160, 204, 228,35, Colors.getWhite(),
                Colors.getMidGray(), 1, 13, Colors.getWhite(), 2, 5,
                patientInfoPanel);

        // Add patient DOB
        addComponent(new JLabel("Date of Birth"), 25, 250, 120,37, Colors.getWhite(),
                Colors.getMidGray(), 0, 15, Colors.getMidGray(), 2, 5,
                patientInfoPanel);
        DatePickerSettings dateSetting_1 = new DatePickerSettings();
        textDob = new DatePicker(dateSetting_1);
        dateSetting_1.setDateRangeLimits(date.minusYears(200), date);
        addComponent(textDob, 160, 250, 228,35, Colors.getWhite(),
                Colors.getMidGray(), 1, 13, Colors.getWhite(), 2, 5,
                patientInfoPanel);

        // patient Contact Number
        addComponent(new JLabel("Contact No"), 25, 295, 120,37, Colors.getWhite(),
                Colors.getMidGray(), 0, 15, Colors.getMidGray(), 2, 5,
                patientInfoPanel);
        textPhone = new JTextField();

        // Create a number filter
        ((AbstractDocument) textPhone.getDocument()).setDocumentFilter(new NumberFilter(10));
        addComponent(textPhone, 160, 295, 228,35, Colors.getWhite(),
                Colors.getMidGray(), 1, 13, Colors.getWhite(), 2, 5,
                patientInfoPanel);

        // Add Additional Info components
        additionalInfoPanel = new JPanel();
        additionalInfoPanel.setLayout(null);
        addComponent(additionalInfoPanel, 500, 100, 390, 350, Colors.getWhite(),
                Colors.getMidGray(), 0, 0, Colors.getBorderWhite(), 2, 0, infoPanel);

        // Add header panel
        JPanel additionalInfoHeaderPanel = new JPanel();
        additionalInfoHeaderPanel.setLayout(null);
        addComponent(additionalInfoHeaderPanel, 0, 0, 390, 50, Colors.getWhite(),
                Colors.getBorderWhite(), 0, 0, Colors.getBorderWhite(), 0, 0,
                additionalInfoPanel);

        // Add title to Additional info Panel
        addComponent(new JLabel("Additional Information", SwingConstants.CENTER), -5, 0, 420,
                50, Color.BLACK, Colors.getBorderWhite(), 0, 17, Colors.getSoftGray(),
                0, 0, additionalInfoHeaderPanel);

        // Add Additional Notes
        addComponent(new JLabel("Notes"), 25, 67,100,37, Colors.getWhite(),
                Colors.getMidGray(), 0, 15, Colors.getMidGray(), 2, 5,
                additionalInfoPanel);
        textNotes = new JTextArea();
        textNotes.setLineWrap(true);
        textNotes.setWrapStyleWord(true);
        JScrollPane scrollPanes = new JScrollPane(textNotes);
        addComponent(scrollPanes, 140, 67, 215,81, Colors.getWhite(),
                Colors.getMidGray(), 1, 13, Colors.getWhite(), 2, 7,
                additionalInfoPanel);

        // Add Additional File
        addComponent(new JLabel("Add files"), 25, 157, 100,37, Colors.getWhite(),
                Colors.getMidGray(), 0, 15, Colors.getMidGray(), 2, 5,
                additionalInfoPanel);
        textFilename = new JTextField();
        textFilename.setEditable(false);
        addComponent(textFilename, 140,157, 215,35, Colors.getWhite(),
                Colors.getMidGray(), 1, 13, Colors.getWhite(), 2, 5,
                additionalInfoPanel);

        // Add File Chooser Button
        JButton fileChooserButton = new JButton("Choose File");
        fileChooserButton.setFocusPainted(false);
        addComponent(fileChooserButton, 140,197, 215,30, Colors.getWhite(),
                Colors.getMidGray(), 1, 13, Colors.getWhite(), 2, 15,
                additionalInfoPanel);

        // Add consultation duration
        addComponent(new JLabel("Duration (Hr)"), 25, 239, 110,37, Colors.getWhite(),
                Colors.getMidGray(), 0, 15, Colors.getMidGray(), 2, 5,
                additionalInfoPanel);
        textDuration = new JTextField();
        textDuration.setEditable(false);

        // MAX £25*24h = 600
        ((AbstractDocument) textDuration.getDocument()).setDocumentFilter(new NumberFilter(600));
        addComponent(textDuration, 140, 239, 215,35, Colors.getWhite(),
                Colors.getMidGray(), 1, 13, Colors.getWhite(), 2, 5,
                additionalInfoPanel);

        // Add consultation fee
        addComponent(new JLabel("Total Fee"), 25, 290, 100,37, Colors.getWhite(),
                Colors.getMidGray(), 0, 15, Colors.getMidGray(), 2, 5,
                additionalInfoPanel);
        textFee = new JTextField();
        textFee.setEditable(false);
        addComponent(textFee, 140,290, 215,35, Colors.getWhite(),
                Colors.getMidGray(), 1, 13, Colors.getWhite(), 2, 5,
                additionalInfoPanel);
        textFee.setText("£0");

        // Add Clear Button
        JButton clearButton = new JButton("Clear");
        clearButton.setFocusPainted(false);
        addComponent(clearButton, 500, 460, 100,30, Colors.getWhite(),
                Colors.getMidGray(), 1, 13, Colors.getWhite(), 2, 15, infoPanel);

        // Add Reset Button
        JButton resetButton = new JButton("Reset");
        resetButton.setFocusPainted(false);
        addComponent(resetButton, 645, 460, 100,30, Colors.getWhite(),
                Colors.getMidGray(), 1, 13, Colors.getWhite(), 2, 15, infoPanel);

        // Add Confirm Button
        JButton submitButton = new JButton("Confirm");
        submitButton.setFocusPainted(false);
        addComponent(submitButton, 790,460, 100,30, Colors.getWhite(),
                Colors.getMidGray(), 1, 13, Colors.getWhite(), 2, 15, infoPanel);

        // Disable patient information panel
        enableAdditionalInfoPanel(false);

        // Add all components to body
        add(infoPanel);

//region EVENT LISTENERS AND ACTION LISTENERS

        // Event listener for the file chooser button
        fileChooserButton.addActionListener(e -> {

            // create an object of JFileChooser class
            JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

            // Accept only selected file types
            fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Image files (.jpg, .jpeg, .png)",
                    "jpg", "jpeg", "png"));

            // Determines whether the AcceptAll FileFilter
            fileChooser.setAcceptAllFileFilterUsed(false);

            // invoke the showsSaveDialog function to show the save dialog
            int saveDialog = fileChooser.showSaveDialog(null);

            // if the user selects a file
            if (saveDialog == JFileChooser.APPROVE_OPTION) {
                textFilename.setText(fileChooser.getSelectedFile().getAbsolutePath());
            } else {textFilename.setText("");}
        });

        // Event listener for the check button
        checkButton.addActionListener (e -> {
            try {

                // Validate all the required fields are filled
                switch (convertToIntResult(!NICText.getText().isEmpty() && !appointmentDate.toString().isEmpty()
                        && !appointmentStartTime.toString().isEmpty() && !appointmentEndTime.toString().isEmpty())) {

                    case 0 -> throw new RuntimeException("Please fill all the required fields!");
                    case 1 -> {
                        String NIC = NICText.getText().toLowerCase();
                        LocalTime currentTime = LocalTime.parse(LocalTime.now()
                                .format(DateTimeFormatter.ofPattern("HH:mm")));

                        // Get the start time and the end time for the validation
                        LocalTime startTime = appointmentStartTime.getTime();
                        LocalTime endTime = appointmentEndTime.getTime();
                        if (appointmentDate.getDate().equals(date)) {
                            if (!(startTime.isAfter(currentTime) && endTime.isAfter(appointmentStartTime.getTime()) &&
                                    endTime.isAfter(currentTime))) {
                                throw new RuntimeException("appointment start time should be after the current time!");
                            } else if (Objects.equals(startTime, endTime)) {
                                throw new RuntimeException("appointment end time should be after the start time!");
                            }
                        }

                        // Check if any doctor is available
                        switch (convertToIntResult(Data.getInstance().getNoOfAppointments() > 0)) {
                            case 0 -> throw new RuntimeException("No doctor is available at the moment!");
                            case 1 -> {

                                // NIC number should have either one of the following format
                                // 0123456789x, 0123456789v, 012345678910
                                // NIC should have length between 11 and 12
                                switch (convertToIntResult((NIC.endsWith("v") && isNumericalNIC(NIC) ||
                                        (NIC.endsWith("x") && isNumericalNIC(NIC)))
                                        || isInteger(NIC) && ( NIC.length() >= 11 && NIC.length() <= 12))) {
                                    case 0 -> throw new RuntimeException("Invalid NIC!");
                                    case 1 -> {

                                        // Check doctor's availability
                                        Doctor doctor = Data.getInstance().
                                                getDoctorByIndex(doctorList.getSelectedIndex());
                                        switch (convertToIntResult(Data.getInstance()
                                                .isDoctorAvailable(doctor.getLicense_number(),
                                                appointmentDate.getDate(), appointmentStartTime.getTime(),
                                                appointmentEndTime.getTime()))) {

                                            case 0 -> assignDoctor(startTime, endTime, NIC);
                                            case 1 -> {

                                                // Calculate the time difference
                                                double timeDifference = getTimeDifference(startTime, endTime);
                                                textDuration.setText(String.valueOf(timeDifference));

                                                // Determine the user by NIC
                                                checkPatientType(NIC, timeDifference);
                                                enableSelectionHeaderPanel(false);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(infoPanel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Event listener for the clear button
        clearButton.addActionListener (e -> {

            // Clear patient Details Panel
            textName.setText("");
            textSurname.setText("");
            textDob.setText("");
            try {
                textPhone.getDocument().remove(0, textPhone.getText().length());
            } catch (BadLocationException ex) {
                throw new RuntimeException(ex);
            }

            // Clear Additional Info Panel
            textNotes.setText("");
            textFilename.setText("");
        });

        // Event listener for the reset button
        resetButton.addActionListener (e -> {

            // Clear Header Info
            NICText.setText("");
            appointmentDate.setText("");
            appointmentStartTime.setText("");
            appointmentEndTime.setText("");

            // Clear patient Details Panel
            textId.setText("");
            textName.setText("");
            textSurname.setText("");
            textDob.setText("");
            try {
                textPhone.getDocument().remove(0, textPhone.getText().length());
            } catch (BadLocationException ex) {
                throw new RuntimeException(ex);
            }

            // Clear Additional Info Panel
            textNotes.setText("");
            try {
                textDuration.getDocument().remove(0, textDuration.getText().length());
            } catch (BadLocationException ex) {
                throw new RuntimeException(ex);
            }
            try {
                textFee.getDocument().remove(0, textFee.getText().length());
                textFee.setText("£0");
            } catch (BadLocationException ex) {
                throw new RuntimeException(ex);
            }
            textFilename.setText("");

            // Disable patient Details Panel & Additional Info Panel components
            enableAdditionalInfoPanel(false);

            // Enable Header Info components
            doctorList.setEnabled(true);
            NICText.setEnabled(true);
            appointmentDate.setEnabled(true);
            appointmentStartTime.setEnabled(true);
            appointmentEndTime.setEnabled(true);
            checkButton.setEnabled(true);
        });

        // Event listener for the submit button
        submitButton.addActionListener(e -> {
            try {
                switch (convertToIntResult(textId.getText().isEmpty() || textName.getText().isEmpty() ||
                        textSurname.getText().isEmpty() ||
                        textDob.getText().isEmpty() || textPhone.getText().isEmpty())) {
                    case 0 -> {

                        // Retrieve values from the user
                        String uniqueId = textId.getText().trim();
                        String NIC = NICText.getText().toLowerCase();
                        String name = textName.getText().trim();
                        String surname = textSurname.getText().trim();
                        LocalDate dob = textDob.getDate();
                        String gender = Objects.requireNonNull(genderBox.getSelectedItem()).toString();
                        LocalDate startDate = appointmentDate.getDate();
                        LocalTime startTime = appointmentStartTime.getTime();
                        LocalTime endTime = appointmentEndTime.getTime();
                        String phone = textPhone.getText().trim();
                        String path = textFilename.getText().trim();
                        BigDecimal cost = new BigDecimal(fee);
                        String fullPath = null;
                        String outputFile = null;

                        // If the additional note is not null, encrypt it
                        String note = null;
                        if (!textNotes.getText().isEmpty()) {
                            String messageToEncrypt = textNotes.getText().trim();
                            note = EncDecString.encryptText(messageToEncrypt);
                        }

                        // If the additional file is not null, encrypt it
                        boolean isFileAvailable = false;
                        if(!textFilename.getText().isEmpty()) {

                            // Retrieve current working directory & create a root folder, if not exists
                            String rootPath = System.getProperty("user.dir") + "\\data";
                            Path rootPaths = Paths.get(rootPath);
                            if (!Files.exists(rootPaths)) {
                                Files.createDirectories(rootPaths);
                            }

                            // Create a unique filename
                            fullPath = System.getProperty("user.dir") + "\\data" + "\\" + uniqueId;
                            Path uniquePaths = Paths.get(fullPath);
                            if (!Files.exists(uniquePaths)) {
                                Files.createDirectories(uniquePaths);
                            }

                            // Retrieve the absolute path to the file and get the file extension
                            String fileName = Paths.get(path).getFileName().toString();
                            String fileExtension = getExtension(new File(fileName));
                            int noOfFiles = Objects.requireNonNull(new File(fullPath).list()).length;

                            // Create a new filename
                            outputFile = fullPath + "\\" + fileName.replace(fileExtension, "") +
                                    (noOfFiles + 1) + fileExtension + ".skf";

                            // Encrypt File
                            EncDecFile.encryptFile(path, outputFile);
                            isFileAvailable = true;
                        }

                        // Create an instance of patient class and doctor
                        Doctor assignedDoctor = Data.getInstance().getDoctorByIndex(doctorList.getSelectedIndex());
                        Patient patient = new Patient(uniqueId, name, surname, gender, dob, phone, NIC);

                        // Construct the consultation
                        Consultation consultation;
                        if (note != null && isFileAvailable) {
                            consultation = new Consultation(assignedDoctor, patient, startDate,
                                    startTime, endTime, note, fullPath, cost);
                        } else if (note != null && !isFileAvailable) {
                            consultation = new Consultation(assignedDoctor, patient, startDate,
                                    startTime, endTime, cost, note);
                        } else if (isFileAvailable && note == null) {
                            consultation = new Consultation(assignedDoctor, patient, startDate,
                                    startTime, endTime, fullPath, outputFile, cost);
                        } else {
                            consultation = new Consultation(assignedDoctor, patient, startDate,
                                    startTime, endTime, cost);
                        }

                        // Update the consultation data
                        Data.getInstance().addConsultation(consultation);

                        // Create a new JPanel
                        JPanel summaryPanel = new JPanel();
                        summaryPanel.setLayout(null);
                        summaryPanel.setSize(350, 500);

                        JLabel messageHeader = new JLabel("Appointment has been recorded successfully!",
                                SwingConstants.CENTER);
                        messageHeader.setBounds(0,0, 500, 40);
                        summaryPanel.add(messageHeader);

                        // Show results to the user
                        JTable summary = new JTable();
                        DefaultTableModel tableModel =  new DefaultTableModel() {
                            // Set cell editable to false - cannot modify its data
                            @Override
                            public boolean isCellEditable(int row, int column) {return false;}
                        };
                        summary.setModel(tableModel);
                        summary.setTableHeader(null);
                        summary.setShowGrid(false);
                        summary.setEnabled(false);

                        tableModel.setColumnIdentifiers(new Object[]{null, null, null});

                        tableModel.addRow(new Object[] {"appointment has been recorded successfully!\n\n"});
                        tableModel.addRow(new Object[]{"patient ID", ":", uniqueId});
                        tableModel.addRow(new Object[]{"patient name", ":", name + " " + surname});
                        tableModel.addRow(new Object[]{"patient phone", ":", phone});

                        tableModel.addRow(new Object[]{"\ndoctor name", ":", assignedDoctor.getName()});
                        tableModel.addRow(new Object[]{"doctor surname", ":", assignedDoctor.getSurname()});
                        tableModel.addRow(new Object[]{"doctor specialisation", ":",
                                assignedDoctor.getSpecialisation()});

                        tableModel.addRow(new Object[]{"\nappointment Date", ":", startDate});
                        tableModel.addRow(new Object[]{"appointment start time", ":", startTime});
                        tableModel.addRow(new Object[]{"appointment duration", ":", textDuration.getText()});
                        tableModel.addRow(new Object[]{"appointment cost", ":", cost});

                        summary.getColumnModel().getColumn(0).setPreferredWidth(150);
                        summary.getColumnModel().getColumn(1).setPreferredWidth(1);
                        summary.getColumnModel().getColumn(2).setPreferredWidth(300);

                        for (int i = 0; i < 10; i++) {summary.setRowHeight(i, 40);}

                        JScrollPane scrollPane = new JScrollPane(summary);
                        scrollPane.setBounds(8, 50, 500, 550);
                        summaryPanel.add(scrollPane);

                        JOptionPane messageDialog = new JOptionPane(summaryPanel);
                        messageDialog.setMessageType(-1);
                        JDialog dialog = messageDialog.createDialog(null, "Success");
                        dialog.setSize(550, 545);
                        dialog.setVisible(true);
                    }
                    case 1 -> throw new RuntimeException("Please fill all the required information!");
                }
            } catch (RuntimeException runtimeException) {
                JOptionPane.showMessageDialog(infoPanel, runtimeException.getMessage(),
                        "Warning", JOptionPane.WARNING_MESSAGE);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(infoPanel, "An error occurred, please try again!", "Error",
                        JOptionPane.ERROR_MESSAGE);
            } finally {

                // Clear Header Info
                NICText.setText("");
                appointmentDate.setText("");
                appointmentStartTime.setText("");
                appointmentEndTime.setText("");

                // Clear patient Details Panel
                textId.setText("");
                textName.setText("");
                textSurname.setText("");
                textDob.setText("");
                textPhone.setText("");

                // Clear Additional Info Panel
                textNotes.setText("");
                textFilename.setText("");

                // Disable patient Details Panel & Additional Info Panel components
                enableAdditionalInfoPanel(false);

                // Enable Header Info components
                doctorList.setEnabled(true);
                NICText.setEnabled(true);
                appointmentDate.setEnabled(true);
                appointmentStartTime.setEnabled(true);
                appointmentEndTime.setEnabled(true);
                checkButton.setEnabled(true);
            }
        });
//endregion
    }

//region SUPPLEMENTAL METHODS

    // Enable or disable components on the selection Header Panel.
    private void enableSelectionHeaderPanel(Boolean mode) {
        doctorList.setEnabled(mode);
        NICText.setEnabled(mode);
        appointmentDate.setEnabled(mode);
        appointmentStartTime.setEnabled(mode);
        appointmentEndTime.setEnabled(mode);
        checkButton.setEnabled(mode);
    }

    // Assign a doctor for the consultation
    private void assignDoctor(LocalTime startTime, LocalTime endTime, String NIC) {

        // Declare a variable to store the doctor's name
        String doctorName = null;

        // Check for another doctor, if the selected doctor is not available
        LinkedList<DoctorAvailability> keys = Data.getInstance()
                .getSortedDoctorAvailabilityInformation();
        try {
            if (keys.size() != 0) {
                while (count.size() != keys.size()) {

                    // Generate a random number
                    int randomDoctorPosition = generateRandomNumber(keys.size());
                    if (!(count.contains(randomDoctorPosition))) {

                        // Update the count
                        count.add(randomDoctorPosition);

                        // Retrieve a random doctor from the list
                        String licenseKey = keys.get(randomDoctorPosition)
                                .getLicenseNumber();
                        DoctorAvailability currentDoctorAvailability =
                                Data.getInstance().
                                        getDoctorAvailability(licenseKey);

                        // Check doctor availability
                        if (Data.getInstance().isDoctorAvailable(licenseKey,
                                appointmentDate.getDate(),
                                appointmentStartTime.getTime(),
                                appointmentEndTime.getTime())) {
                            assert currentDoctorAvailability != null;
                            doctorName = currentDoctorAvailability
                                    .getFullName();
                            doctorList.setSelectedItem(doctorName);
                            doctorList.setEditable(false);

                            // If the end of operations reached
                            throw new EOFException();
                        }
                    }
                }
            }

            // If the program comes to this point, no doctor is available
            throw new NullPointerException();

        } catch (EOFException eofEx) {

            // Calculate the time difference
            double timeDifference = getTimeDifference(startTime, endTime);
            textDuration.setText(String.valueOf(timeDifference));

            // Determine the user by NIC
            checkPatientType(NIC, timeDifference);
            enableSelectionHeaderPanel(false);
            JOptionPane.showMessageDialog(infoPanel,
                    "The particular doctor is not available at the " +
                            "given date and time. \nWe have appointed Dr." +
                            doctorName + " for your convenience",
                    "information", JOptionPane.INFORMATION_MESSAGE);

        } catch (NullPointerException npEx) {
            JOptionPane.showMessageDialog(infoPanel,
                    "We regret to inform you that no doctor is " +
                            "available at the given date and time",
                    "information", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // Enable or disable components on the additionalInfoPanel Panel.
    private void enableAdditionalInfoPanel(Boolean mode) {
        textNotes.setEnabled(mode);
        for (Component component : additionalInfoPanel.getComponents()) {
            component.setEnabled(mode);
        }
        for (Component component : patientInfoPanel.getComponents()) {
            component.setEnabled(mode);
        }
    }

    // Validate the nic format for illegal characters
    private boolean isNumericalNIC(String nic) {
        int length = 0;
        for (int i=0; i<nic.length()-1; i++) {
            if (isInteger(Character.toString(nic.charAt(i)))) {length++;}
        } return (Objects.equals(nic.length()-2, length));
    }

    // patient type can be determined via NIC number which is unique.
    private void checkPatientType(String nic, double timeDifference) {

        // Check user type
        int userType = convertToIntResult(Data.getInstance().isPatientExists(nic));
        switch (userType) {

            // Execute for new users
            case 0 -> {

                // Assign a new unique ID, if not exists
                SysUID UID = new SysUID();
                textId.setText(UID.getUID());
            }

            // Execute for existing users
            case 1 -> {
                for (Consultation consultation : Data.getInstance().getConsultationData()) {

                    // Assign previous unique ID, if exists
                    if (Objects.equals(consultation.getPatient().getNIC(), nic)) {
                        textId.setText(consultation.getPatient().getUniqueId());
                        break;
                    }
                }
            }
        }

        // Update fee
        setFee(calculateCost(userType, timeDifference));
        textFee.setText(String.format("£%,.2f", fee));

        // Release consultation panel components
        enableAdditionalInfoPanel(true);
    }

    // Check whether the string input value last index contains an integer
    private boolean isInteger(String value){return Character.isDigit(value.charAt(value.length() -1));}

    // Calculate the cost for the consultation considering the user type as well as
    // the number of hours the appointment is scheduled. Cost can be high if the
    private double calculateCost(int userType, double hrs) {

        // User Types are as follows, USER 0: EXISTING PATIENT, USER 1: NEW PATIENT
        return userType==0 ? hrs * Cost.COST_N.getValue() : hrs * Cost.COST_O.getValue();
    }

    // Calculate the time difference in hours considering the appointment start time
    // and the appointment end time.
    private double getTimeDifference(LocalTime startTime, LocalTime endTime) {
        try {

            // Parsing the Time Period
            SimpleDateFormat DateFormat = new SimpleDateFormat("HH:mm");
            Date start_Time;
            Date end_Time;
            start_Time = DateFormat.parse(startTime.toString());
            end_Time = DateFormat.parse(endTime.toString());

            // Calculate time difference
            long differenceInMilliSeconds = Math.abs(end_Time.getTime() - start_Time.getTime());
            long differenceInHours = (differenceInMilliSeconds / (60 * 60 * 1000)) % 24;
            long differenceInMinutes = (differenceInMilliSeconds / (60 * 1000)) % 60;

            // If the minutes return 30, replace with 50
            long formattedMinutes = 0;
            if (differenceInMinutes == 30) {formattedMinutes = 50;}

            return Double.parseDouble(differenceInHours  + "." + formattedMinutes);
        } catch (ParseException ex) {
            throw new RuntimeException(ex);
        }
    }

    // Generate a random number between 0 and the number of doctors available.
    private int generateRandomNumber(int max) {return new Random().nextInt(max);}

    // Retrieve the extension of a file
    private String getExtension(File file) {
        int lastIndexOf = file.getName().lastIndexOf(".");
        if (lastIndexOf == -1) {return "";
        } return file.getName().substring(lastIndexOf);
    }

    // Setter method for the consultation fee.
    private void setFee(double fee) {this.fee = fee;}
//endregion
}