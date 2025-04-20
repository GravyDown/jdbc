package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import model.User;
import util.PasswordHasher;
import ui.citizens.CrimeReportPage;
import ui.admin.AdminDashboard;
import java.sql.*;


public class LoginPage extends JFrame implements ActionListener {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton, registerButton;

    public LoginPage() {
        setTitle("Login - Crime Reporting System");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(240, 248, 255));

        JLabel header = new JLabel("Welcome Back", SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 24));
        header.setForeground(new Color(25, 25, 112));
        add(header, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        formPanel.setBackground(new Color(240, 248, 255));

        formPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        emailField.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(emailField);

        formPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(passwordField);

        add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(240, 248, 255));

        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setBackground(new Color(60, 179, 113));
        loginButton.setForeground(Color.WHITE);
        loginButton.addActionListener(this);
        buttonPanel.add(loginButton);

        registerButton = new JButton("Register");
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.setBackground(new Color(135, 206, 235));
        registerButton.setForeground(Color.WHITE);
        registerButton.addActionListener(this);
        buttonPanel.add(registerButton);

        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Email and password are required!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Connection con = null;
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                con = DriverManager.getConnection("jdbc:mysql://localhost:3306/cras", "root", "garvit27");

                PreparedStatement stmt = con.prepareStatement(
                    "SELECT * FROM User WHERE user_email = ? AND User_password = ?");
                stmt.setString(1, email);
                stmt.setString(2, PasswordHasher.hash(password));
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    User user = new User(rs.getInt("user_id"), rs.getString("user_name"), email, rs.getString("role"));
                    dispose();
                    switch (user.getRole()) {
                        case "CITIZEN":
                            new ui.citizens.CrimeReportPage(user);
                            break;
                        case "ADMIN":
                            new ui.admin.AdminDashboard(user);
                            break;
                        case "OFFICER":
                            //Officer officer = OfficerDAO.getOfficerByUserId(user.getUserId()); // use the ID from the 'user' object
                            new ui.officer.MyCasesPage(user);
                            break;
                        default:
                            JOptionPane.showMessageDialog(this, "Unknown role!");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid credentials!", "Error", JOptionPane.ERROR_MESSAGE);
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
        } else if (e.getSource() == registerButton) {
            new RegisterPage().setVisible(true);
            this.dispose();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginPage::new);
    }
}