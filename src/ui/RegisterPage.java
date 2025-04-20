package ui;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.text.ParseException;
import util.PasswordHasher;
import java.sql.*;
import java.util.regex.Pattern;

public class RegisterPage extends JFrame {
    private JTextField nameField, emailField;
    private JFormattedTextField contactField;
    private JPasswordField passwordField, confirmPasswordField;
    private JComboBox<String> roleCombo;
    private JButton registerButton, backButton;
    private JPanel headerPanel;
    
    // Color scheme
    private Color primaryColor = new Color(10, 49, 97);
    private Color accentColor = new Color(190, 30, 45);
    private Color lightBgColor = new Color(240, 248, 255);
    private Color fieldBgColor = new Color(255, 255, 255);
    private Color textColor = new Color(50, 50, 50);

    public RegisterPage() {
        setTitle("Register - Crime Reporting System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(lightBgColor);
        
        createHeaderPanel();
        createMainPanel();
        createFooterPanel();
        
        setVisible(true);
    }
    
    private void createHeaderPanel() {
        headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBackground(primaryColor);
        headerPanel.setPreferredSize(new Dimension(getWidth(), 80));
        
        // Logo and title
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        leftPanel.setBackground(primaryColor);
        
        // Create a shield-like logo
        ImageIcon shieldIcon = createShieldIcon(40, 40);
        JLabel logoLabel = new JLabel(shieldIcon);
        
        JLabel titleLabel = new JLabel("NATIONAL CRIME REPORTING SYSTEM");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        
        leftPanel.add(logoLabel);
        leftPanel.add(titleLabel);
        
        headerPanel.add(leftPanel, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);
    }
    
    private void createMainPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(lightBgColor);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        
        // Registration form title
        JLabel formTitle = new JLabel("Create Your Account");
        formTitle.setFont(new Font("Arial", Font.BOLD, 24));
        formTitle.setForeground(primaryColor);
        formTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel formSubtitle = new JLabel("Please fill in the information below to register");
        formSubtitle.setFont(new Font("Arial", Font.PLAIN, 14));
        formSubtitle.setForeground(textColor);
        formSubtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Registration form
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(25, 30, 25, 30)
        ));
        formPanel.setMaximumSize(new Dimension(600, 400));
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 5, 8, 5);
        
        // Name field
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel nameLabel = new JLabel("Full Name:");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        nameLabel.setForeground(textColor);
        formPanel.add(nameLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        nameField = createStyledTextField();
        formPanel.add(nameField, gbc);
        
        // Email field
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        JLabel emailLabel = new JLabel("Email Address:");
        emailLabel.setFont(new Font("Arial", Font.BOLD, 14));
        emailLabel.setForeground(textColor);
        formPanel.add(emailLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        emailField = createStyledTextField();
        formPanel.add(emailField, gbc);
        
        // Contact field
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        JLabel contactLabel = new JLabel("Contact Number:");
        contactLabel.setFont(new Font("Arial", Font.BOLD, 14));
        contactLabel.setForeground(textColor);
        formPanel.add(contactLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        try {
            MaskFormatter phoneFormatter = new MaskFormatter("##########");
            contactField = new JFormattedTextField(phoneFormatter);
            styleField(contactField);
            contactField.setPreferredSize(new Dimension(400, 35));
        } catch (ParseException e) {
            contactField = new JFormattedTextField();
            styleField(contactField);
            contactField.setPreferredSize(new Dimension(400, 35));
        }
        formPanel.add(contactField, gbc);
        
        // Password field
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 14));
        passwordLabel.setForeground(textColor);
        formPanel.add(passwordLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        passwordField = new JPasswordField();
        styleField(passwordField);
        formPanel.add(passwordField, gbc);
        
        // Confirm Password field
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        confirmPasswordLabel.setFont(new Font("Arial", Font.BOLD, 14));
        confirmPasswordLabel.setForeground(textColor);
        formPanel.add(confirmPasswordLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        confirmPasswordField = new JPasswordField();
        styleField(confirmPasswordField);
        formPanel.add(confirmPasswordField, gbc);
        
        // Role dropdown
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        JLabel roleLabel = new JLabel("Register as:");
        roleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        roleLabel.setForeground(textColor);
        formPanel.add(roleLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        roleCombo = new JComboBox<>(new String[]{"CITIZEN", "ADMIN", "OFFICER"});
        roleCombo.setFont(new Font("Arial", Font.PLAIN, 14));
        styleField(roleCombo);
        formPanel.add(roleCombo, gbc);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        registerButton = new JButton("REGISTER");
        registerButton.setFont(new Font("Arial", Font.BOLD, 16));
        registerButton.setBackground(accentColor);
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        registerButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(160, 20, 35), 1),
            BorderFactory.createEmptyBorder(14, 45, 14, 45)
        ));
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerButton.setOpaque(true);
        registerButton.setContentAreaFilled(true);
        registerButton.setBorderPainted(true);
        
        backButton = new JButton("BACK TO LOGIN");
        backButton.setFont(new Font("Arial", Font.BOLD, 16));
        backButton.setBackground(Color.WHITE);
        backButton.setForeground(primaryColor);
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(primaryColor, 1),
            BorderFactory.createEmptyBorder(11, 30, 11, 30)
        ));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        buttonPanel.add(registerButton);
        buttonPanel.add(backButton);
        
        // Add hover effects
        registerButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                registerButton.setBackground(new Color(220, 40, 55));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                registerButton.setBackground(accentColor);
            }
        });
        
        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                backButton.setBackground(new Color(240, 240, 240));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                backButton.setBackground(Color.WHITE);
            }
        });
        
        // Add components to main panel
        mainPanel.add(formTitle);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        mainPanel.add(formSubtitle);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        mainPanel.add(formPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(buttonPanel);
        
        add(mainPanel, BorderLayout.CENTER);
        
        // Add action listeners
        registerButton.addActionListener(e -> handleRegistration());
        
        backButton.addActionListener(e -> {
            new LoginPage().setVisible(true);
            this.dispose();
        });
    }
    
    private void createFooterPanel() {
        JPanel footerPanel = new JPanel();
        footerPanel.setLayout(new BorderLayout());
        footerPanel.setBackground(new Color(240, 240, 240));
        footerPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        
        JLabel copyrightLabel = new JLabel("© 2025 National Crime Reporting System. All rights reserved.");
        copyrightLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        copyrightLabel.setForeground(Color.DARK_GRAY);
        
        footerPanel.add(copyrightLabel, BorderLayout.WEST);
        
        add(footerPanel, BorderLayout.SOUTH);
    }
    
    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        styleField(field);
        return field;
    }
    
    private void styleField(JComponent field) {
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBackground(fieldBgColor);
        field.setPreferredSize(new Dimension(300, 40)); // Increased size
    
        if (field instanceof JTextField || field instanceof JPasswordField) {
            ((JTextField) field).setBorder(new CompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                BorderFactory.createEmptyBorder(6, 12, 6, 12)
            ));
        } else if (field instanceof JComboBox) {
            field.setBorder(new CompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                BorderFactory.createEmptyBorder(6, 6, 6, 6)
            ));
        }
    }
    
    
    private ImageIcon createShieldIcon(int width, int height) {
        // Create a shield shaped icon programmatically
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        
        // Enable anti-aliasing
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw shield shape
        int[] xPoints = {width/2, width, width*3/4, width/2, width/4, 0};
        int[] yPoints = {0, height/4, height, height, height/4, height/4};
        g2d.setColor(accentColor);
        g2d.fillPolygon(xPoints, yPoints, 6);
        
        // Add emblem
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 18));
        FontMetrics fm = g2d.getFontMetrics();
        String text = "CRS";
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getHeight();
        g2d.drawString(text, (width - textWidth) / 2, height / 2 + textHeight / 4);
        
        g2d.dispose();
        return new ImageIcon(image);
    }
    
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return Pattern.matches(emailRegex, email);
    }

    private boolean isValidContact(String contact) {
        String contactRegex = "^[0-9]{10}$";
        return Pattern.matches(contactRegex, contact);
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    private void handleRegistration() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String contact = contactField.getText().trim().replaceAll("[^0-9]", ""); // Remove non-digit characters
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        String role = (String) roleCombo.getSelectedItem();

        // Validate input fields
        if (name.isEmpty() || email.isEmpty() || contact.isEmpty() || password.isEmpty()) {
            showError("All fields are required!");
            return;
        }

        if (!isValidEmail(email)) {
            showError("Invalid email format!");
            return;
        }

        if (!isValidContact(contact)) {
            showError("Contact must be a 10-digit number!");
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            showError("Passwords do not match!");
            return;
        }
        
        if (password.length() < 8) {
            showError("Password must be at least 8 characters long!");
            return;
        }

        // Database connection
        Connection con = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/cras", "root", "garvit27");

            // Check if email exists
            PreparedStatement checkStmt = con.prepareStatement("SELECT COUNT(*) FROM User WHERE user_email = ?");
            checkStmt.setString(1, email);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                showError("Email already exists!");
                return;
            }

            // Insert user
            PreparedStatement stmt = con.prepareStatement(
                "INSERT INTO User (user_name, user_email, User_contact, User_password, role) VALUES (?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, contact);
            stmt.setString(4, PasswordHasher.hash(password));
            stmt.setString(5, role);
            int rows = stmt.executeUpdate();

            if (rows > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int userId = generatedKeys.getInt(1);
                    switch (role) {
                        case "CITIZEN":
                            PreparedStatement citizenStmt = con.prepareStatement("INSERT INTO Citizen (user_id) VALUES (?)");
                            citizenStmt.setInt(1, userId);
                            citizenStmt.executeUpdate();
                            citizenStmt.close();
                            break;
                        case "ADMIN":
                            PreparedStatement adminStmt = con.prepareStatement("INSERT INTO Admin (user_id) VALUES (?)");
                            adminStmt.setInt(1, userId);
                            adminStmt.executeUpdate();
                            adminStmt.close();
                            break;
                        case "OFFICER":
                            PreparedStatement officerStmt = con.prepareStatement("INSERT INTO Officer (user_id, officer_region) VALUES (?, 'Central')");
                            officerStmt.setInt(1, userId);
                            officerStmt.executeUpdate();
                            officerStmt.close();
                            break;
                    }
                    JOptionPane.showMessageDialog(this, "Registration Successful! Please log in.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    new LoginPage().setVisible(true);
                    this.dispose();
                }
            } else {
                showError("Registration failed!");
            }
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            showError("MySQL JDBC Driver not found!");
        } catch (SQLException ex) {
            ex.printStackTrace();
            showError("Database error: " + ex.getMessage());
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
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            new RegisterPage().setVisible(true);
        });
    }
}