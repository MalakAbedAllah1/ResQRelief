package Management;


import Customer.*;
import Sorting.*;


import java.io.*;
import java.util.Hashtable;
import java.util.ArrayList;
import java.util.List;

/**
 * Management.CustomerManager class to handle all operations related to customers
 * Uses a Hashtable for in-memory operations and syncs with file storage
 * Includes automatic sorting functionality
 */
public class CustomerManager {
    private static final String FILE_PATH = "C:\\Users\\dell2024\\IdeaProjects\\Final_Project_4\\src\\DataBase\\customer.txt";
    private Hashtable<String, Customer> customerTable;
    private List<CustomerUpdateListener> listeners;      //This is a list of objects that track any changes in customer data. هذه قائمة من الكائنات التي تتابع أي تغييرات في بيانات العملاء

    // Singleton instance-one component, we use it to for not making many versions
    private static CustomerManager instance;

    /**
     * Interface for update notifications
     */
    public interface CustomerUpdateListener {   // we use it for changes, interface: A template specifies what a class should do, without specifying how.
        void onCustomerAdded(Customer customer);  //we used void just to tell the system that made a change (add/delete/..)
        void onCustomerDeleted(String customerId);
    }

    /**
     * Get singleton instance of Management.CustomerManager
     */
    public static CustomerManager getInstance() {
        if (instance == null) {
            instance = new CustomerManager();
        }
        return instance;
    }

    /**
     * Private constructor - loads data from file into hashtable
     */
    private CustomerManager() {
        customerTable = new Hashtable<>();
        listeners = new ArrayList<>();
        loadDataFromFile();
    }

    /**
     * Add update listener
     * 1. To separate tasks.
     * The code that adds or removes clients doesn't know or care who is affected. Objects that do care are registered as listeners and are notified when a change occurs.
     * 2. To react to changes:
     * For example, when adding or deleting a client:
     * Updating the UI.
     * Saving the change to a file.
     * All of these can be executed automatically via a listener.
     */
    public void addUpdateListener(CustomerUpdateListener listener) {  //we made a new listeners to add it to the list
        if (!listeners.contains(listener)) {                          //before adding this new listeners, we check if this listeners list doesn't contain it
            listeners.add(listener);
        }
    }



    /**
     * Notify listeners of customer addition
     */
    private void notifyCustomerAdded(Customer customer) {
        for (CustomerUpdateListener listener : listeners) {
            try {
                listener.onCustomerAdded(customer);
            } catch (Exception e) {
                System.err.println("Error notifying listener: " + e.getMessage());
            }
        }
    }

    /**
     * Notify listeners of customer deletion
     */
    private void notifyCustomerDeleted(String customerId) {
        for (CustomerUpdateListener listener : listeners) {
            try {
                listener.onCustomerDeleted(customerId);
            } catch (Exception e) {
                System.err.println("Error notifying listener: " + e.getMessage());
            }
        }
    }




