package ui.analytics;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import util.DatabaseConnection;

public class CrimeHeatmapsPage extends JFrame implements ActionListener {
    private JTextArea heatmapArea;
    private JButton backButton;

    public CrimeHeatmapsPage() {
        setTitle("Crime Heatmaps");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        heatmapArea = new JTextArea();
        heatmapArea.setEditable(false);
        add(new JScrollPane(heatmapArea), BorderLayout.CENTER);

        backButton = new JButton("Back");
        backButton.addActionListener(this);
        add(backButton, BorderLayout.SOUTH);

        loadHeatmap();
    }

    private void loadHeatmap() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT location, COUNT(*) as count FROM Crime_Reports GROUP BY location")) {
            StringBuilder sb = new StringBuilder();
            while (rs.next()) {
                sb.append("Location: ").append(rs.getString("location"))
                  .append(", Count: ").append(rs.getInt("count"))
                  .append("\n");
            }
            heatmapArea.setText(sb.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.dispose();
    }
}