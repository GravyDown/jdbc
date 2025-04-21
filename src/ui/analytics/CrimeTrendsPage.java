package ui.analytics;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import util.DatabaseConnection;

public class CrimeTrendsPage extends JFrame implements ActionListener {
    private JTextArea trendsArea;
    private JButton backButton;

    public CrimeTrendsPage() {
        setTitle("Crime Trends");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        trendsArea = new JTextArea();
        trendsArea.setEditable(false);
        add(new JScrollPane(trendsArea), BorderLayout.CENTER);

        backButton = new JButton("Back");
        backButton.addActionListener(this);
        add(backButton, BorderLayout.SOUTH);

        loadTrends();
    }

    private void loadTrends() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT crime_category, COUNT(*) as count FROM incident_report GROUP BY crime_category")) {
            StringBuilder sb = new StringBuilder();
            while (rs.next()) {
                sb.append("Crime Type: ").append(rs.getString("crime_category"))
                  .append(", Count: ").append(rs.getInt("count"))
                  .append("\n");
            }
            trendsArea.setText(sb.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.dispose();
    }
}