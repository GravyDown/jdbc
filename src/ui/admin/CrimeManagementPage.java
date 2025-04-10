package ui.admin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import model.User;
import model.CrimeReport;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CrimeManagementPage extends JFrame implements ActionListener {
    private JTextArea reportsArea;
    private JButton backButton;
    private User user;

    public CrimeManagementPage(User user) {
        this.user = user;
        setTitle("Crime Management");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        reportsArea = new JTextArea();
        reportsArea.setEditable(false);
        add(new JScrollPane(reportsArea), BorderLayout.CENTER);

        backButton = new JButton("Back");
        backButton.addActionListener(this);
        add(backButton, BorderLayout.SOUTH);

        loadReports();
    }

    private void loadReports() {
        Connection con = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/crime_system", "root", "garvit27");

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Crime_Reports");

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
            for (CrimeReport report : reports) {
                sb.append("Report ID: ").append(report.getReportId())
                  .append(", Type: ").append(report.getCrimeType())
                  .append(", Status: ").append(report.getStatus())
                  .append(", Citizen ID: ").append(report.getCitizenId())
                  .append("\n");
            }
            reportsArea.setText(sb.toString());

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
        new ui.admin.AdminDashboard(user).setVisible(true);
        this.dispose();
    }
}