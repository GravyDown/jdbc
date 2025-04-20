package ui.officer;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import model.User;
import model.CrimeReport;
import ui.LoginPage;
import java.util.Date;

/**
 * Officer Dashboard displaying assigned cases with categorized views
 * Federal Bureau of Investigation - Crime Reporting System
 */
public class MyCasesPage extends JFrame implements ActionListener {
    // Constants
    private static final Color FBI_BLUE = new Color(0, 48, 143);
    private static final Color FBI_GOLD = new Color(206, 184, 136);
    private static final Color FBI_RED = new Color(186, 12, 47);
    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 16);
    private static final Font NORMAL_FONT = new Font("Arial", Font.PLAIN, 12);
    
    // UI Components
    private JTabbedPane tabbedPane;
    private JPanel mainPanel;
    private JPanel pendingCasesPanel;
    private JPanel closedCasesPanel;
    private JPanel newCasesPanel;
    private JLabel userInfoLabel;
    private JButton refreshButton, updateButton, detailsButton, backButton, logoutButton;
    private JLabel statusLabel;
    
    // Data
    private User user;
    private int officerId;
    
    public MyCasesPage(User user) {
        this.user = user;
        
        // Set up the frame
        setTitle("FBI Case Management - Agent " + user.getName());
        setSize(900, 700);
        setMinimumSize(new Dimension(800, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Create main panel with border layout
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(240, 240, 245));
        setContentPane(mainPanel);
        
        // Create header panel
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Create tabbed pane for case categories
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 14));
        
        // Create panels for each tab
        pendingCasesPanel = new JPanel();
        pendingCasesPanel.setLayout(new BoxLayout(pendingCasesPanel, BoxLayout.Y_AXIS));
        pendingCasesPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        closedCasesPanel = new JPanel();
        closedCasesPanel.setLayout(new BoxLayout(closedCasesPanel, BoxLayout.Y_AXIS));
        closedCasesPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        newCasesPanel = new JPanel();
        newCasesPanel.setLayout(new BoxLayout(newCasesPanel, BoxLayout.Y_AXIS));
        newCasesPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Add scroll panes to tabs
        tabbedPane.addTab("Pending Cases", new JScrollPane(pendingCasesPanel));
        tabbedPane.addTab("Recently Closed", new JScrollPane(closedCasesPanel));
        tabbedPane.addTab("Newly Assigned", new JScrollPane(newCasesPanel));
        
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        
        // Create button panel
        JPanel buttonPanel = createButtonPanel();
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Create status bar
        statusLabel = new JLabel("Ready");
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        mainPanel.add(statusLabel, BorderLayout.SOUTH);
        
        // Load data
        loadOfficerId();
        loadCases();
        
        setVisible(true);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(FBI_BLUE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Add FBI title
        JLabel titleLabel = new JLabel("Federal Bureau of Investigation - Case Management System");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        // Add user info on right
        userInfoLabel = new JLabel("Officer: " + user.getName());
        userInfoLabel.setFont(NORMAL_FONT);
        userInfoLabel.setForeground(Color.WHITE);
        
        // Add profile icon
        JPanel profilePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        profilePanel.setOpaque(false);
        
        // Create a simple profile icon
        JPanel iconPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.WHITE);
                g.fillOval(0, 0, 30, 30);
                g.setColor(FBI_BLUE);
                g.drawOval(0, 0, 30, 30);
            }
        };
        iconPanel.setPreferredSize(new Dimension(30, 30));
        iconPanel.setOpaque(false);
        
        profilePanel.add(userInfoLabel);
        profilePanel.add(iconPanel);
        headerPanel.add(profilePanel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        
        refreshButton = new JButton("Refresh Cases");
        refreshButton.addActionListener(this);
        
        updateButton = new JButton("Update Case");
        updateButton.addActionListener(this);
        
        detailsButton = new JButton("Case Details");
        detailsButton.addActionListener(this);
        
        backButton = new JButton("Back");
        backButton.addActionListener(this);
        
        logoutButton = new JButton("Logout");
        logoutButton.setBackground(FBI_RED);
        logoutButton.setForeground(Color.WHITE);
        logoutButton.addActionListener(this);
        
        buttonPanel.add(refreshButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(detailsButton);
        buttonPanel.add(backButton);
        buttonPanel.add(logoutButton);
        
        return buttonPanel;
    }
    
    private void loadOfficerId() {
        Connection con = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/cras", "root", "garvit27");
            
            PreparedStatement stmt = con.prepareStatement("SELECT officer_id FROM Officers WHERE user_id = ?");
            stmt.setInt(1, user.getUserId());
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                officerId = rs.getInt("officer_id");
                System.out.println("Found officer ID: " + officerId);
            } else {
                JOptionPane.showMessageDialog(this, "No officer record found for this user.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading officer data: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    private void loadCases() {
        statusLabel.setText("Loading cases...");
        
        // Clear existing panels
        pendingCasesPanel.removeAll();
        closedCasesPanel.removeAll();
        newCasesPanel.removeAll();
        
        Connection con = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/cras", "root", "garvit27");
            
            // Load pending cases
            loadPendingCases(con);
            
            // Load closed cases
            loadClosedCases(con);
            
            // Load new cases
            loadNewCases(con);
            
            statusLabel.setText("Cases loaded successfully.");
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading cases: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            statusLabel.setText("Error loading cases.");
        } finally {
            try {
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        // Update UI
        pendingCasesPanel.revalidate();
        pendingCasesPanel.repaint();
        closedCasesPanel.revalidate();
        closedCasesPanel.repaint();
        newCasesPanel.revalidate();
        newCasesPanel.repaint();
    }
    
    private void loadPendingCases(Connection con) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(
            "SELECT cr.* FROM incident_report cr " +
            "JOIN officer_assigned_cases ca ON cr.report_id = ca.report_id " +
            "WHERE ca.officer_id = ? AND cr.status NOT IN ('Closed', 'Resolved') " +
            "ORDER BY cr.timestamp DESC"
        );
        stmt.setInt(1, officerId);
        ResultSet rs = stmt.executeQuery();
        
        int count = 0;
        while (rs.next()) {
            CrimeReport report = new CrimeReport(
                rs.getInt("report_id"),
                rs.getInt("citizen_id"),
                rs.getString("crime_type"),
                rs.getString("description"),
                rs.getString("location"),
                rs.getString("evidence_url"),
                rs.getString("status"),
                rs.getString("timestamp")
            );
            
            JPanel casePanel = createCasePanel(report, FBI_BLUE);
            pendingCasesPanel.add(casePanel);
            pendingCasesPanel.add(Box.createVerticalStrut(10));
            count++;
        }
        
        if (count == 0) {
            JLabel emptyLabel = new JLabel("No pending cases assigned to you.");
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            pendingCasesPanel.add(emptyLabel);
        }
        
        // Update tab title with count
        tabbedPane.setTitleAt(0, "Pending Cases (" + count + ")");
    }
    
    private void loadClosedCases(Connection con) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(
            "SELECT cr.* FROM incident_report cr " +
            "JOIN officer_assigned_cases ca ON cr.report_id = ca.report_id " +
            "WHERE ca.officer_id = ? AND cr.status IN ('Closed', 'Resolved') " +
            "ORDER BY cr.timestamp DESC LIMIT 20"
        );
        stmt.setInt(1, officerId);
        ResultSet rs = stmt.executeQuery();
        
        int count = 0;
        while (rs.next()) {
            CrimeReport report = new CrimeReport(
                rs.getInt("report_id"),
                rs.getInt("citizen_id"),
                rs.getString("crime_type"),
                rs.getString("description"),
                rs.getString("location"),
                rs.getString("evidence_url"),
                rs.getString("status"),
                rs.getString("timestamp")
            );
            
            JPanel casePanel = createCasePanel(report, FBI_GOLD);
            closedCasesPanel.add(casePanel);
            closedCasesPanel.add(Box.createVerticalStrut(10));
            count++;
        }
        
        if (count == 0) {
            JLabel emptyLabel = new JLabel("No recently closed cases.");
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            closedCasesPanel.add(emptyLabel);
        }
        
        // Update tab title with count
        tabbedPane.setTitleAt(1, "Recently Closed (" + count + ")");
    }
    
    private void loadNewCases(Connection con) throws SQLException {
        // For demonstration, we'll consider cases from the last 7 days as "new"
        PreparedStatement stmt = con.prepareStatement(
            "SELECT cr.* FROM incident_report cr " +
            "JOIN officer_assigned_cases ca ON cr.report_id = ca.report_id " +
            "WHERE ca.officer_id = ? " +
            "ORDER BY cr.timestamp DESC LIMIT 10"
        );
        stmt.setInt(1, officerId);
        ResultSet rs = stmt.executeQuery();
        
        int count = 0;
        while (rs.next()) {
            CrimeReport report = new CrimeReport(
                rs.getInt("report_id"),
                rs.getInt("citizen_id"),
                rs.getString("crime_type"),
                rs.getString("description"),
                rs.getString("location"),
                rs.getString("evidence_url"),
                rs.getString("status"),
                rs.getString("timestamp")
            );
            
            JPanel casePanel = createCasePanel(report, FBI_RED);
            newCasesPanel.add(casePanel);
            newCasesPanel.add(Box.createVerticalStrut(10));
            count++;
        }
        
        if (count == 0) {
            JLabel emptyLabel = new JLabel("No newly assigned cases.");
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            newCasesPanel.add(emptyLabel);
        }
        
        // Update tab title with count
        tabbedPane.setTitleAt(2, "Newly Assigned (" + count + ")");
    }
    
    private JPanel createCasePanel(CrimeReport report, Color accentColor) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(accentColor, 2),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        // Header with case ID and status
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        
        JLabel idLabel = new JLabel("Case #" + report.getReportId());
        idLabel.setFont(new Font("Arial", Font.BOLD, 14));
        idLabel.setForeground(accentColor);
        headerPanel.add(idLabel, BorderLayout.WEST);
        
        JLabel statusLabel = new JLabel(report.getStatus());
        statusLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        headerPanel.add(statusLabel, BorderLayout.EAST);
        
        panel.add(headerPanel, BorderLayout.NORTH);
        
        // Content panel with case details
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        
        JLabel typeLabel = new JLabel("Crime Type: " + report.getCrimeType());
        typeLabel.setFont(NORMAL_FONT);
        contentPanel.add(typeLabel);
        contentPanel.add(Box.createVerticalStrut(5));
        
        JLabel descLabel = new JLabel("<html>Description: " + 
            (report.getDescription().length() > 100 ? 
                report.getDescription().substring(0, 100) + "..." : 
                report.getDescription()) + 
            "</html>");
        descLabel.setFont(NORMAL_FONT);
        contentPanel.add(descLabel);
        contentPanel.add(Box.createVerticalStrut(5));
        
        JLabel locationLabel = new JLabel("Location: " + report.getLocation());
        locationLabel.setFont(NORMAL_FONT);
        contentPanel.add(locationLabel);
        
        panel.add(contentPanel, BorderLayout.CENTER);
        
        // Footer with timestamp and view button
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setOpaque(false);
        
        SimpleDateFormat displayFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm");
        SimpleDateFormat parseFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = report.getTimestamp();
        
        try {
            Date date = parseFormat.parse(report.getTimestamp());
            formattedDate = displayFormat.format(date);
        } catch (Exception e) {
            // Use original timestamp if parsing fails
        }
        
        JLabel timeLabel = new JLabel("Reported: " + formattedDate);
        timeLabel.setFont(new Font("Arial", Font.ITALIC, 11));
        footerPanel.add(timeLabel, BorderLayout.WEST);
        
        JButton viewButton = new JButton("View Details");
        viewButton.setFont(new Font("Arial", Font.PLAIN, 10));
        viewButton.addActionListener(e -> viewCaseDetails(report.getReportId()));
        footerPanel.add(viewButton, BorderLayout.EAST);
        
        panel.add(footerPanel, BorderLayout.SOUTH);
        
        // Make the panel interactive
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                panel.setBackground(new Color(245, 245, 250));
                panel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                panel.setBackground(Color.WHITE);
                panel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
            
            @Override
            public void mouseClicked(MouseEvent e) {
                viewCaseDetails(report.getReportId());
            }
        });
        
        return panel;
    }
    
    private void viewCaseDetails(int reportId) {
        JOptionPane.showMessageDialog(this, 
            "Opening case details for Report #" + reportId + "\n" +
            "This feature will be implemented in the CaseUpdatePage.",
            "Case Details",
            JOptionPane.INFORMATION_MESSAGE);
        
        new CaseUpdatePage(user);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == refreshButton) {
            loadCases();
        } else if (e.getSource() == updateButton) {
            // Get selected tab to determine which type of case to update
            int tabIndex = tabbedPane.getSelectedIndex();
            if (tabIndex == 0) {
                // Pending cases
                JOptionPane.showMessageDialog(this, "Please select a pending case to update.");
            } else if (tabIndex == 1) {
                // Closed cases
                JOptionPane.showMessageDialog(this, "Closed cases cannot be updated.");
            } else {
                // New cases
                JOptionPane.showMessageDialog(this, "Please select a new case to update.");
            }
            new CaseUpdatePage(user);
        } else if (e.getSource() == detailsButton) {
            // Get selected tab
            int tabIndex = tabbedPane.getSelectedIndex();
            if (tabIndex == 0) {
                // Pending cases
                JOptionPane.showMessageDialog(this, "Please select a pending case to view details.");
            } else if (tabIndex == 1) {
                // Closed cases
                JOptionPane.showMessageDialog(this, "Please select a closed case to view details.");
            } else {
                // New cases
                JOptionPane.showMessageDialog(this, "Please select a new case to view details.");
            }
        } else if (e.getSource() == backButton) {
            // Go back to previous screen
            this.dispose();
        } else if (e.getSource() == logoutButton) {
            // Logout
            int choice = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to log out?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
                
            if (choice == JOptionPane.YES_OPTION) {
                new ui.LoginPage().setVisible(true);
                this.dispose();
            }
        }
    }
    
    // For testing
    public static void main(String[] args) {
        User testUser = new User(1, "Test Officer", "officer", "password");
        new MyCasesPage(testUser);
    }
}