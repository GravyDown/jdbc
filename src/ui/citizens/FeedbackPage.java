package ui.citizens;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import model.User;
import util.DatabaseConnection;

public class FeedbackPage extends JFrame implements ActionListener {
    private JTextField categoryField, ratingField;
    private JTextArea commentsArea;
    private JButton submitButton, backButton;
    private User user;

    public FeedbackPage(User user) {
        this.user = user;
        setTitle("Feedback");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 2, 5, 5));

        add(new JLabel("Category:"));
        categoryField = new JTextField();
        add(categoryField);

        add(new JLabel("Rating (1-5):"));
        ratingField = new JTextField();
        add(ratingField);

        add(new JLabel("Comments:"));
        commentsArea = new JTextArea();
        add(new JScrollPane(commentsArea));

        submitButton = new JButton("Submit Feedback");
        submitButton.addActionListener(this);
        add(submitButton);

        backButton = new JButton("Back");
        backButton.addActionListener(this);
        add(backButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submitButton) {
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO Feedback (user_id, feedback_category, ratings, comments) VALUES (?, ?, ?, ?)")) {
                stmt.setInt(1, user.getUserId());
                stmt.setString(2, categoryField.getText());
                stmt.setInt(3, Integer.parseInt(ratingField.getText()));
                stmt.setString(4, commentsArea.getText());
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Feedback Submitted!");
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