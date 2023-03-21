package org.skin.consultation.management.gui;

import org.skin.consultation.management.gui.component.RoundedJBorder;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;


public interface UserPanel {

    // Add components to JPanel with specified formatting
    default void addComponent(javax.swing.JComponent component, int x_axis, int y_axis, int width, int height,
                              Color foregroundColor, Color backgroundColor, int fontType, int fontSize,
                              Color borderColor, int lineSize, int cornerSize, JPanel parentPanel) {
        component.setBounds(x_axis, y_axis, width,height);
        component.setForeground(foregroundColor);
        if (fontType == 0) {component.setFont(new Font("Inter", Font.BOLD, fontSize));}
        else {component.setFont(new Font("Inter", Font.PLAIN, fontSize));}
        component.setBackground(backgroundColor);
        component.setBorder(new RoundedJBorder(borderColor, lineSize, cornerSize));
        parentPanel.add(component);
    }

    // Filter numeric input
    class NumberFilter extends DocumentFilter {
        private final int length;
        public NumberFilter(int length) {
            this.length = length;
        }

        // Check if the input is numeric
        private boolean isNumeric(String strNum) {
            if (strNum == null) {
                return false;
            }
            else {
                try {
                    Double.parseDouble(strNum);
                } catch (NumberFormatException nfe) {
                    return false;
                } return true;
            }
        }

        // Allow conditional input
        @Override
        public void insertString(FilterBypass fb, int offset, String string,
                                 AttributeSet attr) throws BadLocationException {
            if (isNumeric(string)) {
                if (this.length > 0 && fb.getDocument().getLength() + string.length() > this.length) {
                    return;
                }
                super.insertString(fb, offset, string, attr);
            }
        }

        // Replace a text
        @Override
        public void replace(FilterBypass fb, int offset, int length, String text,
                            AttributeSet attrs) throws BadLocationException {
            if (isNumeric(text)) {
                if (this.length > 0 && fb.getDocument().getLength() + text.length() > this.length) {
                    return;
                }
                super.insertString(fb, offset, text, attrs);
            }
        }

        // Remove text
        @Override
        public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
            super.remove(fb, offset, length);
        }
    }

    // Convert boolean value in to numerical value
    default int convertToIntResult(boolean value) {return value ? 1:0;}

    // Add listener to search bar
    default void addSearchListener(JTable table, JTextField searchField) {
        // Implement sorter for search bar
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel());

        // Implement row sorter for the table
        table.setRowSorter(sorter);

        // Implement document listener
        searchField.getDocument().addDocumentListener(new DocumentListener(){
            public void insertUpdate(DocumentEvent e) {

                String text = searchField.getText().trim();
                switch (convertToIntResult(text.length() == 0)) {
                    case 0 -> sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                    case 1 -> sorter.setRowFilter(null);
                }
            }
            public void removeUpdate(DocumentEvent e) {

                String text = searchField.getText().trim();
                switch (convertToIntResult(text.length() == 0)) {
                    case 0 -> sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                    case 1 -> sorter.setRowFilter(null);
                }
            }
            public void changedUpdate(DocumentEvent e) {}
        });
    }
}