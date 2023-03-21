package org.skin.consultation.management.gui.controller.appointment;

import org.skin.consultation.management.gui.cryptography.files.EncDecFile;
import org.skin.consultation.management.gui.component.RoundedJBorder;
import org.skin.consultation.management.gui.UserPanel;
import org.skin.consultation.users.doctor.consultation.Consultation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.skin.consultation.management.gui.cryptography.data.EncDecString;
import org.skin.consultation.utils.Colors;

public class AdditionalData extends JFrame implements UserPanel {
    private ArrayList<String> viewedFiles = new ArrayList<>();
    public AdditionalData(Consultation consultation) throws RuntimeException {

        // Make panel absolute positioning
        Container body = getContentPane();
        body.setLayout(null);
        body.setBackground(Colors.getPitchGray());

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(null);
        infoPanel.setBackground(Colors.getMidGray());
        infoPanel.setBounds(10, 10, 516, 392);

        // Create a header panel
        JPanel top = new JPanel();
        top.setLayout(null);
        top.setBackground(Colors.getSemiGray());
        top.setSize(516, 90);

        // Create a header label
        JLabel label = new JLabel("Patient Additional information", SwingConstants.CENTER);
        label.setBounds(0, 25, 516, 35);
        label.setForeground(Colors.getWhite());
        label.setFont(new Font("Inter", Font.BOLD, 18));
        top.add(label);
        infoPanel.add(top);

        // Add patient Note
        addComponent(new JLabel("Patient Note"), 15, 150, 120,37, Colors.getWhite(),
                Colors.getMidGray(), 0, 15, Colors.getMidGray(), 2, 5, infoPanel);

        // Add patient Note to textArea
        JTextArea textNote = new JTextArea();
        textNote.setLineWrap(true);
        textNote.setWrapStyleWord(true);
        textNote.setEditable(false);

        // Check if there are any notes available
        if (consultation.isNoteAvailable()) {
            try {

                // Decrypt the note and append to the JTextField
                String note = EncDecString.decryptText(consultation.getNote());
                textNote.append(note);
            } catch (Exception ex) {
                throw new RuntimeException("An error occurred while getting the patient note!");
            }
        } else {

            // If the note is not available, disable the JTextField
            textNote.setEnabled(false);
        }

        // Add Scroll Panel
        JScrollPane scrollPane = new JScrollPane(textNote);
        addComponent(scrollPane, 140,110, 360,125, Colors.getWhite(),
                Colors.getMidGray(), 1, 13, Colors.getWhite(), 2, 5, infoPanel);

        // Add patient data, If Available
        addComponent(new JLabel("Patient data"), 15, 260, 120,37, Colors.getWhite(),
                Colors.getMidGray(), 0, 15, Colors.getMidGray(), 2, 5, infoPanel);

        // Add File Count
        addComponent(new JLabel("No. of files :"), 140, 260, 120,37, Colors.getWhite(),
                Colors.getMidGray(), 0, 15, Colors.getMidGray(), 2, 5, infoPanel);

        // Get no of files available in the directory
        int noOfFiles = 0;
        JComboBox<String> doctorList = new JComboBox<>();
        doctorList.setEditable(false);
        addComponent(doctorList, 140, 300, 270, 35, Colors.getWhite(), Colors.getFixedGray(),
                0, 13, Colors.getSoftGray(), 2, 5, infoPanel);

        // Add View Button
        JButton viewButton = new JButton("View");
        viewButton.setFocusPainted(false);
        addComponent(viewButton, 416,300, 84,35, Colors.getWhite(),
                Colors.getMidGray(), 1, 13, Colors.getWhite(), 2, 15, infoPanel);

        // Check if the user has updated the record with a file
        if (consultation.isFileAvailable()) {

            // Retrieve the file path
            String filePath = consultation.getFilePath();
            if (Files.exists(Path.of(filePath))) {

                // Retrieve file count and throw exception, if its zero
                noOfFiles = Objects.requireNonNull(new File(filePath).list()).length;
                if (noOfFiles == 0) {throw new RuntimeException();}

                // Add a comboBox containing all the file names
                String[] fileNames = new String[noOfFiles];
                List<String> fileList;
                try {
                    fileList = getFiles(Paths.get(filePath));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                for (int i = 0; i < noOfFiles; i++) {fileNames[i] =
                        String.valueOf(Path.of(fileList.get(i)).getFileName());}

                // Reset the ComboBox
                doctorList.removeAllItems();

                // Update the ComboBox
                for (int i = 0; i < noOfFiles; i++) {doctorList.addItem(fileNames[i]);}

            } else {
                throw new RuntimeException("An unauthorised modification is detected on patient data!");
            }
        } else {
            doctorList.setEnabled(false);
            viewButton.setEnabled(false);
        }
        addComponent(new JLabel(String.valueOf(noOfFiles)), 250, 260, 150,37, Colors.getWhite(),
                Colors.getMidGray(), 0, 15, Colors.getMidGray(), 0, 0, infoPanel);

//region EVENT LISTENERS

        // Add View Button Event Listener
        viewButton.addActionListener(evt -> {
            if (evt.getActionCommand().equals("View")) {

                // Retrieve the selected file name
                String selectedFileName = Objects.requireNonNull(doctorList.getSelectedItem()).toString();
                String selectedFileFullPath = consultation.getFilePath() + "\\" + selectedFileName;
                try {
                    // Get the file extension
                    String fileExtension = selectedFileFullPath.substring(
                            selectedFileFullPath.lastIndexOf(".skf") + 1);

                    // Decrypt the file
                    EncDecFile.decryptFile(selectedFileFullPath, selectedFileFullPath.replace(
                            "." + fileExtension, ""));
                    viewedFiles.add(selectedFileFullPath);

                    // Get file absolute path
                    File fileDirectory = new File(consultation.getFilePath());

                    // Open the file explorer
                    Desktop.getDesktop().open(fileDirectory);
                } catch (Exception e) {
                    throw new RuntimeException("An error occurred while processing the file!");
                } finally {
                    viewButton.setText("Close");
                }
            } else {
                if (viewedFiles.size() != 0) {
                    for (String viewedFile : viewedFiles) {

                        // Delete the viewed file
                        File currentViewedFile = new File(viewedFile.replace(".skf", ""));
                        currentViewedFile.delete();
                    }
                }
                viewButton.setText("View");
            }
        });

        // Add mouse listener to view button
        viewButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                viewButton.setBorder(new RoundedJBorder(Colors.getSoftGray(), 3, 25));
            }
            public void mouseExited(MouseEvent evt) {
                viewButton.setBorder(new RoundedJBorder(Colors.getWhite(), 2, 25));
            }
        });
//endregion

        // Add all components to the JPanel
        add(infoPanel);
    }

    private List<String> getFiles(Path path) throws Exception {
        List<String> fileList;
        try (Stream<Path> walk = Files.walk(path)) {
            fileList = walk
                    .filter(p -> !Files.isDirectory(p))
                    .map(p -> p.getFileName().toString().toLowerCase())
                    .filter(f -> f.endsWith(".skf"))
                    .collect(Collectors.toList());
        } return fileList;
    }
}