package ui.citizens;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import model.User;
import model.CrimeReport;
import java.sql.*;
import java.util.ArrayList;

public class MyReportsPage extends JFrame implements ActionListener {
    private JTextArea reportsArea;
    private JButton backButton;
    private User user;

    public MyReportsPage(User user) {
        this.user = user;
        setTitle("My Reports");
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

            PreparedStatement stmt = con.prepareStatement(
                "SELECT * FROM Crime_Reports WHERE citizen_id = (SELECT citizen_id FROM Citizens WHERE user_id = ?)");
            stmt.setInt(1, user.getUserId());
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
            for (CrimeReport report : reports) {
                sb.append("Report ID: ").append(report.getReportId())
                  .append(", Type: ").append(report.getCrimeType())
                  .append(", Status: ").append(report.getStatus())
                  .append(", Timestamp: ").append(report.getTimestamp())
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
        new ui.citizens.CrimeReportPage(user).setVisible(true);
        this.dispose();
    }
}