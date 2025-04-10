package ui.officer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import model.User;
import model.CrimeReport;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MyCasesPage extends JFrame implements ActionListener {
    private JTextArea casesArea;
    private JButton updateButton, backButton, logoutButton;
    private User user;

    public MyCasesPage(User user) {
        this.user = user;
        setTitle("My Cases - Officer " + user.getName());
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        casesArea = new JTextArea();
        casesArea.setEditable(false);
        add(new JScrollPane(casesArea), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        updateButton = new JButton("Update Case");
        updateButton.addActionListener(this);
        buttonPanel.add(updateButton);

        backButton = new JButton("Back");
        backButton.addActionListener(this);
        buttonPanel.add(backButton);

        logoutButton = new JButton("Logout");
        logoutButton.setBackground(new Color(220, 20, 60)); // Crimson
        logoutButton.setForeground(Color.WHITE);
        logoutButton.addActionListener(this);
        buttonPanel.add(logoutButton);

        add(buttonPanel, BorderLayout.SOUTH);

        try {
            loadCases();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading cases: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            this.dispose();
        }

        setVisible(true);
    }

    private void loadCases() {
        Connection con = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/crime_system", "root", "garvit27");
            System.out.println("Database connection successful in MyCasesPage");

            PreparedStatement officerStmt = con.prepareStatement("SELECT officer_id FROM Officers WHERE user_id = ?");
            officerStmt.setInt(1, user.getUserId());
            ResultSet officerRs = officerStmt.executeQuery();
            if (!officerRs.next()) {
                casesArea.setText("No officer ID found for this user.");
                System.out.println("No officer_id found for user_id: " + user.getUserId());
                return;
            }
            int officerId = officerRs.getInt("officer_id");
            System.out.println("Officer ID: " + officerId);

            PreparedStatement stmt = con.prepareStatement(
                "SELECT cr.* FROM Crime_Reports cr " +
                "JOIN Crime_Assignment ca ON cr.report_id = ca.report_id " +
                "WHERE ca.officer_id = ?");
            stmt.setInt(1, officerId);
            ResultSet rs = stmt.executeQuery();

            List<CrimeReport> reports = new ArrayList<>();
            while (rs.next()) {
                reports.add(new CrimeReport(
                    rs.getInt("report_id"),
                    rs.getInt("citizen_id"),
                    rs.getString("crime_type"),
                    rs.getString("description"),
                    rs.getString("location"),
                    rs.getString("evidence_url"),
                    rs.getString("status"),
                    rs.getString("timestamp")
                ));
            }

            StringBuilder sb = new StringBuilder();
            if (reports.isEmpty()) {
                sb.append("No cases assigned to you yet.");
                System.out.println("No cases found for officer_id: " + officerId);
            } else {
                for (CrimeReport report : reports) {
                    sb.append("Report ID: ").append(report.getReportId())
                      .append(", Type: ").append(report.getCrimeType())
                      .append(", Status: ").append(report.getStatus())
                      .append("\n");
                }
                System.out.println("Cases loaded: " + reports.size());
            }
            casesArea.setText(sb.toString());

        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "MySQL JDBC Driver not found!", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == updateButton) {
            new ui.officer.CaseUpdatePage(user).setVisible(true);
        } else if (e.getSource() == backButton) {
            this.dispose();
        } else if (e.getSource() == logoutButton) {
            new ui.LoginPage().setVisible(true);
            this.dispose();
        }
    }
}