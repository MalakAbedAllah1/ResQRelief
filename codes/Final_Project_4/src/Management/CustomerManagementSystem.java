package Management;

import Operations.*;
import Customer.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CustomerManagementSystem implements CustomerManager.CustomerUpdateListener {  // Create a new class called CustomerManagementSystem. It follows an interface called CustomerUpdateListener inside a class called CustomerManager. I will implement all the functions required by this interface.
    private static CustomerManagementSystem instance;  // static: instead of making it an object
    private static JTable customerTable;
    private static DefaultTableModel tableModel;
    private static JFrame allCustomersFrame;
    private static JLabel countLabel;
    private static JFrame mainFrame; // Make main frame accessible





    // Modern Color Scheme
    // Professional Color Scheme matching the logo
    private static final Color PRIMARY_COLOR = new Color(184, 134, 11);      // Dark Yellow (from logo)
    private static final Color PRIMARY_DARK = new Color(146, 107, 9);        // Darker Yellow shade
    private static final Color SECONDARY_COLOR = new Color(45, 45, 45);      // Dark charcoal (professional dark)
    private static final Color SUCCESS_COLOR = new Color(184, 134, 11);      // Dark Yellow for success
    private static final Color DANGER_COLOR = new Color(169, 68, 66);        // Muted red for danger
    private static final Color CARD_COLOR = Color.WHITE;                              // Pure white cards
    private static final Color TEXT_COLOR = new Color(33, 37, 41);           // Dark text
    private static final Color MUTED_TEXT = new Color(108, 117, 125);        // Muted gray text

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Set modern look and feel,to display the system depends on the kind of the PC (Like: Windows, MAC)
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

                // Custom UI properties for modern look, (Button: for each button in the system "In general")
                //when we focus on a button in the UI it will be transparent (not visible) using (0,0,0,0) and then change all the borders(these changes is for all the jBottons in the ui)
                UIManager.put("Button.focus", new Color(0, 0, 0, 0));//to change the chosen property by choosing it with "",and then change what we want after the (,)
                UIManager.put("Button.border", BorderFactory.createEmptyBorder(10, 20, 10, 20));

            } catch (Exception e) {
                System.out.println("Could not set system look and feel, using default.");
            }

            instance = new CustomerManagementSystem(); // Singleton system-create one copy of the class
            instance.createSplashScreen();// Start with welcome window instead
        });
    }



    private void createSplashScreen() {
        // Create the splash screen frame
        JFrame splashFrame = new JFrame();

        splashFrame.setUndecorated(true); // Remove window decorations
        splashFrame.setSize(700, 500);
        splashFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        splashFrame.setLayout(new BorderLayout());
        splashFrame.getContentPane().setBackground(Color.WHITE);

        // Create the splash content
        JPanel splashContent = createSplashContent();
        splashFrame.add(splashContent, BorderLayout.CENTER);

        // Center the splash screen
        splashFrame.setLocationRelativeTo(null);
        splashFrame.setVisible(true);

        // Timer for showing the splash screen (2.5 seconds visible)
        Timer showTimer = new Timer(2500, _ -> {
            // Start fade out animation
            fadeOutSplashScreen(splashFrame);
        });
        showTimer.setRepeats(false);
        showTimer.start();
    }

    private void fadeOutSplashScreen(JFrame splashFrame) {
        Timer fadeTimer = new Timer(50, null); // every 50ms reduces the clarity and the transparency
        final float[] opacity = {1.0f}; // Starting opacity, starting the clarity reduction
        final float fadeStep = 0.1f; // How much to fade each step

        fadeTimer.addActionListener(_ -> {
            opacity[0] -= fadeStep;

            if (opacity[0] <= 0.0f) {
                // Fade complete - dispose splash and show welcome (closes the first GUI and moves to the next one
                fadeTimer.stop();
                splashFrame.dispose();
                createWelcomeWindow();
            } else {
                // Continue fading
                splashFrame.setOpacity(opacity[0]); //if the reduction (opacity operation) did not finish
            }
        });

        fadeTimer.start();
    }


    private JPanel createSplashContent() {
        JPanel splashPanel = createSplashBackgroundPanel("C:\\Users\\dell2024\\IdeaProjects\\Final_Project_4\\src\\Images\\_Navigating the Impact_ How COVID-19 Is Shaping Real Estate Sales_ (1).jfif");


        splashPanel.setLayout(new BorderLayout());
        splashPanel.setBorder(BorderFactory.createEmptyBorder(60, 50, 60, 50));

        // Making content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false); // to make the panel trasparent because all the panels are non trasparents by defult



        // Animated company logo/icon
        JLabel logoLabel = new JLabel("🍽️") {
            private long startTime = System.currentTimeMillis();

            @Override
            protected void paintComponent(Graphics g) {
                // Gentle rotation animation
                Graphics2D g2 = (Graphics2D) g.create();
                long elapsed = System.currentTimeMillis() - startTime;
                double angle = (elapsed / 8000.0) * Math.PI * 2; // 8-second rotation

                g2.rotate(angle, getWidth() / 2.0, getHeight() / 2.0);
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        logoLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 90));
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // Company title with shadow effect
        JLabel titleLabel = new JLabel("Emergency Food") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw shadow
                g2.setColor(new Color(0, 0, 0, 50));
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent()) / 2;
                g2.drawString(getText(), x + 2, y + 2);

                // Draw main text
                g2.setColor(getForeground());
                g2.drawString(getText(), x, y);
                g2.dispose();
            }
        };
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 46));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setOpaque(false);

        JLabel subtitleLabel = new JLabel("Distribution System") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw shadow
                g2.setColor(new Color(0, 0, 0, 50));
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent()) / 2;
                g2.drawString(getText(), x + 2, y + 2);

                // Draw main text
                g2.setColor(getForeground());
                g2.drawString(getText(), x, y);
                g2.dispose();
            }
        };
        subtitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 46));
        subtitleLabel.setForeground(Color.WHITE);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitleLabel.setOpaque(false);

        // Animated tagline
        JLabel taglineLabel = new JLabel("Helping Communities in Times of Need") {
            private long startTime = System.currentTimeMillis();

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setFont(getFont());

                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent()) / 2;
                g2.drawString(getText(), x, y);
                g2.dispose();
            }
        };
        taglineLabel.setFont(new Font("Segoe UI", Font.ITALIC, 20));
        taglineLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        taglineLabel.setBorder(BorderFactory.createEmptyBorder(25, 0, 0, 0));
        taglineLabel.setOpaque(false);

        // Animated loading dots
        JLabel loadingLabel = new JLabel("Loading") {
            private long startTime = System.currentTimeMillis();
            private String[] dots = {"", ".", "..", "..."};

            @Override
            protected void paintComponent(Graphics g) {
                long elapsed = System.currentTimeMillis() - startTime;
                int dotIndex = (int)((elapsed / 500) % dots.length);

                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 180));
                g2.setFont(getFont());

                String text = "Loading" + dots[dotIndex];
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(text)) / 2;
                int y = (getHeight() + fm.getAscent()) / 2;
                g2.drawString(text, x, y);
                g2.dispose();
            }
        };
        loadingLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        loadingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        loadingLabel.setBorder(BorderFactory.createEmptyBorder(35, 0, 0, 0));
        loadingLabel.setOpaque(false);

        // Add components to content panel
        contentPanel.add(logoLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        contentPanel.add(subtitleLabel);
        contentPanel.add(taglineLabel);
        contentPanel.add(loadingLabel);


        // Enhanced version info at bottom
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));

        JLabel versionLabel = new JLabel("Version 2.0 | Professional Edition");

        versionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        versionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        versionLabel.setOpaque(false);

        bottomPanel.add(versionLabel, BorderLayout.CENTER);

        splashPanel.add(contentPanel, BorderLayout.CENTER);
        splashPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Start animation timer for repainting
        Timer animationTimer = new Timer(50, _ -> splashPanel.repaint());
        animationTimer.start();

        // Stop animation after 3 seconds
        Timer stopTimer = new Timer(3000, _ -> animationTimer.stop());
        stopTimer.setRepeats(false);
        stopTimer.start();

        return splashPanel;
    }


    private void createWelcomeWindow() {
        // Create the welcome frame
        mainFrame = new JFrame("Customer Management System - Welcome");
        mainFrame.setSize(600, 500);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLayout(new BorderLayout());
        mainFrame.getContentPane().setBackground(Color.WHITE);

        try {
            ImageIcon frameIcon = new ImageIcon("C:\\Users\\dell2024\\IdeaProjects\\Final_Project_4\\src\\Images\\logo.jpeg");
            mainFrame.setIconImage(frameIcon.getImage());
        } catch (Exception ex) {
            System.out.println("Could not load frame icon: " + ex.getMessage());
        }

        // Create welcome content
        JPanel welcomeContent = createWelcomeContent();

        mainFrame.add(welcomeContent, BorderLayout.CENTER);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);

        System.out.println("Customer Management System Welcome Screen started!");
    }

    private JPanel createWelcomeContent() {
        JPanel welcomePanel = new JPanel();
        welcomePanel.setLayout(new BorderLayout());
        welcomePanel.setBackground(Color.WHITE);
        welcomePanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        // Title Section
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(Color.WHITE);

        JLabel welcomeTitle = new JLabel("Welcome to");
        welcomeTitle.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        welcomeTitle.setForeground(MUTED_TEXT);
        welcomeTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel systemTitle = new JLabel("Customer Management System");
        systemTitle.setFont(new Font("Segoe UI", Font.BOLD, 32));
        systemTitle.setForeground(PRIMARY_COLOR);
        systemTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Please select your access level");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitle.setForeground(MUTED_TEXT);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        titlePanel.add(welcomeTitle);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        titlePanel.add(systemTitle);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 20)));
        titlePanel.add(subtitle);

        // Buttons Section
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        buttonPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Customer button (only add operation)
        JPanel customerCard = createWelcomeCard("👤", "Customer Access", "New order registration", SUCCESS_COLOR, () -> openAddCustomerWindow());

        // Admin button (full access)
        JPanel adminCard = createWelcomeCard("🔒", "Admin Access", "Full system management", PRIMARY_COLOR, () -> showAdminLogin());

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 1; //gridx = 0 : first column, gridy = 0 : first row, weightx = 1 : its weight
        buttonPanel.add(customerCard, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        buttonPanel.add(adminCard, gbc);

        welcomePanel.add(titlePanel, BorderLayout.NORTH);
        welcomePanel.add(buttonPanel, BorderLayout.CENTER);

        return welcomePanel;
    }

    private JPanel createWelcomeCard(String icon, String title, String description, Color accentColor, Runnable action) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Card background with rounded corners
                g2.setColor(CARD_COLOR);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

                // Subtle shadow
                g2.setColor(new Color(0, 0, 0, 10));
                g2.fillRoundRect(3, 3, getWidth(), getHeight(), 15, 15);

                g2.dispose();
            }
        };

        card.setLayout(new BorderLayout());
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));
        card.setPreferredSize(new Dimension(400, 120));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Content panel
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);

        // Left side - icon
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 36));
        iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));

        // Right side - text
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel descLabel = new JLabel(description);
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descLabel.setForeground(MUTED_TEXT);
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        textPanel.add(titleLabel);
        textPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        textPanel.add(descLabel);

        contentPanel.add(iconLabel, BorderLayout.WEST);
        contentPanel.add(textPanel, BorderLayout.CENTER);

        // Accent line
        JPanel accentLine = new JPanel();
        accentLine.setBackground(accentColor);
        accentLine.setPreferredSize(new Dimension(0, 4));

        card.add(accentLine, BorderLayout.NORTH);
        card.add(contentPanel, BorderLayout.CENTER);

        // Hover effects
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 20));
                card.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(CARD_COLOR);
                card.repaint();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                // Click animation
                Timer timer = new Timer(100, evt -> {
                    card.setBackground(CARD_COLOR);
                    card.repaint();
                    ((Timer) evt.getSource()).stop();
                });
                card.setBackground(new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 50));
                card.repaint();
                timer.start();

                // Execute action
                SwingUtilities.invokeLater(action);
            }
        });

        return card;
    }

    private void showAdminLogin() {
        // Create admin login dialog
        JDialog loginDialog = new JDialog(mainFrame, "Admin Authentication", true);
        loginDialog.setSize(400, 300);
        loginDialog.setLayout(new BorderLayout());
        loginDialog.getContentPane().setBackground(Color.WHITE);

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel headerLabel = new JLabel("Admin Login");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Email field
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        emailLabel.setForeground(TEXT_COLOR);

        JTextField emailField = new JTextField();
        emailField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        emailField.setPreferredSize(new Dimension(250, 35));
        emailField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(206, 212, 218)),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));

        // Password field
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        passwordLabel.setForeground(TEXT_COLOR);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setPreferredSize(new Dimension(250, 35));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(206, 212, 218)),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));

        // Show/Hide Password Button
        JButton showPasswordButton = new JButton("👁");
        showPasswordButton.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
        showPasswordButton.setPreferredSize(new Dimension(35, 35));
        showPasswordButton.setBackground(new Color(233, 236, 239));
        showPasswordButton.setBorder(BorderFactory.createLineBorder(new Color(206, 212, 218)));
        showPasswordButton.setFocusPainted(false);
        showPasswordButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Panel to hold password field and show button
        JPanel passwordPanel = new JPanel(new BorderLayout());
        passwordPanel.add(passwordField, BorderLayout.CENTER);
        passwordPanel.add(showPasswordButton, BorderLayout.EAST);

        // Show/Hide password functionality
        showPasswordButton.addActionListener(_ -> {
            if (passwordField.getEchoChar() == 0) {  // getEchoChar() : to replace the words with a symbols (*),
                passwordField.setEchoChar('*');
                showPasswordButton.setText("👁");
            } else {
                passwordField.setEchoChar((char) 0);
                showPasswordButton.setText("🙈");
            }
        });

        // Error label
        JLabel errorLabel = new JLabel(" ");
        errorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        errorLabel.setForeground(DANGER_COLOR);

        // Layout form components
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST; // where to put the value in the cell when it is bigger that the content
        formPanel.add(emailLabel, gbc);

        gbc.gridy = 1; gbc.weightx = 1;
        formPanel.add(emailField, gbc);

        gbc.gridy = 2; gbc.weightx = 0;
        formPanel.add(passwordLabel, gbc);

        gbc.gridy = 3; gbc.weightx = 1;
        formPanel.add(passwordPanel, gbc);

        gbc.gridy = 4;
        formPanel.add(errorLabel, gbc);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);

        JButton loginButton = createStyledButton("Login", PRIMARY_COLOR);
        JButton cancelButton = createStyledButton("Cancel", SECONDARY_COLOR);

        loginButton.addActionListener(_ -> {
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (authenticateAdmin(email, password)) {
                loginDialog.dispose();
                showMainOperations();
            } else {
                // Clear the password field after failed attempt
                passwordField.setText("");
                passwordField.requestFocus();
            }
        });

        cancelButton.addActionListener(_ -> loginDialog.dispose());

        // Enter key support
        passwordField.addActionListener(_ -> loginButton.doClick());

        buttonPanel.add(loginButton);
        buttonPanel.add(cancelButton);

        loginDialog.add(headerPanel, BorderLayout.NORTH);
        loginDialog.add(formPanel, BorderLayout.CENTER);
        loginDialog.add(buttonPanel, BorderLayout.SOUTH);

        loginDialog.setLocationRelativeTo(mainFrame);
        loginDialog.setVisible(true);
    }

    private boolean authenticateAdmin(String email, String password) {
        boolean isValid = (email.equals("malakabedallah50@gmail.com") && password.equals("malak123")) ||
                (email.equals("maryamimranhassan@gmail.com") && password.equals("maryam123"));

        if (!isValid) {
            // Show error message on screen
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(
                        null,
                        "Invalid email or password!\nPlease check your credentials and try again.",
                        "Authentication Error",
                        JOptionPane.ERROR_MESSAGE
                );
            });
        }

        return isValid;
    }

    private void showMainOperations() {
        // Hide the welcome window and show the main operations
        mainFrame.dispose();
        createMainWindow();
    }

    private void createMainWindow() {
        // Create the main frame with modern styling
        mainFrame = new JFrame("Customer Management System - Admin Panel");
        mainFrame.setSize(900, 700);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLayout(new BorderLayout());
        mainFrame.getContentPane().setBackground(Color.WHITE);

        try {
            ImageIcon frameIcon = new ImageIcon("C:\\Users\\dell2024\\IdeaProjects\\Final_Project_4\\src\\Images\\logo.jpeg");
            mainFrame.setIconImage(frameIcon.getImage());
        } catch (Exception ex) {
            System.out.println("Could not load frame icon: " + ex.getMessage());
        }

        // Register for updates
        CustomerManager manager = CustomerManager.getInstance();
        manager.addUpdateListener(this);

        // Create beautiful header
        JPanel headerPanel = createHeaderPanel(manager);

        // Create stunning main content
        JPanel mainContent = createMainContent();

        // Create footer with logout button
        JPanel footerPanel = createFooterPanel();

        // Add components to main frame
        mainFrame.add(headerPanel, BorderLayout.NORTH);
        mainFrame.add(mainContent, BorderLayout.CENTER);
        mainFrame.add(footerPanel, BorderLayout.SOUTH);

        // Center the window
        mainFrame.setLocationRelativeTo(null);

        // Add subtle shadow effect (if supported)
        mainFrame.setVisible(true);

        System.out.println("Beautiful Customer Management System started!");
    }

    private JPanel createHeaderPanel(CustomerManager manager) {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // Title with icon
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(PRIMARY_COLOR);

        JLabel iconLabel = new JLabel("[CMS]");
        iconLabel.setFont(new Font("Monospaced", Font.BOLD, 20));
        iconLabel.setForeground(Color.WHITE);

        JLabel titleLabel = new JLabel("Customer Management System - Admin");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));

        titlePanel.add(iconLabel);
        titlePanel.add(titleLabel);

        // Stats panel
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        statsPanel.setBackground(PRIMARY_COLOR);

        countLabel = new JLabel("Total: " + manager.getCustomerCount() + " customers");
        countLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        countLabel.setForeground(Color.WHITE);
        countLabel.setOpaque(true);
        countLabel.setBackground(PRIMARY_DARK);
        countLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Round the corners of count label
        countLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(5, 5, 5, 5),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));

        statsPanel.add(countLabel);

        headerPanel.add(titlePanel, BorderLayout.WEST);
        headerPanel.add(statsPanel, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createMainContent() {
        JPanel mainContent = createBackgroundPanel("C:\\Users\\dell2024\\IdeaProjects\\Final_Project_4\\src\\Images\\Warehouse in the US of Worldcraft Logistics - 2030.jfif");
        mainContent.setLayout(new GridBagLayout());
        mainContent.setBackground(Color.WHITE);
        mainContent.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.BOTH;

        // Create beautiful action cards
        JPanel addCard = createActionCard("+", "Add Customer", "Register new customers", SUCCESS_COLOR, () -> openAddCustomerWindow());
        JPanel searchCard = createActionCard("?", "Search Customer", "Find existing customers", PRIMARY_COLOR, () -> openSearchCustomerWindow());
        JPanel deleteCard = createActionCard("X", "Delete Customer", "Remove customers", DANGER_COLOR, () -> openDeleteCustomerWindow());
        JPanel viewCard = createActionCard("=", "View All", "Display all customers", SECONDARY_COLOR, () -> displayAllCustomers());

        // Layout the cards in a 2x2 grid
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 1; gbc.weighty = 1;
        mainContent.add(addCard, gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        mainContent.add(searchCard, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        mainContent.add(deleteCard, gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        mainContent.add(viewCard, gbc);

        return mainContent;
    }

    private JPanel createActionCard(String icon, String title, String description, Color accentColor, Runnable action) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Card background with rounded corners
                g2.setColor(CARD_COLOR);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                // Subtle shadow
                g2.setColor(new Color(0, 0, 0, 10));
                g2.fillRoundRect(2, 2, getWidth(), getHeight(), 20, 20);

                g2.dispose();
            }
        };

        card.setLayout(new BorderLayout());
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(30, 25, 30, 25));
        card.setPreferredSize(new Dimension(300, 200));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Icon and title panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        topPanel.add(iconLabel, BorderLayout.CENTER);
        topPanel.add(titleLabel, BorderLayout.SOUTH);

        // Description
        JLabel descLabel = new JLabel(description);
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descLabel.setForeground(MUTED_TEXT);
        descLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Accent line
        JPanel accentLine = new JPanel();
        accentLine.setBackground(accentColor);
        accentLine.setPreferredSize(new Dimension(0, 4));

        card.add(topPanel, BorderLayout.CENTER);
        card.add(descLabel, BorderLayout.SOUTH);
        card.add(accentLine, BorderLayout.NORTH);

        // Hover effects
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 20));
                card.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(CARD_COLOR);
                card.repaint();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                // Click animation
                Timer timer = new Timer(100, evt -> {
                    card.setBackground(CARD_COLOR);
                    card.repaint();
                    ((Timer) evt.getSource()).stop();
                });
                card.setBackground(new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 50));
                card.repaint();
                timer.start();

                // Execute action
                SwingUtilities.invokeLater(action);
            }
        });

        return card;
    }

    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(Color.WHITE);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JLabel statusLabel = new JLabel("Data: customer.txt | Ready");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setForeground(Color.BLACK);  // Change from Color.WHITE

        // Right panel with version and logout
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setBackground(Color.WHITE);  // Change from new Color(17, 24, 39)

        JLabel versionLabel = new JLabel("Version 2.0");
        versionLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        versionLabel.setForeground(Color.BLACK);  // Change from Color.WHITE

        JButton logoutButton = new JButton("Log Out");
        logoutButton.setFont(new Font("Segoe UI", Font.BOLD, 11));
        logoutButton.setForeground(Color.BLACK);  // Keep Black text
        logoutButton.setBackground(Color.BLACK);
        logoutButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        logoutButton.setFocusPainted(false);
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        logoutButton.addActionListener(_ -> {
            int result = JOptionPane.showConfirmDialog(
                    mainFrame,
                    "Are you sure you want to logout?",
                    "Logout Confirmation",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if (result == JOptionPane.YES_OPTION) {
                mainFrame.dispose();
                createWelcomeWindow();
            }
        });

        rightPanel.add(versionLabel);
        rightPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        rightPanel.add(logoutButton);

        footerPanel.add(statusLabel, BorderLayout.WEST);
        footerPanel.add(rightPanel, BorderLayout.EAST);

        return footerPanel;
    }
