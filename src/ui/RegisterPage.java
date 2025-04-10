package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import util.PasswordHasher;
import java.sql.*;
import java.util.regex.Pattern;

public class RegisterPage extends JFrame implements ActionListener {
    private JTextField nameField, emailField, contactField;
    private JPasswordField passwordField;
    private JComboBox<String> roleCombo;
    private JButton registerButton, loginButton;

    public RegisterPage() {
        setTitle("Register - Crime Reporting System");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(240, 248, 255));

        JLabel header = new JLabel("Create Your Account", SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 24));
        header.setForeground(new Color(25, 25, 112));
        add(header, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        formPanel.setBackground(new Color(240, 248, 255));

        formPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        nameField.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(nameField);

        formPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        emailField.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(emailField);

        formPanel.add(new JLabel("Contact:"));
        contactField = new JTextField();
        contactField.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(contactField);

        formPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(passwordField);

        formPanel.add(new JLabel("Role:"));
        roleCombo = new JComboBox<>(new String[]{"CITIZEN", "ADMIN", "OFFICER"});
        roleCombo.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(roleCombo);

        add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(240, 248, 255));

        registerButton = new JButton("Register");
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.setBackground(new Color(60, 179, 113));
        registerButton.setForeground(Color.WHITE);
        registerButton.addActionListener(this);
        buttonPanel.add(registerButton);

        loginButton = new JButton("Go to Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setBackground(new Color(135, 206, 235));
        loginButton.setForeground(Color.WHITE);
        loginButton.addActionListener(this);
        buttonPanel.add(loginButton);

        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return Pattern.matches(emailRegex, email);
    }

    private boolean isValidContact(String contact) {
        String contactRegex = "^[0-9]{10}$";
        return Pattern.matches(contactRegex, contact);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == registerButton) {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String contact = contactField.getText().trim();
            String password = new String(passwordField.getPassword());
            String role = (String) roleCombo.getSelectedItem();

            if (name.isEmpty() || email.isEmpty() || contact.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!isValidEmail(email)) {
                JOptionPane.showMessageDialog(this, "Invalid email format!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!isValidContact(contact)) {
                JOptionPane.showMessageDialog(this, "Contact must be a 10-digit number!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Connection con = null;
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                con = DriverManager.getConnection("jdbc:mysql://localhost:3306/crime_system", "root", "garvit27");

                // Check if email exists
                PreparedStatement checkStmt = con.prepareStatement("SELECT COUNT(*) FROM User WHERE user_email = ?");
                checkStmt.setString(1, email);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    JOptionPane.showMessageDialog(this, "Email already exists!", "Error", JOptionPane.ERROR_MESSAGE);
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
                                PreparedStatement citizenStmt = con.prepareStatement("INSERT INTO Citizens (user_id) VALUES (?)");
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
                                PreparedStatement officerStmt = con.prepareStatement("INSERT INTO Officers (user_id, department) VALUES (?, 'General')");
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
                    JOptionPane.showMessageDialog(this, "Registration failed!", "Error", JOptionPane.ERROR_MESSAGE);
                }
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
        } else if (e.getSource() == loginButton) {
            new LoginPage().setVisible(true);
            this.dispose();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RegisterPage::new);
    }
}