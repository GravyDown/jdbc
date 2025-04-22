package ui.citizens;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import model.User;
import util.DatabaseConnection;

public class FeedbackPage extends JFrame implements ActionListener {
    private JComboBox<Long> reportIdDropdown;
    private JTextField ratingField;
    private JTextArea complaintArea;
    private JButton submitButton, backButton;
    private User user;

    public FeedbackPage(User user) {
        this.user = user;
        setTitle("Feedback");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 2, 5, 5));

        add(new JLabel("Select Report ID:"));
        reportIdDropdown = new JComboBox<>();
        loadUserReports(); // Populate dropdown
        add(reportIdDropdown);

        add(new JLabel("Star Rating (1-5):"));
        ratingField = new JTextField();
        add(ratingField);

        add(new JLabel("Complaint:"));
        complaintArea = new JTextArea();
        add(new JScrollPane(complaintArea));

        submitButton = new JButton("Submit Feedback");
        submitButton.addActionListener(this);
        add(submitButton);

        backButton = new JButton("Back");
        backButton.addActionListener(this);
        add(backButton);
    }

    private void loadUserReports() {
        try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(
            "SELECT ir.report_id " +
            "FROM incident_report ir " +
            "JOIN citizen c ON ir.citizen_id = c.citizen_id " +
            "WHERE c.user_id = ?"
        );) {
            stmt.setInt(1, user.getUserId());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                reportIdDropdown.addItem(rs.getLong("report_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading reports.");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submitButton) {
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO feedback (report_id, star_rating, complaint) VALUES (?, ?, ?)")) {

                Long selectedReportId = (Long) reportIdDropdown.getSelectedItem();
                int rating = Integer.parseInt(ratingField.getText());
                String complaint = complaintArea.getText();

                stmt.setLong(1, selectedReportId);
                stmt.setInt(2, rating);
                stmt.setString(3, complaint);

                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Feedback Submitted!");
                ratingField.setText("");
                complaintArea.setText("");

            } catch (SQLException | NumberFormatException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error submitting feedback!");
            }
        } else if (e.getSource() == backButton) {
            new CrimeReportPage(user).setVisible(true);
            this.dispose();
        }
    }
}
