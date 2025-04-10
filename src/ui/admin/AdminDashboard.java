package ui.admin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import model.User;

public class AdminDashboard extends JFrame implements ActionListener {
    private JButton crimeManagementButton, officerAssignmentButton, logoutButton;
    private User user;

    public AdminDashboard(User user) {
        this.user = user;
        setTitle("Admin Dashboard");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        crimeManagementButton = new JButton("Crime Management");
        crimeManagementButton.addActionListener(this);
        buttonPanel.add(crimeManagementButton);

        officerAssignmentButton = new JButton("Officer Assignment");
        officerAssignmentButton.addActionListener(this);
        buttonPanel.add(officerAssignmentButton);

        logoutButton = new JButton("Logout");
        logoutButton.setBackground(new Color(220, 20, 60)); // Crimson
        logoutButton.setForeground(Color.WHITE);
        logoutButton.addActionListener(this);
        buttonPanel.add(logoutButton);

        add(buttonPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == crimeManagementButton) {
            new ui.admin.CrimeManagementPage(user).setVisible(true);
            this.dispose();
        } else if (e.getSource() == officerAssignmentButton) {
            new ui.admin.OfficerAssignmentPage(user).setVisible(true);
            this.dispose();
        } else if (e.getSource() == logoutButton) {
            new ui.LoginPage().setVisible(true);
            this.dispose();
        }
    }
}