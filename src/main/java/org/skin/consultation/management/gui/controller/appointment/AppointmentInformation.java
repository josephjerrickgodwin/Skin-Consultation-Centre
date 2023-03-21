package org.skin.consultation.management.gui.controller.appointment;

import org.skin.consultation.management.gui.UserPanel;
import org.skin.consultation.storage.Data;
import org.skin.consultation.users.doctor.consultation.Consultation;
import org.skin.consultation.utils.Colors;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Objects;

public final class AppointmentInformation extends JFrame implements UserPanel {
    private ArrayList<String> doctorLicense = new ArrayList<>();
    private int selectedRow = 0;
    public AppointmentInformation() {

        // Create a header panel
        JPanel top = new JPanel();
        top.setLayout(null);
        top.setBackground(new Color(50, 50, 57));
        top.setSize(1020, 167);

        // Create a header label
        JLabel label = new JLabel("Appointment Information Subsystem", SwingConstants.CENTER);
        label.setBounds(0, 64, 1020, 36);
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

        // Add a search bar
        addComponent(new JLabel("Search appointment", SwingConstants.LEFT), 35, 40, 170,25,
                Colors.getWhite(), Colors.getLightGray(), 0, 17, Colors.getLightGray(), 0, 0, infoPanel);
        JTextField searchText = new JTextField();
        searchText.setEditable(false);
        addComponent(searchText, 220, 35, 200, 35, Colors.getWhite(), Colors.getFixedGray(),
                0, 13, Colors.getSoftGray(), 2, 5, infoPanel);

        // Add a button to view an encrypted file
        JButton viewButton = new JButton("View data");
        viewButton.setFocusPainted(false);
        viewButton.setEnabled(false);
        addComponent(viewButton, 815,40, 100,30, Colors.getWhite(),
                Colors.getMidGray(), 1, 13, Colors.getWhite(), 2, 15, infoPanel);

        // Create a table
        JTable table = new JTable();
        DefaultTableModel tableModel =  new DefaultTableModel() {

            // Set cell editable to false - cannot modify its data
            @Override
            public boolean isCellEditable(int row, int column) {return false;}
        };

        // Initialize the Table & data
        String[] columnNames = {"patient ID", "patient Name", "patient Surname", "doctor Name", "doctor Surname",
                "Date", "Time", "Cost", "data"};

        // Add column title
        tableModel.setColumnIdentifiers(columnNames);
        table.setModel(tableModel);

        // adding ScrollPane to Table
        JScrollPane jScrollPane = new JScrollPane(table);

        // Add values to the table
        for (Consultation consultation : Data.getInstance().getConsultationData()) {

            // Add data to table
            assert consultation != null; // Make sure the consultation is not null
            tableModel.addRow(new Object[] {consultation.getPatient().getUniqueId(), consultation.getPatient().getName(),
                    consultation.getPatient().getSurname(), consultation.getDoctor().getName(),
                    consultation.getDoctor().getSurname(), consultation.getDate(),
                    consultation.getStartTime() + " - " + consultation.getEndTime(), "£" + consultation.getCost(),
                    checkData(consultation)});

            // Store doctor license key to retrieve later
            doctorLicense.add(consultation.getDoctor().getLicense_number());
        }

        // Set Components on the panel
        addComponent(jScrollPane, 25, 90,900,415, Colors.getWhite(),
                Colors.getMidGray(), 0, 15, Colors.getWhite(), 6, 10, infoPanel);

        // Add all components to the JPanel
        add(infoPanel);

//region EVENT LISTENERS

        // Add action listener to the view button
        viewButton.addActionListener(e -> {

            // Retrieve doctor license number from the selected row
            String doctorLicenseNumber = doctorLicense.get(selectedRow);
            Consultation selectedConsultation = Data.getInstance().getConsultation(doctorLicenseNumber);

            // Prompt the dataView panel
            try {
                assert selectedConsultation != null;
                AdditionalData additionalData = new AdditionalData(selectedConsultation);
                additionalData.setResizable(false);
                additionalData.setTitle("Patient Additional Information");
                additionalData.setSize(550, 450);
                additionalData.setVisible(true);
            } catch (RuntimeException runtimeException) {
                JOptionPane.showMessageDialog(infoPanel, "An error occurred while processing the data!",
                        "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception exception) {
                JOptionPane.showMessageDialog(infoPanel, "An unknown error occurred!",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }

        });

        // Add mouse action listener to the table
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {

                // Retrieve the selected row
                selectedRow = table.getSelectedRow();

                // Retrieve the data type of the selected appointment
                String dataType = tableModel.getValueAt(selectedRow, 8).toString();
                viewButton.setEnabled(!Objects.equals(dataType, "No data"));
            }
        });

        // Add search event listener
        addSearchListener(table, searchText);
//endregion
    }

    // Check what type of data the user has inserted
    private String checkData(Consultation consultation) {
        if (consultation.isNoteAvailable() && consultation.isFileAvailable()) {
            return "Note & File";
        } else if (consultation.isNoteAvailable() && !consultation.isFileAvailable() ) {
            return "Note";
        } else if (consultation.isFileAvailable() && !consultation.isNoteAvailable()) {
            return "File";
        } else {
            return "No data";
        }
    }
}