    /**
     * Load customer data from file into hashtable
     */
    private void loadDataFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            System.out.println("Customer data file '" + FILE_PATH + "' not found. Starting with empty database.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            int lineNumber = 0;
            int loadedCount = 0;

            System.out.println("Loading data from: " + FILE_PATH);

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                line = line.trim();

                if (line.isEmpty()) {
                    continue;
                }

                try {
                    String[] parts = line.split(", ");

                    if (parts.length >= 6) {
                        String id = parts[0].trim();
                        String name = parts[1].trim();
                        String email = parts[2].trim();
                        int familySize = Integer.parseInt(parts[3].trim());
                        int emergencyLevel = Integer.parseInt(parts[4].trim());
                        String location = parts[5].trim();

                        // Handle notes - might contain commas or be empty
                        String notes = "";
                        if (parts.length > 6) {
                            StringBuilder notesBuilder = new StringBuilder();
                            for (int i = 6; i < parts.length; i++) {  //i=6 because the notes is in the sixth part of out parts array
                                if (i > 6) notesBuilder.append(", ");  //append is for splitting the notes if there are more than one note
                                notesBuilder.append(parts[i].trim());
                            }
                            notes = notesBuilder.toString();
                        }

                        /*we used this constructonr to copy the customers from the
                         file to the hash table and also to give it the same ID*/
                        Customer customer = new Customer(id, name, email, familySize, emergencyLevel, location, notes);
                        customerTable.put(id, customer);
                        loadedCount++;

                        System.out.println("Loaded: " + name + " (ID: " + id + ")");

                    } else {
                        System.err.println("Invalid data format at line " + lineNumber + " (not enough fields): " + line);
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Invalid number format at line " + lineNumber + ": " + line);
                } catch (Exception e) {
                    System.err.println("Error parsing line " + lineNumber + ": " + line);
                }
            }

            System.out.println("Successfully loaded " + loadedCount + " customers from " + lineNumber + " lines.");

        } catch (IOException e) {
            System.err.println("Error reading customer data file '" + FILE_PATH + "': " + e.getMessage());
        }
    }

    /**
     * Save all customer data from hashtable back to file (with sorting)
     */
    public boolean saveDataToFile() {
        return CustomerSorter.sortAndSaveToFile(FILE_PATH, customerTable);
    }


    public boolean addCustomer(Customer customer) {
        if (customerTable.containsKey(customer.getId())) {
            return false; // Customer with this ID already exists
        }

        // Use Sorting.CustomerSorter to insert in correct position (main file)
        boolean successMain = CustomerSorter.insertCustomerInSortedOrder(customer, FILE_PATH, customerTable);

        // Also insert in backup file (without affecting hashtable again)
        boolean successBackup = CustomerSorter.insertCustomerInSortedOrder(customer, FILE_PATH + ".backup", new Hashtable<>(customerTable));

        if (successMain && successBackup) {
            notifyCustomerAdded(customer);
            return true;
        } else {
            return false;
        }
    }


    /**
     * Find customer by ID
     */
    public Customer findCustomerById(String id) {
        return customerTable.get(id);
    }

    /**
     * Find customer by name (returns first match)
     */
    public Customer findCustomerByName(String name) {
        for (Customer customer : customerTable.values()) {
            if (customer.getName().equalsIgnoreCase(name)) {
                return customer;
            }
        }
        return null;
    }

    /**
     * Find customer by ID and name
     */
    public Customer findCustomer(String id, String name) {
        Customer customer = customerTable.get(id);
        if (customer != null && customer.getName().equalsIgnoreCase(name)) {
            return customer;
        }
        return null;
    }

    /**
     * Delete customer by id and maintain sorted order
     */
    public boolean deleteCustomer(String id) {
        if (!customerTable.containsKey(id)) {
            return false;
        }

        Customer removedCustomer = customerTable.remove(id);

        if (saveDataToFile()) { // This will automatically sort and save
            notifyCustomerDeleted(id);  //to notify the listeners about this change "Deletion"
            return true;
        } else {
            // Rollback (puts the customer back again to file) if save failed in the file for not deleting it from the memory
            customerTable.put(id, removedCustomer);
            return false;
        }
    }


    /**
     * Get all customers in sorted order
     */
    public List<Customer> getAllCustomers() {
        return CustomerSorter.sortCustomers(new ArrayList<>(customerTable.values()));
    }

    /**
     * Get customer count
     */
    public int getCustomerCount() {
        return customerTable.size();
    }

    /**
     * Check if customer already exists by name and email
     */
    public boolean findCustomerByNameAndEmail(String name, String email) {
        for (Customer customer : customerTable.values()) {
            if (customer.getName().equalsIgnoreCase(name) &&
                    customer.getEmail().equalsIgnoreCase(email)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get customer's position in the sorted queue (1-based index)
     */
    public int getCustomerPositionInQueue(String customerId) {
        List<Customer> sortedCustomers = getAllCustomers(); // This already returns sorted list
        for (int i = 0; i < sortedCustomers.size(); i++) {
            if (sortedCustomers.get(i).getId().equals(customerId)) {
                return i + 1; // Return 1-based position
            }
        }
        return -1; // Customer not found
    }
}