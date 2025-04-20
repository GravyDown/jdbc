package ui.officer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import model.User;
import java.sql.*;

public class CaseUpdatePage extends JFrame implements ActionListener {
    private JTextField reportIdField;
    private JComboBox<String> statusCombo;
    private JButton updateButton, backButton;
    private User user;

    public CaseUpdatePage(User user) {
        this.user = user;
        setTitle("Update Case Status");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        formPanel.add(new JLabel("Report ID:"));
        reportIdField = new JTextField();
        formPanel.add(reportIdField);

        formPanel.add(new JLabel("New Status:"));
        statusCombo = new JComboBox<>(new String[]{"PENDING", "UNDER_INVESTIGATION", "RESOLVED", "CLOSED"});
        formPanel.add(statusCombo);

        add(formPanel, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        updateButton = new JButton("Update");
        updateButton.addActionListener(this);
        buttonPanel.add(updateButton);

        backButton = new JButton("Back");
        backButton.addActionListener(this);
        buttonPanel.add(backButton);

        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == updateButton) {
            String reportIdText = reportIdField.getText().trim();
            String newStatus = (String) statusCombo.getSelectedItem();

            if (reportIdText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Report ID is required!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int reportId;
            try {
                reportId = Integer.parseInt(reportIdText);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Report ID must be a number!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Connection con = null;
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                con = DriverManager.getConnection("jdbc:mysql://localhost:3306/cras", "root", "garvit27");
                System.out.println("Database connection successful in CaseUpdatePage");

                // Verify the report is assigned to this officer
                PreparedStatement checkStmt = con.prepareStatement(
                    "SELECT COUNT(*) FROM officer_assigned_cases WHERE report_id = ? AND officer_id = " +
                    "(SELECT officer_id FROM Officers WHERE user_id = ?)");
                checkStmt.setInt(1, reportId);
                checkStmt.setInt(2, user.getUserId());
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next() && rs.getInt(1) == 0) {
                    JOptionPane.showMessageDialog(this, "This report is not assigned to you!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Update status
                PreparedStatement stmt = con.prepareStatement(
                    "UPDATE incident_report SET status = ? WHERE report_id = ?");
                stmt.setString(1, newStatus);
                stmt.setInt(2, reportId);
                int rows = stmt.executeUpdate();

                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "Case status updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    reportIdField.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update case status: Report ID not found!", "Error", JOptionPane.ERROR_MESSAGE);
                }

            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "MySQL JDBC Driver not found!", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error updating case status: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } finally {
                if (con != null) {
                    try {
                        con.close();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        } else if (e.getSource() == backButton) {
            new MyCasesPage(user).setVisible(true);
            this.dispose();
        }
    }
}