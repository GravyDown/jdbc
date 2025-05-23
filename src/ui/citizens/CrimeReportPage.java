package ui.citizens;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import model.User;
import java.sql.*;

public class CrimeReportPage extends JFrame implements ActionListener {
    private JTextField crimeTypeField, locationField, evidenceField;
    private JTextArea descriptionArea;
    private JButton submitButton, resetButton, myReportsButton, feedbackButton, logoutButton;
    private User user;

    public CrimeReportPage(User user) {
        this.user = user;
        setTitle("Report a Crime");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(240, 248, 255));

        JLabel header = new JLabel("Report a Crime", SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 24));
        header.setForeground(new Color(25, 25, 112));
        add(header, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        formPanel.setBackground(new Color(240, 248, 255));

        formPanel.add(new JLabel("Crime Type:"));
        crimeTypeField = new JTextField();
        crimeTypeField.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(crimeTypeField);

        formPanel.add(new JLabel("Description:"));
        descriptionArea = new JTextArea(5, 20);
        descriptionArea.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(new JScrollPane(descriptionArea));

        formPanel.add(new JLabel("Location:"));
        locationField = new JTextField();
        locationField.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(locationField);

        formPanel.add(new JLabel("Evidence URL (optional):"));
        evidenceField = new JTextField();
        evidenceField.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(evidenceField);

        add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(240, 248, 255));

        resetButton = new JButton("Reset");
        resetButton.setFont(new Font("Arial", Font.BOLD, 14));
        resetButton.setBackground(new Color(255, 165, 0));
        resetButton.setForeground(Color.WHITE);
        resetButton.addActionListener(this);
        buttonPanel.add(resetButton);

        submitButton = new JButton("Submit");
        submitButton.setFont(new Font("Arial", Font.BOLD, 14));
        submitButton.setBackground(new Color(60, 179, 113));
        submitButton.setForeground(Color.WHITE);
        submitButton.addActionListener(this);
        buttonPanel.add(submitButton);

        myReportsButton = new JButton("My Reports");
        myReportsButton.setFont(new Font("Arial", Font.BOLD, 14));
        myReportsButton.setBackground(new Color(135, 206, 235));
        myReportsButton.setForeground(Color.WHITE);
        myReportsButton.addActionListener(this);
        buttonPanel.add(myReportsButton);

        feedbackButton = new JButton("Feedback");
        feedbackButton.setFont(new Font("Arial", Font.BOLD, 14));
        feedbackButton.setBackground(new Color(147, 112, 219));
        feedbackButton.setForeground(Color.WHITE);
        feedbackButton.addActionListener(this);
        buttonPanel.add(feedbackButton);

        logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Arial", Font.BOLD, 14));
        logoutButton.setBackground(new Color(220, 20, 60)); // Crimson for logout
        logoutButton.setForeground(Color.WHITE);
        logoutButton.addActionListener(this);
        buttonPanel.add(logoutButton);

        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == resetButton) {
            crimeTypeField.setText("");
            descriptionArea.setText("");
            locationField.setText("");
            evidenceField.setText("");
        } else if (e.getSource() == submitButton) {
            String crimeType = crimeTypeField.getText().trim();
            String description = descriptionArea.getText().trim();
            String location = locationField.getText().trim();
            String evidenceUrl = evidenceField.getText().trim();

            if (crimeType.isEmpty() || description.isEmpty() || location.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Crime Type, Description, and Location are required!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Connection con = null;
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                con = DriverManager.getConnection("jdbc:mysql://localhost:3306/crime_system", "root", "garvit27");

                PreparedStatement citizenStmt = con.prepareStatement("SELECT citizen_id FROM Citizens WHERE user_id = ?");
                citizenStmt.setInt(1, user.getUserId());
                ResultSet rs = citizenStmt.executeQuery();
                if (!rs.next()) {
                    JOptionPane.showMessageDialog(this, "Citizen ID not found for this user!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int citizenId = rs.getInt("citizen_id");

                PreparedStatement stmt = con.prepareStatement(
                    "INSERT INTO Crime_Reports (citizen_id, crime_type, description, location, evidence_url, status) " +
                    "VALUES (?, ?, ?, ?, ?, 'PENDING')");
                stmt.setInt(1, citizenId);
                stmt.setString(2, crimeType);
                stmt.setString(3, description);
                stmt.setString(4, location);
                stmt.setString(5, evidenceUrl.isEmpty() ? null : evidenceUrl);
                int rows = stmt.executeUpdate();

                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "Report submitted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    crimeTypeField.setText("");
                    descriptionArea.setText("");
                    locationField.setText("");
                    evidenceField.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to submit report!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "MySQL JDBC Driver not found!", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error submitting report: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } finally {
                if (con != null) {
                    try {
                        con.close();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        } else if (e.getSource() == myReportsButton) {
            new ui.citizens.MyReportsPage(user).setVisible(true);
            this.dispose();
        } else if (e.getSource() == feedbackButton) {
            new ui.citizens.FeedbackPage(user).setVisible(true);
            this.dispose();
        } else if (e.getSource() == logoutButton) {
            new ui.LoginPage().setVisible(true);
            this.dispose();
        }
    }
}