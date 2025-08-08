package Operations;

import Management.CustomerManagementSystem;
import Management.CustomerManager;
import VerificationSystem.EmailSender;
import Customer.Customer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class AddCustomer {
    private static JFrame frame;

    //This line is used to ensure that the function is called and to use the thread to avoid facing any issues in the interface display//
    public static void main(String[] args) {
        //invokeLater ensures that the window launches at the correct time within the Swing environment.
        SwingUtilities.invokeLater(() -> createAddCustomerWindow());
    }
//-------------------------------------------------------------------------------
    private static void createAddCustomerWindow() {
        if (frame != null) {
            frame.setVisible(true);
            frame.toFront();
            return;
        }


        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);   // To check if the email is valid using the emailRegex //

        String[] locations = {"Jerusalem", "East Jerusalem", "West Jerusalem"};
        String[] priorities = {
                "1 - Displacement/Asylum",
                "2 - Disabled people-can't work",
                "3 - Elderly",
                "4 - Family without breadwinner/Unemployment",
        };

        frame = new JFrame("Add New Customer");
        frame.setSize(500, 700);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());  /*Purpose:
                                               BorderLayout helps organize elements logically and ensures that buttons always remain visible at the bottom of the window,
                                               while content in the middle can expand or collapse as needed.*/

        try {
            ImageIcon frameIcon = new ImageIcon("C:\\Users\\dell2024\\IdeaProjects\\Final_Project_4\\src\\Images\\logo.jpeg");
            frame.setIconImage(frameIcon.getImage());
        } catch (Exception ex) {    //we used catch in order to catch if there are any problems
            System.out.println("Could not load frame icon: " + ex.getMessage());
        }


        frame.addWindowListener(new WindowAdapter() {
            @Override  //استدعاء دالة من الكلاس الي قبلو
            public void windowClosed(WindowEvent e) {
                frame = null;  // in order to delete المرجع from the memory
                CustomerManagementSystem.onChildWindowClosed();  //if there are any windows are open
            }
        });

        // Header
        final Color PRIMARY_COLOR = new Color(184, 134, 11); //final: can't be modified
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Add New Order");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20)); //Font : גופן
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);

        // Form Panel - Using simple GridLayout with SCROLL PANE, contains components in a table design
        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); //borders: גבולות

        // Create simple form fields
        JTextField nameField = new JTextField(20);
        JTextField emailField = new JTextField(20);
        JTextField familyField = new JTextField(20);
        JComboBox<String> locationBox = new JComboBox<>(locations); //<> --> array list
        JComboBox<String> priorityBox = new JComboBox<>(priorities);
        JTextField notesField = new JTextField(20);

        // Add labels and fields
        formPanel.add(new JLabel("Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);
        formPanel.add(new JLabel("Family Size:"));
        formPanel.add(familyField);
        formPanel.add(new JLabel("Location:"));
        formPanel.add(locationBox);
        formPanel.add(new JLabel("Priority:"));
        formPanel.add(priorityBox);
        formPanel.add(new JLabel("Notes:"));
        formPanel.add(notesField);

        // PUT FORM IN SCROLL PANE so it can never push buttons off screen
        JScrollPane formScrollPane = new JScrollPane(formPanel);
        formScrollPane.setBorder(null);
        formScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); //to move up & down or right&left
        formScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // Create FIXED BOTTOM PANEL for buttons - ALWAYS VISIBLE
        JPanel bottomContainer = new JPanel(new BorderLayout());
        bottomContainer.setBackground(Color.LIGHT_GRAY);

        // Create BEAUTIFUL, CREATIVE button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(new Color(52, 58, 64)); // Dark elegant background
        buttonPanel.setBorder(BorderFactory.createCompoundBorder( //to create double borders
                BorderFactory.createMatteBorder(3, 0, 0, 0, PRIMARY_COLOR),
                BorderFactory.createEmptyBorder(25, 20, 25, 20)
        ));

        // CREATE STUNNING BUTTONS WITH GRADIENT-LIKE EFFECTS
        JButton addButton = new JButton(" ADD");
        addButton.setPreferredSize(new Dimension(180, 50));
        addButton.setBackground(Color.WHITE);
        addButton.setForeground(Color.BLACK);
        addButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        addButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 2),
                BorderFactory.createEmptyBorder(8, 16, 8, 16)
        ));
        addButton.setFocusPainted(false); // don't show the blue borders around the button when touch it
        addButton.setCursor(new Cursor(Cursor.HAND_CURSOR));  // to change the shape of the mouse into a hand when touch the button

        JButton clearButton = new JButton("CLEAR FORM");
        clearButton.setPreferredSize(new Dimension(180, 50));
        clearButton.setBackground(new Color(108, 117, 125)); // Modern gray
        clearButton.setForeground(Color.BLACK); // BLACK text on gray
        clearButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        clearButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(73, 80, 87), 2),
                BorderFactory.createEmptyBorder(8, 16, 8, 16)
        ));
        clearButton.setFocusPainted(false);
        clearButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add hover effects
        addButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                addButton.setBackground(Color.yellow);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                addButton.setBackground(Color.WHITE);
            }
        });

        clearButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                clearButton.setBackground(Color.yellow);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                clearButton.setBackground(new Color(108, 117, 125)); // Original gray
            }
        });

        // ADD BUTTONS TO PANEL
        buttonPanel.add(addButton);
        buttonPanel.add(Box.createHorizontalStrut(20)); // Space between buttons
        buttonPanel.add(clearButton);

        // Status label - BEAUTIFUL and MODERN
        JLabel statusLabel = new JLabel("Ready to add order", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        statusLabel.setForeground(new Color(173, 181, 189)); // Light text
        statusLabel.setBorder(BorderFactory.createEmptyBorder(15, 10, 20, 10));
        statusLabel.setBackground(new Color(52, 58, 64)); // Same as button panel
        statusLabel.setOpaque(true);

        // Combine buttons and status - FIXED at bottom
        bottomContainer.add(buttonPanel, BorderLayout.CENTER);
        bottomContainer.add(statusLabel, BorderLayout.SOUTH);

        CustomerManager manager = CustomerManager.getInstance(); /*It ensures that all operations (adding, deleting, modifying clients) occur on the same database/list, maintaining data sequencing and integrity.
                                                                 It is a design pattern that ensures that a class has only one copy in the entire program and provides a common access point to this copy.
                                                                 //to use the same instance of manager because whole the classes works with one manager*/

        // Add Customer button action
        addButton.addActionListener(_ -> {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String familyText = familyField.getText().trim();
            String location = (String) locationBox.getSelectedItem();
            String levelStr = (String) priorityBox.getSelectedItem();
            String notes = notesField.getText().trim();

            // Simple validation
            if (name.isEmpty()) {
                statusLabel.setText("Please enter name");
                statusLabel.setForeground(Color.RED);
                return;
            }

            if (email.isEmpty()) {
                statusLabel.setText("Please enter email");
                statusLabel.setForeground(Color.RED);
                return;
            }

            if (!isValidEmail(email, pattern)) {
                statusLabel.setText("Please enter valid email");
                statusLabel.setForeground(Color.RED);
                return;
            }

            if (familyText.isEmpty()) {
                statusLabel.setText("Please enter family size");
                statusLabel.setForeground(Color.RED);
                return;
            }

            try {
                int familySize = Integer.parseInt(familyText);

                if (familySize < 1) {
                    statusLabel.setText("Family size must be at least 1");
                    statusLabel.setForeground(Color.RED);
                    return;
                }

                if (manager.findCustomerByNameAndEmail(name, email)) {
                    statusLabel.setText("Customer already exists");
                    statusLabel.setForeground(Color.RED);
                    return;
                }

                String levelChar = levelStr.charAt(0) + "";
                int emergencyLevel = Integer.parseInt(levelChar);

                Customer customer = new Customer(name, email, familySize, emergencyLevel, location, notes);

                if (manager.addCustomer(customer)) {
                    statusLabel.setText("New order added successfully! ");
                    statusLabel.setForeground(Color.GREEN);

                    int queuePosition = manager.getCustomerPositionInQueue(customer.getId());
                    if (queuePosition > 0) {
                        EmailSender.sendQueuePositionEmail(
                                customer.getEmail(),
                                customer.getName(),
                                queuePosition,
                                manager.getCustomerCount()
                        );
                    } else {
                        System.err.println("Error: Could not determine queue position for customer " + customer.getId());
                    }

                    // Clear form
                    nameField.setText("");
                    emailField.setText("");
                    familyField.setText("");
                    notesField.setText("");
                    locationBox.setSelectedIndex(0);
                    priorityBox.setSelectedIndex(0);

                    JOptionPane.showMessageDialog(frame,      // رسالة منبثقة
                              customer.getName() + "'s order added successfully!\nID: " + customer.getId(),
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    statusLabel.setText("Error adding customer");
                    statusLabel.setForeground(Color.RED);
                }

            } catch (NumberFormatException ex) {
                statusLabel.setText("Family size must be a number");
                statusLabel.setForeground(Color.RED);
            }
        });

        // Clear button action
        clearButton.addActionListener(_ -> {
            String levelStr = (String) priorityBox.getSelectedItem();
            String levelChar = levelStr.charAt(0) + "";
            nameField.setText("");
            emailField.setText("");
            familyField.setText("");
            notesField.setText("");
            locationBox.setSelectedIndex(0);
            priorityBox.setSelectedIndex(0);
            statusLabel.setText("Form cleared");
            statusLabel.setForeground(Color.BLACK);
        });

        // LAYOUT - GUARANTEES BUTTONS ARE ALWAYS VISIBLE
        frame.add(headerPanel, BorderLayout.NORTH);        // Header at top
        frame.add(formScrollPane, BorderLayout.CENTER);    // Scrollable form in center
        frame.add(bottomContainer, BorderLayout.SOUTH);    // FIXED buttons at bottom

        frame.setLocationRelativeTo(null);  // makes the frame to be in the middle of the screen
        frame.setVisible(true); //to show the frame

        // Force repaint to ensure everything shows
        frame.revalidate(); //to ensure that the components in the right place
        frame.repaint(); //to updates the GUI(colors, texts, ..)

        // Debug: Print to console
        System.out.println("Add Customer window created!");
        System.out.println("Button panel added: " + (buttonPanel != null));
        System.out.println("Add button created: " + (addButton != null));
    }

    private static boolean isValidEmail(String email, Pattern pattern) {
        if (email == null || email.trim().isEmpty()) { //null, to check if the email doesn't point to anything, trim().isEmpty(), to check if the text is empty
            return false;
        }

        //returns true or false
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


}