//--------------------------------------------------------------------------------------------------
    private JPanel createBackgroundPanel(String imagePath) {
        return new JPanel() {
            private Image backgroundImage;

            {
                try {
                    backgroundImage = new ImageIcon(imagePath).getImage();
                } catch (Exception e) {
                    System.out.println("Could not load background image: " + e.getMessage());
                }
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                    g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
    }

    private JPanel createSplashBackgroundPanel(String imagePath) {
        return new JPanel() {
            private Image backgroundImage;

            {
                try {
                    backgroundImage = new ImageIcon(imagePath).getImage();
                } catch (Exception e) {
                    System.out.println("Could not load splash background image: " + e.getMessage());
                }
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

                // Draw background image first
                if (backgroundImage != null) {
                    g2.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }

                // Then draw the original gradient and pattern overlay
                GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(184, 134, 11, 180),        // Semi-transparent Dark Yellow top
                        0, getHeight(), new Color(240, 240, 240, 180)  // Semi-transparent Light gray bottom
                );
                g2.setPaint(gradient);
                g2.fillRect(0, 0, getWidth(), getHeight());

                // Subtle professional pattern overlay
                g2.setColor(new Color(255, 255, 255, 20));
                for (int i = 0; i < getWidth(); i += 40) {
                    for (int j = 0; j < getHeight(); j += 40) {
                        g2.fillOval(i, j, 2, 2);  // Small consistent dots
                    }
                }

                // Professional border
                g2.setColor(new Color(184, 134, 11, 100));
                g2.drawRoundRect(10, 10, getWidth() - 20, getHeight() - 20, 20, 20);

                g2.dispose();
            }
        };
    }

    //----------------------------------------------------------------------------------------------

    private void openAddCustomerWindow() {
        SwingUtilities.invokeLater(() -> AddCustomer.main(null));
    }

    private void openSearchCustomerWindow() {
        SwingUtilities.invokeLater(() -> SearchCustomer.main(null));
    }

    private void openDeleteCustomerWindow() {
        SwingUtilities.invokeLater(() -> DeleteCustomer.main(null));
    }

    private void updateCountLabel() {
        if (countLabel != null) {
            CustomerManager manager = CustomerManager.getInstance();
            countLabel.setText("Total: " + manager.getCustomerCount() + " customers");
        }
    }

    public static void displayAllCustomers() {
        CustomerManager manager = CustomerManager.getInstance();

        if (allCustomersFrame != null) {
            refreshCustomerTable();
            allCustomersFrame.setVisible(true);
            allCustomersFrame.toFront();
            return;
        }

        allCustomersFrame = new JFrame("All Customers");
        allCustomersFrame.setSize(1200, 700);
        allCustomersFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        allCustomersFrame.setLayout(new BorderLayout());
        allCustomersFrame.getContentPane().setBackground(Color.WHITE);

        try {
            ImageIcon frameIcon = new ImageIcon("C:\\Users\\dell2024\\IdeaProjects\\Final_Project_4\\src\\Images\\logo.jpeg");
            allCustomersFrame.setIconImage(frameIcon.getImage());
        } catch (Exception ex) {
            System.out.println("Could not load frame icon: " + ex.getMessage());
        }

        // Beautiful header for table window
        JPanel tableHeader = new JPanel(new BorderLayout());
        tableHeader.setBackground(PRIMARY_COLOR);
        tableHeader.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel tableTitle = new JLabel("Customer Database");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        tableTitle.setForeground(Color.WHITE);

        JLabel tableCount = new JLabel("Total: " + manager.getCustomerCount() + " customers");
        tableCount.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tableCount.setForeground(Color.WHITE);

        tableHeader.add(tableTitle, BorderLayout.WEST);
        tableHeader.add(tableCount, BorderLayout.EAST);

        // Create beautiful table
        String[] columnNames = {"ID", "Name", "Email", "Family Size", "Emergency Level", "Location", "Notes"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        customerTable = new JTable(tableModel);
        customizeTable(customerTable);

        JScrollPane scrollPane = new JScrollPane(customerTable);
        scrollPane.setBackground(Color.WHITE);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Beautiful control panel
        JPanel controlPanel = new JPanel(new FlowLayout());
        controlPanel.setBackground(Color.WHITE);
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 20, 30));


        JButton closeButton = createStyledButton("Close", SECONDARY_COLOR);
        closeButton.addActionListener(_ -> allCustomersFrame.setVisible(false));


        controlPanel.add(closeButton);

        allCustomersFrame.add(tableHeader, BorderLayout.NORTH);
        allCustomersFrame.add(scrollPane, BorderLayout.CENTER);
        allCustomersFrame.add(controlPanel, BorderLayout.SOUTH);

        refreshCustomerTable();
        allCustomersFrame.setLocationRelativeTo(null);
        allCustomersFrame.setVisible(true);
    }

    private static void customizeTable(JTable table) {
        // Modern table styling
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(35);
        table.setGridColor(new Color(222, 226, 230));
        table.setSelectionBackground(new Color(PRIMARY_COLOR.getRed(), PRIMARY_COLOR.getGreen(), PRIMARY_COLOR.getBlue(), 50));
        table.setSelectionForeground(TEXT_COLOR);
        table.setShowGrid(true);
        table.setIntercellSpacing(new Dimension(1, 1));

        // Header styling
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(64, 64, 64));  // Dark gray instead of (31, 41, 55)
        header.setForeground(Color.BLACK);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, PRIMARY_COLOR));
        header.setPreferredSize(new Dimension(0,45));

        // Custom cell renderer for alternating rows
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (!isSelected) {
                    if (row % 2 == 0) {
                        c.setBackground(new Color(248, 249, 250));  // Very light gray
                    } else {
                        c.setBackground(Color.WHITE);  // Pure white
                    }
                }

                setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
                return c;
            }
        };

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(renderer); //renderer is to make changing on the cells of the row
        }
    }

    private static JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2.setColor(color.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(color.brighter());
                } else {
                    g2.setColor(color);
                }

                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

                g2.setColor(Color.WHITE);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int stringWidth = fm.stringWidth(getText());
                int stringHeight = fm.getAscent();
                g2.drawString(getText(), (getWidth() - stringWidth) / 2, (getHeight() + stringHeight) / 2 - 2);

                g2.dispose();
            }
        };

        button.setForeground(Color.BLACK);
        button.setBackground(new Color(184, 134, 11));
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(100, 35));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return button;
    }

    public static void refreshCustomerTable() {
        if (customerTable != null && tableModel != null) {
            CustomerManager manager = CustomerManager.getInstance();

            tableModel.setRowCount(0);

            for (Customer customer : manager.getAllCustomers()) {
                Object[] row = {
                        customer.getId(),
                        customer.getName(),
                        customer.getEmail(),
                        customer.getFamilySize(),
                        customer.getEmergencyLevel() + " - " + customer.getEmergencyLevelDescription(),
                        customer.getLocation(),
                        customer.getNotes().isEmpty() ? "No notes" : customer.getNotes()
                };
                tableModel.addRow(row);
            }

            tableModel.fireTableDataChanged(); // to tell the table that we made changes on it because he can't know by itself
        }
    }

    // CustomerUpdateListener implementation
    @Override
    public void onCustomerAdded(Customer customer) {
        SwingUtilities.invokeLater(() -> {
            updateCountLabel();
            refreshCustomerTable();
        });
    }

    @Override
    public void onCustomerDeleted(String customerId) {
        SwingUtilities.invokeLater(() -> {
            updateCountLabel();
            refreshCustomerTable();
        });
    }

    public static void onChildWindowClosed() {
        if (instance != null) {
            instance.updateCountLabel();
        }
    }
}