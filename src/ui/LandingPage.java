package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.net.URL;
import java.awt.image.BufferedImage;
import javax.swing.border.Border;
import java.awt.Insets;

class RoundedBorder implements Border {
    private int radius;
    private Color color;
    
    RoundedBorder(int radius, Color color) {
        this.radius = radius;
        this.color = color;
    }
    
    public Insets getBorderInsets(Component c) {
        return new Insets(this.radius, this.radius, this.radius, this.radius);
    }
    
    public boolean isBorderOpaque() {
        return true;
    }
    
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(color);
        g2.drawRoundRect(x, y, width-1, height-1, radius, radius);
    }
}

public class LandingPage extends JFrame {
    private JPanel headerPanel, mainPanel, footerPanel;
    private JButton loginButton, registerButton, reportCrimeButton;
    private JLabel logoLabel, titleLabel, subtitleLabel;
    private Color primaryColor = new Color(10, 49, 97);
    private Color accentColor = new Color(190, 30, 45);
    private Color lightBgColor = new Color(240, 248, 255);
    
    public LandingPage() {
        setTitle("Crime Reporting System");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);
        
        createHeaderPanel();
        createMainPanel();
        createFooterPanel();
        
        add(headerPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);
    }
    
    private void createHeaderPanel() {
        headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBackground(primaryColor);
        headerPanel.setPreferredSize(new Dimension(getWidth(), 80));
        
        // Logo and title
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        leftPanel.setBackground(primaryColor);
        
        // Create a shield-like logo
        ImageIcon shieldIcon = createShieldIcon(50, 50);
        logoLabel = new JLabel(shieldIcon);
        
        titleLabel = new JLabel("NATIONAL CRIME REPORTING SYSTEM");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        
        leftPanel.add(logoLabel);
        leftPanel.add(titleLabel);
        
        // Navigation buttons
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 20));
        rightPanel.setBackground(primaryColor);
        
        loginButton = createHeaderButton("LOGIN");
        registerButton = createHeaderButton("REGISTER");
        
        rightPanel.add(loginButton);
        rightPanel.add(registerButton);
        
        headerPanel.add(leftPanel, BorderLayout.WEST);
        headerPanel.add(rightPanel, BorderLayout.EAST);
        
        // Add event listeners
        loginButton.addActionListener(e -> {
            new LoginPage().setVisible(true);
            this.dispose();
        });
        
        registerButton.addActionListener(e -> {
            new RegisterPage().setVisible(true);
            this.dispose();
        });
    }
    
    private JButton createHeaderButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.white);
        button.setBackground(new Color(37,99,235));

        button.setContentAreaFilled(false);
        button.setOpaque(true);

        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.white, 0),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(29, 78, 216));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(37, 99, 235));
            }
        });
        
        return button;
    }
    
    private void createMainPanel() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        
        // Hero section
        JPanel heroPanel = new JPanel();
        heroPanel.setLayout(new BoxLayout(heroPanel, BoxLayout.Y_AXIS));
        heroPanel.setBackground(lightBgColor);
        heroPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        
        // Hero image panel with dark overlay for text readability
        JPanel imagePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Draw background image (would normally be loaded from a file)
                g.setColor(new Color(0, 0, 50));
                g.fillRect(0, 0, getWidth(), getHeight());
                
                // Draw a subtle grid pattern
                g.setColor(new Color(30, 30, 80));
                for (int i = 0; i < getWidth(); i += 20) {
                    g.drawLine(i, 0, i, getHeight());
                }
                for (int i = 0; i < getHeight(); i += 20) {
                    g.drawLine(0, i, getWidth(), i);
                }
            }
        };
        imagePanel.setLayout(new BoxLayout(imagePanel, BoxLayout.Y_AXIS));
        imagePanel.setPreferredSize(new Dimension(getWidth(), 300));
        imagePanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        
        JLabel mainTitle = new JLabel("Reporting Crime. Serving Justice.");
        mainTitle.setFont(new Font("Arial", Font.BOLD, 32));
        mainTitle.setForeground(Color.WHITE);
        mainTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel mainSubtitle = new JLabel("A secure platform for citizens to report incidents and collaborate with law enforcement");
        mainSubtitle.setFont(new Font("Arial", Font.PLAIN, 16));
        mainSubtitle.setForeground(Color.WHITE);
        mainSubtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        reportCrimeButton = new JButton("REPORT A CRIME");
        reportCrimeButton.setFont(new Font("Arial", Font.BOLD, 16));

        reportCrimeButton.setBackground(accentColor);
        reportCrimeButton.setContentAreaFilled(false);
        reportCrimeButton.setOpaque(true);

        reportCrimeButton.setForeground(Color.WHITE);
        reportCrimeButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        reportCrimeButton.setFocusPainted(false);
        reportCrimeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        reportCrimeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        reportCrimeButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, 
                "Please login or register to report a crime.", 
                "Authentication Required", 
                JOptionPane.INFORMATION_MESSAGE);
            new LoginPage().setVisible(true);
            this.dispose();
        });
        
        // Add hover effect
        reportCrimeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                reportCrimeButton.setBackground(new Color(220, 40, 55));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                reportCrimeButton.setBackground(accentColor);
            }
        });
        
        // Add spacing between components
        imagePanel.add(Box.createVerticalGlue());
        imagePanel.add(mainTitle);
        imagePanel.add(Box.createRigidArea(new Dimension(0, 20)));
        imagePanel.add(mainSubtitle);
        imagePanel.add(Box.createRigidArea(new Dimension(0, 30)));
        imagePanel.add(reportCrimeButton);
        imagePanel.add(Box.createVerticalGlue());
        
        // Features section
        JPanel featuresPanel = new JPanel();
        featuresPanel.setLayout(new GridLayout(1, 3, 20, 0));
        featuresPanel.setBackground(Color.WHITE);
        featuresPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        
        featuresPanel.add(createFeaturePanel("Secure Reporting", "Submit crime reports securely with complete anonymity protection"));
        featuresPanel.add(createFeaturePanel("Track Investigations", "Monitor the progress of your reported incidents"));
        featuresPanel.add(createFeaturePanel("Community Safety", "Access safety alerts and crime statistics in your area"));
        
        heroPanel.add(imagePanel);
        
        mainPanel.add(heroPanel, BorderLayout.NORTH);
        mainPanel.add(featuresPanel, BorderLayout.CENTER);
    }
    
    private JPanel createFeaturePanel(String title, String description) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 3, 0, primaryColor),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(primaryColor);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JTextArea descLabel = new JTextArea(description);
        descLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        descLabel.setForeground(Color.DARK_GRAY);
        descLabel.setWrapStyleWord(true);
        descLabel.setLineWrap(true);
        descLabel.setEditable(false);
        descLabel.setBackground(Color.WHITE);
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        descLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(descLabel);
        
        return panel;
    }
    
    private void createFooterPanel() {
        footerPanel = new JPanel();
        footerPanel.setLayout(new BorderLayout());
        footerPanel.setBackground(new Color(240, 240, 240));
        footerPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        
        JLabel copyrightLabel = new JLabel("© 2025 National Crime Reporting System. All rights reserved.");
        copyrightLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        copyrightLabel.setForeground(Color.DARK_GRAY);
        
        JPanel linksPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        linksPanel.setBackground(new Color(240, 240, 240));
        
        String[] links = {"About Us", "Contact", "Privacy Policy", "Terms of Service"};
        for (String link : links) {
            JLabel linkLabel = new JLabel(link);
            linkLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            linkLabel.setForeground(new Color(70, 130, 180));
            linkLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            linkLabel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
            linksPanel.add(linkLabel);
            
            // Add hover effect
            linkLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    linkLabel.setForeground(primaryColor);
                }
                
                @Override
                public void mouseExited(MouseEvent e) {
                    linkLabel.setForeground(new Color(70, 130, 180));
                }
            });
        }
        
        footerPanel.add(copyrightLabel, BorderLayout.WEST);
        footerPanel.add(linksPanel, BorderLayout.EAST);
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
        g2d.setFont(new Font("Arial", Font.BOLD, 24));
        FontMetrics fm = g2d.getFontMetrics();
        String text = "CRS";
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getHeight();
        g2d.drawString(text, (width - textWidth) / 2, height / 2 + textHeight / 4);
        
        g2d.dispose();
        return new ImageIcon(image);
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            new LandingPage().setVisible(true);
        });
    }
}