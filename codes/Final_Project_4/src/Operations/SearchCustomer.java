package Operations;


import Customer.*;
import Management.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class SearchCustomer {
    private static JFrame frame;

    // Beautiful Color Scheme
    private static final Color PRIMARY_COLOR = new Color(184, 134, 11);
    private static final Color SUCCESS_COLOR = new Color(40, 167, 69);
    private static final Color DANGER_COLOR = new Color(220, 53, 69);
    private static final Color BACKGROUND_COLOR = new Color(248, 249, 250);
    private static final Color CARD_COLOR = Color.WHITE;
    private static final Color TEXT_COLOR = new Color(33, 37, 41);
    private static final Color MUTED_TEXT = new Color(108, 117, 125);
    private static final Color BORDER_COLOR = new Color(222, 226, 230);

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createSearchCustomerWindow());
    }

    private static void createSearchCustomerWindow() {
        if (frame != null) {
            frame.setVisible(true);
            frame.toFront();
            return;
        }

        frame = new JFrame("Search Customer");
        frame.setSize(700, 650);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(BACKGROUND_COLOR);

        try {
            ImageIcon frameIcon = new ImageIcon("C:\\Users\\dell2024\\IdeaProjects\\Final_Project_4\\src\\Images\\logo.jpeg");
            frame.setIconImage(frameIcon.getImage());
        } catch (Exception ex) {
            System.out.println("Could not load frame icon: " + ex.getMessage());
        }

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                frame = null;
                CustomerManagementSystem.onChildWindowClosed();
            }
        });

        // Beautiful header
        JPanel headerPanel = createHeaderPanel();

        // Customer content panel
        JPanel mainPanel = new JPanel(new BorderLayout());  // is a تخطيط that separates the screen into 5 parts -east, west, north, south, center
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30)); // حدود فارغة بمقدار 30 بيكسل

        // Search criteria panel
        JPanel searchPanel = createSearchPanel();
        JTextField idField = createStyledTextField("Enter customer ID...");
        JTextField nameField = createStyledTextField("Enter customer name...");

        addSearchField(searchPanel, "[ID] Customer ID", idField);
        addSearchField(searchPanel, "[Name] Customer Name", nameField);

        // Buttons panel
        JPanel buttonPanel = createButtonPanel();
        JButton searchButton = createStyledButton("Search", PRIMARY_COLOR, "?");
        JButton clearButton = createStyledButton("Clear", MUTED_TEXT, "X");

        buttonPanel.add(searchButton);
        buttonPanel.add(Box.createHorizontalStrut(15));  //to create a space between the 2 buttons
        buttonPanel.add(clearButton);

        // Results panel
        JPanel resultsPanel = createResultsPanel();
        JTextArea resultArea = createStyledTextArea();
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                "Search Results",
                0, 0,
                new Font("Segoe UI", Font.BOLD, 14),
                TEXT_COLOR
        ));
        scrollPane.setBackground(CARD_COLOR);
        scrollPane.setPreferredSize(new Dimension(0, 200));

        resultsPanel.add(scrollPane, BorderLayout.CENTER);

        // Status panel
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBackground(BACKGROUND_COLOR);
        statusPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        JLabel statusLabel = new JLabel("Enter Customer ID or Name to search");
        statusLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        statusLabel.setForeground(MUTED_TEXT);
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusPanel.add(statusLabel);

        // Layout main panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(BACKGROUND_COLOR);
        topPanel.add(searchPanel, BorderLayout.NORTH);
        topPanel.add(buttonPanel, BorderLayout.CENTER);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(resultsPanel, BorderLayout.CENTER);
        mainPanel.add(statusPanel, BorderLayout.SOUTH);
