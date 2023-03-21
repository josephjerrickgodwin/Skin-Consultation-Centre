package org.skin.consultation.management.gui.controller.information;

import org.skin.consultation.management.gui.UserPanel;
import org.skin.consultation.storage.Data;
import org.skin.consultation.users.doctor.Doctor;
import org.skin.consultation.utils.Colors;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.LinkedList;
import java.util.Objects;

public final class DoctorInformation extends JFrame implements UserPanel {
    public DoctorInformation() {

        // Create a header panel
        JPanel top = new JPanel();
        top.setLayout(null);
        top.setBackground(new Color(50, 50, 57));
        top.setSize(1020, 167);

        // Create a header label
        JLabel label = new JLabel("Doctor Information Subsystem", SwingConstants.CENTER);
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
        add(infoPanel);

        // Add sort menu
        addComponent(new JLabel("Sort doctor by", SwingConstants.LEFT), 56, 40, 170,25,
                Colors.getWhite(), Colors.getLightGray(), 0, 17, Colors.getFixedGray(), 0, 0, infoPanel);

        // Add comboBox & attributes
        JComboBox<String> comboBoxAttributes = new JComboBox<>(new String[] {"Name","Surname", "Specialisation"});
        addComponent(comboBoxAttributes, 190, 35, 200, 35, Colors.getWhite(), Colors.getFixedGray(),
                0, 13, Colors.getSoftGray(), 2, 5, infoPanel);

        // doctor name label
        addComponent(new JLabel("Search doctor", SwingConstants.LEFT), 560, 40, 170,25,
                Colors.getWhite(), Colors.getLightGray(), 0, 17, Colors.getFixedGray(),
                0, 0, infoPanel);
        JTextField searchText = new JTextField();
        addComponent(searchText, 690, 35, 200, 35, Colors.getWhite(), Colors.getFixedGray(),
                0, 13, Colors.getSoftGray(), 2, 5, infoPanel);

        // Create a table
        JTable table = new JTable();
        DefaultTableModel tableModel =  new DefaultTableModel() {

            // Set cell editable to false - cannot modify its data
            @Override
            public boolean isCellEditable(int row, int column) {return false;}
        };

        // Add column title
        tableModel.setColumnIdentifiers(new String[]{ "Name","Surname","Specialisation","Date of Birth",
                "License Number","Mobile Number"});
        table.setModel(tableModel);

        // adding ScrollPane to Table
        JScrollPane jScrollPane = new JScrollPane(table);

        // Add ScrollPane to the JPanel
        addComponent(jScrollPane, 56, 100, 841, 400, Colors.getWhite(), Colors.getFixedGray(),
                0, 13, Colors.getWhite(), 6, 10, infoPanel);

        // Add default values to an array
        for (String key : Data.getInstance().getDoctorData().keySet()) {
            Doctor doctor = Data.getInstance().getDoctor(key);

            // Add data to table
            tableModel.addRow(new Object[]{doctor.getName(), doctor.getSurname(), doctor.getSpecialisation(),
                    doctor.getDate_of_birth(), doctor.getLicense_number(), doctor.getMobile_number()
            });
        }

        // Add actionListener to comboBox
        comboBoxAttributes.addActionListener (e -> {

            // Clear all data from the table
            ((DefaultTableModel)table.getModel()).setRowCount(0);

            // Add new values to the table
            switch (Objects.requireNonNull(comboBoxAttributes.getSelectedItem()).toString()) {

                // Sort by name
                case "Name" -> updateTable(tableModel, Data.getInstance().sortDoctorInformation("name"));

                // Sort by surname
                case "Surname" -> updateTable(tableModel, Data.getInstance().sortDoctorInformation("surname"));

                // Sort by specialisation
                case "Specialisation" -> updateTable(tableModel, Data.getInstance().
                        sortDoctorInformation("specialisation"));
            }
        });

        // Add an event listener to the search bar
        addSearchListener(table, searchText);
    }

    // Update the table
    private void updateTable(DefaultTableModel tableModel, LinkedList<Doctor> doctorList) {
        for (Doctor doctor : doctorList) {
            tableModel.addRow(new Object[]{doctor.getName(), doctor.getSurname(), doctor.getSpecialisation(),
                    doctor.getDate_of_birth(), doctor.getLicense_number(), doctor.getMobile_number()
            });
        }
    }
}