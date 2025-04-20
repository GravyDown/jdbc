package ui.officer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import model.User;
import model.CrimeReport;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import ui.LoginPage;

public class OfficerLanding extends JFrame {
    private Officer currentOfficer;
    private JPanel mainPanel;
    private JLabel welcomeLabel, regionLabel, caseCountLabel;
    private JButton myCasesBtn, newCasesBtn, updateProfileBtn, logoutBtn;
    private JTable recentActivityTable;
    
    public OfficerLanding(Officer officer) {
        this.currentOfficer = officer;
        setTitle("Officer Dashboard - Case Management System");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        initComponents();
        loadOfficerData();
        loadRecentActivity();
        
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    private void initComponents() {
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Header panel with officer info
        JPanel headerPanel = new JPanel(new BorderLayout());
        
        JPanel officerInfoPanel = new JPanel(new GridLayout(3, 1));
        welcomeLabel = new JLabel("Welcome, Officer");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        regionLabel = new JLabel("Region: ");
        caseCountLabel = new JLabel("Pending Cases: ");
        
        officerInfoPanel.add(welcomeLabel);
        officerInfoPanel.add(regionLabel);
        officerInfoPanel.add(caseCountLabel);
        
        // Quick stats panel
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        statsPanel.add(createStatPanel("Total Cases", "0"));
        statsPanel.add(createStatPanel("Pending", "0"));
        statsPanel.add(createStatPanel("Completed", "0"));
        
        headerPanel.add(officerInfoPanel, BorderLayout.WEST);
        headerPanel.add(statsPanel, BorderLayout.EAST);
        
        // Action buttons panel
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        myCasesBtn = new JButton("My Cases");
        newCasesBtn = new JButton("New Cases");
        updateProfileBtn = new JButton("Update Profile");
        logoutBtn = new JButton("Logout");
        
        myCasesBtn.addActionListener(e -> openMyCases());
        newCasesBtn.addActionListener(e -> checkNewCases());
        updateProfileBtn.addActionListener(e -> updateProfile());
        logoutBtn.addActionListener(e -> logout());
        
        actionPanel.add(myCasesBtn);
        actionPanel.add(newCasesBtn);
        actionPanel.add(updateProfileBtn);
        actionPanel.add(logoutBtn);
        
        // Recent activity panel
        JPanel activityPanel = new JPanel(new BorderLayout());
        activityPanel.setBorder(BorderFactory.createTitledBorder("Recent Activity"));
        
        String[] columns = {"Date", "Case ID", "Activity", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        recentActivityTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(recentActivityTable);
        activityPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Regional overview panel
        JPanel regionOverviewPanel = new JPanel(new BorderLayout());
        regionOverviewPanel.setBorder(BorderFactory.createTitledBorder("Regional Overview"));
        
        // We'll add a chart or table here in a real implementation
        JPanel chartPanel = new JPanel();
        chartPanel.setPreferredSize(new Dimension(300, 200));
        chartPanel.setBackground(Color.LIGHT_GRAY);
        chartPanel.add(new JLabel("Regional Case Statistics Chart"));
        
        regionOverviewPanel.add(chartPanel, BorderLayout.CENTER);
        
        // Add all panels to main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(actionPanel, BorderLayout.CENTER);
        
        JPanel bottomPanel = new JPanel(new GridLayout(2, 1, 0, 10));
        bottomPanel.add(activityPanel);
        bottomPanel.add(regionOverviewPanel);
        
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private JPanel createStatPanel(String title, String value) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEtchedBorder());
        panel.setBackground(new Color(240, 240, 240));
        
        JLabel titleLabel = new JLabel(title, JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        
        JLabel valueLabel = new JLabel(value, JLabel.CENTER);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 18));
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(valueLabel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void loadOfficerData() {
        // In a real app, this would fetch data from the database
        welcomeLabel.setText("Welcome, Officer " + currentOfficer.getName());
        regionLabel.setText("Region: " + getRegionName(currentOfficer.getRegionId()));
        caseCountLabel.setText("Pending Cases: " + currentOfficer.getPendingCases());
        
        // Update stats panels with real data
        // This would be implemented with actual database queries
    }
    
    private String getRegionName(int regionId) {
        // This would be a database query in the real implementation
        switch(regionId) {
            case 1: return "North District";
            case 2: return "South District";
            case 3: return "East District";
            case 4: return "West District";
            case 5: return "Central District";
            default: return "Unknown Region";
        }
    }
    
    private void loadRecentActivity() {
        // This would load recent activity from the database
        DefaultTableModel model = (DefaultTableModel) recentActivityTable.getModel();
        model.setRowCount(0); // Clear existing rows
        
        // Sample data - would be replaced with database query results
        model.addRow(new Object[]{"2025-04-17", "CS-2025-0042", "Status Updated", "In Progress"});
        model.addRow(new Object[]{"2025-04-16", "CS-2025-0039", "New Comment", "Under Investigation"});
        model.addRow(new Object[]{"2025-04-15", "CS-2025-0035", "Case Closed", "Resolved"});
        model.addRow(new Object[]{"2025-04-15", "CS-2025-0041", "Case Assigned", "New"});
    }
    
    private void openMyCases() {
        // TODO: Fix type mismatch by converting Officer to User or adjust MyCasesPage to accept Officer
        // For now, just dispose this frame
        this.dispose();
    }
    
    private void checkNewCases() {
        // Placeholder method - to be implemented
        JOptionPane.showMessageDialog(this, "Checking for new cases in your region...");
    }
    
    private void updateProfile() {
        // Placeholder method - to be implemented
        JOptionPane.showMessageDialog(this, "Profile update functionality to be implemented");
    }
    
    private void logout() {
        // Log out and return to login screen
        new LoginPage().setVisible(true);
        this.dispose();
    }
    
    // Officer class (simplified)
    public static class Officer {
        private int id;
        private String name;
        private int regionId;
        private int pendingCases;
        
        // Getters and setters
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public int getRegionId() { return regionId; }
        public void setRegionId(int regionId) { this.regionId = regionId; }
        public int getPendingCases() { return pendingCases; }
        public void setPendingCases(int pendingCases) { this.pendingCases = pendingCases; }
    }
}