//-----------------------------------------------------------------
        CustomerManager manager = CustomerManager.getInstance();

        // Search button action
        searchButton.addActionListener(_ -> {
            String inputId = idField.getText().trim();
            String inputName = nameField.getText().trim();

            statusLabel.setForeground(TEXT_COLOR);

            // Clean placeholder text
            if (inputId.equals("Enter customer ID...")) inputId = "";
            if (inputName.equals("Enter customer name...")) inputName = "";

            if (inputId.isEmpty() && inputName.isEmpty()) {
                resultArea.setText("[ERROR] SEARCH ERROR\n\n" +
                        "Please enter either Customer ID or Name to search.\n\n" +
                        "Tip: You can search by ID, name, or both for precise results.");
                statusLabel.setText("[ERROR] Validation Error: No search criteria provided");
                statusLabel.setForeground(DANGER_COLOR);
                return;
            }

            Customer foundCustomer = null;
            String searchType = "";

            try {
                if (!inputId.isEmpty() && !inputName.isEmpty()) {
                    foundCustomer = manager.findCustomer(inputId, inputName);
                    searchType = "ID and Name";
                } else if (!inputId.isEmpty()) {
                    foundCustomer = manager.findCustomerById(inputId);
                    searchType = "ID";
                } else {
                    foundCustomer = manager.findCustomerByName(inputName);
                    searchType = "Name";
                }

                if (foundCustomer != null) {
                    displayCustomerResult(resultArea, foundCustomer, searchType);
                    statusLabel.setText("[OK] Search completed successfully - Customer found!");
                    statusLabel.setForeground(SUCCESS_COLOR);
                } else {
                    displayNoResultsFound(resultArea, searchType, inputId, inputName);
                    statusLabel.setText("[ERROR] No customer found with the provided criteria");
                    statusLabel.setForeground(DANGER_COLOR);
                }
            } catch (Exception ex) {
                resultArea.setText("[ERROR] SEARCH ERROR\n\n" +
                        "An unexpected error occurred during search:\n" + ex.getMessage() + "\n\n" +
                        "Please try again or contact support if the problem persists.");
                statusLabel.setText("[ERROR] Search error occurred");
                statusLabel.setForeground(DANGER_COLOR);
            }
        });

        // Clear button action
        clearButton.addActionListener(_ -> {
            clearFields(idField, nameField);
            resultArea.setText("Ready to search...\n\n" +
                    "Enter a Customer ID or Name above and click 'Search' to find customer information.\n\n" +
                    "Tips:\n" +
                    "• Use exact ID for fastest results\n" +
                    "• Name search is case-insensitive\n" +
                    "• You can search by both ID and name for verification");
            statusLabel.setText("Form cleared - Ready for new search");
            statusLabel.setForeground(MUTED_TEXT);
            idField.requestFocus();        //to make the admin able to write the ID directly without needing to click on the text field
        });

        // Enter key support
        idField.addActionListener(_ -> searchButton.doClick()); /*Beside the ActionEvent,we e ---> _ ,
         we replaced variable e with underscore because any java version up to 21 allows to delete this lambda variable and replacing it with _
         */
        nameField.addActionListener(_ -> searchButton.doClick());
        frame.getRootPane().setDefaultButton(searchButton);   // to make the search button (the default button) that means whenwe click (enter) on the keyboard, this button action will be done

        // Initial content
        resultArea.setText("Ready to search...\n\n" +
                "Enter a Customer ID or Name above and click 'Search' to find customer information.\n\n" +
                "Tips:\n" +
                "• Use exact ID for fastest results\n" +
                "• Name search is case-insensitive\n" +
                "• You can search by both ID and name for verification");

        frame.add(headerPanel, BorderLayout.NORTH);
        frame.add(mainPanel, BorderLayout.CENTER);

        frame.setLocationRelativeTo(null);   // make the frame back to center (null)
        frame.setVisible(true);

        SwingUtilities.invokeLater(() -> idField.requestFocus());      // to give the focus for the ID field when the frame starts
    }

    private static JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        JLabel titleLabel = new JLabel("? Search Customer");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);

        JLabel subtitleLabel = new JLabel("Find customers by ID or name");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(255, 255, 255, 180));

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(PRIMARY_COLOR);
        titlePanel.add(titleLabel, BorderLayout.NORTH);
        titlePanel.add(subtitleLabel, BorderLayout.SOUTH);

        headerPanel.add(titlePanel, BorderLayout.WEST);

        return headerPanel;
    }

    private static JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.Y_AXIS));
        searchPanel.setBackground(CARD_COLOR);
        searchPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(BORDER_COLOR, 1),
                        "Search Criteria",
                        0, 0,          //titleJustification = محاذاة النص داخل اللابل
                        new Font("Segoe UI", Font.BOLD, 14),
                        TEXT_COLOR
                ),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        return searchPanel;
    }

    private static JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        return buttonPanel;
    }

    private static JPanel createResultsPanel() {
        JPanel resultsPanel = new JPanel(new BorderLayout());
        resultsPanel.setBackground(BACKGROUND_COLOR);

        return resultsPanel;
    }

    private static JTextField createStyledTextField(String placeholder) {
        JTextField field = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {   //to change the painting way for the JTextField
                Graphics2D g2 = (Graphics2D) g.create();  //change the painter to get better results
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8); //to make the baorders in rounded way (حواف دائرية)

                if (hasFocus()) {   // if we are focusing on the field, it will be bold and shiny, if not then not
                    g2.setColor(PRIMARY_COLOR);
                    g2.setStroke(new BasicStroke(2));
                } else {
                    g2.setColor(BORDER_COLOR);
                    g2.setStroke(new BasicStroke(1));
                }
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);

                g2.dispose();     // to finish the editing
                super.paintComponent(g);  //to add the text to the background
            }
        };

        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(0, 40));
        field.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        field.setBackground(Color.WHITE);
        field.setForeground(TEXT_COLOR);
        field.setText(placeholder);
        field.setForeground(MUTED_TEXT);

        field.addFocusListener(new FocusAdapter() {     // to control showing up and hiding the dumyText "نص وهمي"
            @Override
            public void focusGained(FocusEvent e) {  // the text box contains a "نص وهمي" so we clear it to write our input
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(TEXT_COLOR);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {    //when the focus is lost on this textBox "means that we moved the moused away from it" we replace the "نص وهمي
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(MUTED_TEXT);
                }
            }
        });

        return field;
    }

    private static JTextArea createStyledTextArea() {
        JTextArea textArea = new JTextArea();
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        textArea.setBackground(CARD_COLOR);
        textArea.setForeground(TEXT_COLOR);
        textArea.setEditable(false);
        textArea.setLineWrap(true);    // to ensure that a new line will be created when we arrive to the end of the fram of the textArea
        textArea.setWrapStyleWord(true);  // to ensure that the word will not be cut instead of that, move it to the next line
        textArea.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        return textArea;
    }

    private static JButton createStyledButton(String text, Color color, String icon) {
        JButton button = new JButton(icon + " " + text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color bgColor = color;
                if (getModel().isPressed()) {
                    bgColor = color.darker();
                } else if (getModel().isRollover()) {
                    bgColor = color.brighter();
                }

                g2.setColor(bgColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

                g2.setColor(Color.WHITE);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();   //to give the information about text
                int stringWidth = fm.stringWidth(getText());
                int stringHeight = fm.getAscent();
                g2.drawString(getText(), (getWidth() - stringWidth) / 2, (getHeight() + stringHeight) / 2 - 2); // to edit the measurments

                g2.dispose();
            }
        };

        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(130, 40));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return button;
    }

    private static void addSearchField(JPanel parent, String label, JTextField field) {
        JPanel fieldPanel = new JPanel(new BorderLayout());
        fieldPanel.setBackground(CARD_COLOR);
        fieldPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        JLabel fieldLabel = new JLabel(label);
        fieldLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        fieldLabel.setForeground(TEXT_COLOR);
        fieldLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));

        fieldPanel.add(fieldLabel, BorderLayout.NORTH);
        fieldPanel.add(field, BorderLayout.CENTER);

        parent.add(fieldPanel);
    }

    private static void displayCustomerResult(JTextArea resultArea, Customer customer, String searchType) {
        StringBuilder sb = new StringBuilder();
        sb.append("[FOUND] CUSTOMER FOUND\n");
        sb.append("===============================================\n");
        sb.append("Search Method: ").append(searchType).append("\n\n");

        sb.append("ID: ").append(customer.getId()).append("\n");
        sb.append("Name: ").append(customer.getName()).append("\n");
        sb.append("Email: ").append(customer.getEmail()).append("\n");
        sb.append("Family Size: ").append(customer.getFamilySize()).append(" members\n");
        sb.append("Emergency Level: ").append(customer.getEmergencyLevel());
        sb.append(" (").append(getEmergencyLevelDescription(customer.getEmergencyLevel())).append(")\n");
        sb.append("Location: ").append(customer.getLocation()).append("\n");
        sb.append("Notes: ").append(customer.getNotes().isEmpty() ? "No additional notes" : customer.getNotes()).append("\n");

        sb.append("===============================================\n");
        sb.append("Record Status: Active\n");
        sb.append("Use this information to assist the customer.");

        resultArea.setText(sb.toString());
    }

    private static void displayNoResultsFound(JTextArea resultArea, String searchType, String inputId, String inputName) {
        StringBuilder sb = new StringBuilder();
        sb.append("❌ NO CUSTOMER FOUND\n");
        sb.append("═══════════════════════════════════════════════════════════\n");
        sb.append("Search Method: ").append(searchType).append("\n");
        if (!inputId.isEmpty()) {
            sb.append("Searched ID: ").append(inputId).append("\n");
        }
        if (!inputName.isEmpty()) {
            sb.append("Searched Name: ").append(inputName).append("\n");
        }
        sb.append("\n🔍 SEARCH SUGGESTIONS:\n");
        sb.append("• Double-check the spelling of the name\n");
        sb.append("• Verify the customer ID format\n");
        sb.append("• Try searching with just first name or last name\n");
        sb.append("• Check if the customer exists in 'View All Customers'\n");
        sb.append("• Ensure the customer was properly registered\n\n");
        sb.append("💡 If you're sure the customer exists, they may need to be re-registered.");

        resultArea.setText(sb.toString());
    }

    private static void clearFields(JTextField idField, JTextField nameField) {
        idField.setText("Enter customer ID...");
        idField.setForeground(MUTED_TEXT);
        nameField.setText("Enter customer name...");
        nameField.setForeground(MUTED_TEXT);
    }

    private static String getEmergencyLevelDescription(int level) {
        switch (level) {
            case 1: return "Displacement/Asylum";
            case 2: return "Disabled people-can't work";
            case 3: return "Elderly";
            case 4: return "Family without breadwinner/Unemployment";

            default: return "Unknown level";
        }
    }
}