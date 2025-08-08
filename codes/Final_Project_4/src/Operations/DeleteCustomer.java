package Operations;

import Customer.*;
import Management.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class DeleteCustomer {
    private static JFrame frame;

    // Beautiful Color Scheme
    private static final Color PRIMARY_COLOR = new Color(226, 183, 74);
    private static final Color SUCCESS_COLOR = new Color(40, 167, 69);
    private static final Color DANGER_COLOR = new Color(220, 53, 69);
    private static final Color BACKGROUND_COLOR = new Color(248, 249, 250);
    private static final Color CARD_COLOR = Color.WHITE;
    private static final Color TEXT_COLOR = new Color(33, 37, 41);
    private static final Color MUTED_TEXT = new Color(108, 117, 125);
    private static final Color BORDER_COLOR = new Color(222, 226, 230);

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createDeleteCustomerWindow());
    }

    private static void createDeleteCustomerWindow() {
        if (frame != null) {
            frame.setVisible(true);
            frame.toFront();
            return;
        }

        frame = new JFrame("Delete Customer");
        frame.setSize(650, 600);
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

        // Customer.Main content panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Search section
        JPanel searchSection = createSearchSection();
        JTextField idField = createStyledTextField("Enter customer ID to delete...");
        addSearchField(searchSection, "[ID] Customer ID", idField);

        // Buttons panel
        JPanel buttonPanel = createButtonPanel();
        JButton searchButton = createStyledButton("Find Customer", MUTED_TEXT, "?");
        JButton deleteButton = createStyledButton("Delete Customer", DANGER_COLOR, "X");
        JButton clearButton = createStyledButton("Clear", MUTED_TEXT, "C");

        buttonPanel.add(searchButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(deleteButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(clearButton);

        // Customer preview panel
        JPanel previewSection = createPreviewSection();
        JTextArea previewArea = createStyledTextArea();
        JScrollPane previewScroll = new JScrollPane(previewArea);
        previewScroll.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                "Customer Information",
                0, 0,                  //Title alignment (horizontally and vertically)
                new Font("Segoe UI", Font.BOLD, 14),
                TEXT_COLOR
        ));
        previewScroll.setPreferredSize(new Dimension(0, 200));
        previewSection.add(previewScroll, BorderLayout.CENTER);

        // Status panel
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBackground(BACKGROUND_COLOR);
        statusPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        JLabel statusLabel = new JLabel("Enter a customer ID to begin deletion process");
        statusLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        statusLabel.setForeground(MUTED_TEXT);
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusPanel.add(statusLabel);

        // Layout main panel
        JPanel topSection = new JPanel(new BorderLayout());
        topSection.setBackground(BACKGROUND_COLOR);
        topSection.add(searchSection, BorderLayout.NORTH);
        topSection.add(buttonPanel, BorderLayout.CENTER);

        mainPanel.add(topSection, BorderLayout.NORTH);
        mainPanel.add(previewSection, BorderLayout.CENTER);
        mainPanel.add(statusPanel, BorderLayout.SOUTH);

        CustomerManager manager = CustomerManager.getInstance();

        // Initially disable delete button
        deleteButton.setEnabled(false);       // it is false until we do the search operation

        // Search button action
        searchButton.addActionListener(_ -> {
            String targetID = idField.getText().trim();

            if (targetID.isEmpty() || targetID.equals("Enter customer ID to delete...")) {
                statusLabel.setText("[ERROR] Please enter a customer ID");
                statusLabel.setForeground(DANGER_COLOR);
                previewArea.setText("");
                deleteButton.setEnabled(false);
                return;
            }

            Customer customer = manager.findCustomerById(targetID);
            if (customer != null) {
                displayCustomerPreview(previewArea, customer);
                statusLabel.setText("[FOUND] Customer found - Review information before deleting");
                statusLabel.setForeground(SUCCESS_COLOR);
                deleteButton.setEnabled(true);
            } else {
                previewArea.setText("[NOT FOUND] Customer Not Found\n" +
                        "===============================================\n" +
                        "No customer found with ID: " + targetID + "\n\n" +
                        "SUGGESTIONS:\n" +
                        "• Double-check the customer ID\n" +
                        "• Verify the customer exists in the system\n" +
                        "• Use 'View All Customers' to browse available IDs\n" +
                        "• Contact administrator if customer should exist");
                statusLabel.setText("[ERROR] Customer not found - Please check the ID");
                statusLabel.setForeground(DANGER_COLOR);
                deleteButton.setEnabled(false);
            }
        });

        // Delete button action
        deleteButton.addActionListener(_ -> {
            String targetID = idField.getText().trim();

            if (targetID.isEmpty() || targetID.equals("Enter customer ID to delete...")) {
                statusLabel.setText("[ERROR] Please enter a customer ID");
                statusLabel.setForeground(DANGER_COLOR);
                return;
            }

            Customer customer = manager.findCustomerById(targetID);
            if (customer == null) {
                statusLabel.setText("[ERROR] Customer not found - Please search first");
                statusLabel.setForeground(DANGER_COLOR);
                return;
            }

            // Show confirmation dialog with customer details
            int choice = showConfirmationDialog(customer);

            if (choice == JOptionPane.YES_OPTION) {
                if (manager.deleteCustomer(targetID)) {
                    statusLabel.setText("[OK] Customer deleted successfully");
                    statusLabel.setForeground(SUCCESS_COLOR);

                    // Clear form
                    clearForm(idField, previewArea, deleteButton);

                    // Show success message
                    showSuccessDialog(customer);
                    idField.requestFocus();
                } else {
                    statusLabel.setText("[ERROR] Could not delete customer - Please try again");
                    statusLabel.setForeground(DANGER_COLOR);
                }
            }
        });

        // Clear button action
        clearButton.addActionListener(_ -> {
            clearForm(idField, previewArea, deleteButton);
            statusLabel.setText("Form cleared - Enter customer ID to begin");
            statusLabel.setForeground(MUTED_TEXT);
            idField.requestFocus();
        });

        // Enter key support for search
        idField.addActionListener(_ -> searchButton.doClick());
        frame.getRootPane().setDefaultButton(searchButton);

        // Initial content
        previewArea.setText("Customer Preview\n" +
                "===============================================\n" +
                "Enter a customer ID above and click 'Find Customer'\n" +
                "to preview customer information before deletion.\n\n" +
                "SAFETY FEATURES:\n" +
                "• Customer information preview before deletion\n" +
                "• Confirmation dialog with customer details\n" +
                "• Automatic form clearing after deletion\n" +
                "• Cannot delete without finding customer first\n\n" +
                "WARNING: Deletion is permanent and cannot be undone!");

        frame.add(headerPanel, BorderLayout.NORTH);
        frame.add(mainPanel, BorderLayout.CENTER);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        SwingUtilities.invokeLater(() -> idField.requestFocus());
    }
    //GUI header in the deletion gui
    private static JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(DANGER_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        JLabel titleLabel = new JLabel("X Delete Customer");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);

        JLabel subtitleLabel = new JLabel("Remove customers from the system (PERMANENT)");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(255, 255, 255, 180));

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(DANGER_COLOR);
        titlePanel.add(titleLabel, BorderLayout.NORTH);
        titlePanel.add(subtitleLabel, BorderLayout.SOUTH);

        JLabel warningLabel = new JLabel("! CAUTION: This action cannot be undone");
        warningLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        warningLabel.setForeground(new Color(255, 255, 255, 220));
        warningLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        headerPanel.add(titlePanel, BorderLayout.WEST);
        headerPanel.add(warningLabel, BorderLayout.EAST);

        return headerPanel;
    }

    private static JPanel createSearchSection() {
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.Y_AXIS));
        searchPanel.setBackground(CARD_COLOR);
        searchPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(BORDER_COLOR, 1),
                        "Step 1: Find Customer",
                        0, 0,
                        new Font("Segoe UI", Font.BOLD, 14),
                        TEXT_COLOR
                ),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        return searchPanel;
    }

    private static JPanel createPreviewSection() {
        JPanel previewPanel = new JPanel(new BorderLayout());
        previewPanel.setBackground(BACKGROUND_COLOR);
        previewPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        return previewPanel;
    }

    private static JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        return buttonPanel;
    }

    private static JTextField createStyledTextField(String placeholder) {
        JTextField field = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);

                if (hasFocus()) {
                    g2.setColor(PRIMARY_COLOR);
                    g2.setStroke(new BasicStroke(2));
                } else {
                    g2.setColor(BORDER_COLOR);
                    g2.setStroke(new BasicStroke(1));
                }
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);

                g2.dispose();
                super.paintComponent(g);
            }
        };

        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(0, 40));
        field.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        field.setBackground(Color.WHITE);
        field.setForeground(TEXT_COLOR);
        field.setText(placeholder);
        field.setForeground(MUTED_TEXT);

        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(TEXT_COLOR);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
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
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
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
                } else if (!isEnabled()) {
                    bgColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), 100);  // a: alpha غير شفاف255 , 0 شفاف  تماما,  transparent
                }

                g2.setColor(bgColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

                g2.setColor(isEnabled() ? Color.WHITE : new Color(255, 255, 255, 150)); // if it is enabled then the color will be white, if not the color will be another one
                g2.setFont(getFont());                                                              // ? : ternary operator (it is a short from if statement)
                FontMetrics fm = g2.getFontMetrics();
                int stringWidth = fm.stringWidth(getText());
                int stringHeight = fm.getAscent();
                g2.drawString(getText(), (getWidth() - stringWidth) / 2, (getHeight() + stringHeight) / 2 - 2);

                g2.dispose();
            }
        };

        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(140, 40));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return button;
    }

    private static void addSearchField(JPanel parent, String label, JTextField field) {
        JPanel fieldPanel = new JPanel(new BorderLayout());
        fieldPanel.setBackground(CARD_COLOR);
        fieldPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JLabel fieldLabel = new JLabel(label);
        fieldLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        fieldLabel.setForeground(TEXT_COLOR);
        fieldLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));

        fieldPanel.add(fieldLabel, BorderLayout.NORTH);
        fieldPanel.add(field, BorderLayout.CENTER);

        parent.add(fieldPanel);
    }

    private static void displayCustomerPreview(JTextArea previewArea, Customer customer) {
        StringBuilder sb = new StringBuilder();
        sb.append("[PREVIEW] Customer Information\n");
        sb.append("===============================================\n");
        sb.append("The following customer will be PERMANENTLY deleted:\n\n");

        sb.append("ID: ").append(customer.getId()).append("\n");
        sb.append("Name: ").append(customer.getName()).append("\n");
        sb.append("Email: ").append(customer.getEmail()).append("\n");
        sb.append("Family Size: ").append(customer.getFamilySize()).append(" members\n");
        sb.append("Emergency Level: ").append(customer.getEmergencyLevel());
        sb.append(" (").append(getEmergencyLevelDescription(customer.getEmergencyLevel())).append(")\n");
        sb.append("Location: ").append(customer.getLocation()).append("\n");
        sb.append("Notes: ").append(customer.getNotes().isEmpty() ? "No additional notes" : customer.getNotes()).append("\n");

        sb.append("===============================================\n");
        sb.append("WARNING: This deletion is PERMANENT!\n");
        sb.append("Click 'Delete Customer' to proceed with deletion.\n");
        sb.append("A confirmation dialog will appear before final deletion.");

        previewArea.setText(sb.toString());
    }

    private static int showConfirmationDialog(Customer customer) {
        String message = String.format(
                "PERMANENT DELETION CONFIRMATION\n\n" +
                        "Are you absolutely sure you want to delete this customer?\n\n" +
                        "Customer Details:\n" +
                        "Name: %s\n" +
                        "Email: %s\n" +
                        "ID: %s\n" +
                        "Family Size: %d members\n" +
                        "Emergency Level: %d\n\n" +
                        "WARNING: This action CANNOT be undone!\n" +
                        "The customer will be permanently removed from the system.",
                customer.getName(),
                customer.getEmail(),
                customer.getId(),
                customer.getFamilySize(),
                customer.getEmergencyLevel()
        );

        return JOptionPane.showConfirmDialog(
                frame,
                message,
                "Confirm Permanent Deletion",
                JOptionPane.YES_NO_OPTION,       //confirming deleting
                JOptionPane.WARNING_MESSAGE      //showing a warning symbol
        );
    }

    private static void showSuccessDialog(Customer customer) {
        String message = String.format(
                "Customer Successfully Deleted\n\n" +
                        "The following customer has been permanently removed:\n\n" +
                        "Name: %s\n" +
                        "ID: %s\n\n" +
                        "The customer data has been removed from the system.",
                customer.getName(),
                customer.getId()
        );

        JOptionPane.showMessageDialog(frame, message, "Deletion Complete", JOptionPane.INFORMATION_MESSAGE);
    }

    private static void clearForm(JTextField idField, JTextArea previewArea, JButton deleteButton) {
        idField.setText("Enter customer ID to delete...");
        idField.setForeground(MUTED_TEXT);
        previewArea.setText("Customer Preview\n" +
                "===============================================\n" +
                "Enter a customer ID above and click 'Find Customer'\n" +
                "to preview customer information before deletion.\n\n" +
                "SAFETY FEATURES:\n" +
                "• Customer information preview before deletion\n" +
                "• Confirmation dialog with customer details\n" +
                "• Automatic form clearing after deletion\n" +
                "• Cannot delete without finding customer first\n\n" +
                "WARNING: Deletion is permanent and cannot be undone!");
        deleteButton.setEnabled(false);
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