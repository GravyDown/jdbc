package ui.admin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import model.User;
import util.DatabaseConnection;

public class OfficerAssignmentPage extends JFrame implements ActionListener {
    private JTextField reportIdField, officerIdField;
    private JButton assignButton, backButton;
    private User user;

    public OfficerAssignmentPage(User user) {
        this.user = user;
        setTitle("Officer Assignment");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 2, 10, 10));

        add(new JLabel("Report ID:"));
        reportIdField = new JTextField();
        add(reportIdField);

        add(new JLabel("Officer ID:"));
        officerIdField = new JTextField();
        add(officerIdField);

        assignButton = new JButton("Assign");
        assignButton.addActionListener(this);
        add(assignButton);

        backButton = new JButton("Back");
        backButton.addActionListener(this);
        add(backButton);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == assignButton) {
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO officer_assigned_cases (report_id, officer_id) VALUES (?, ?)")) {
                stmt.setInt(1, Integer.parseInt(reportIdField.getText()));
                stmt.setInt(2, Integer.parseInt(officerIdField.getText()));
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Officer Assigned!");
            } catch (SQLException | NumberFormatException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error assigning officer!");
            }
        } else if (e.getSource() == backButton) {
            new AdminDashboard(user).setVisible(true);
            this.dispose();
        }
    }
}