package Sorting;

import Customer.Customer;
import java.io.*;
import java.util.*;

/**
 * "EMERGENCY PRIORITY CHAIN ALGORITHM"
 * This implements a completely custom priority queue using a chain-based structure.
 * Each priority level has its own chain of customers, and we manually manage
 *
 * insertion, extraction, and priority ordering without using ANY Java built-ins.
 */
public class CustomerSorter {

    private static final Map<String, Integer> LOCATION_PRIORITY = new HashMap<>();
//The Map contains locations (Strings) as keys, and numbers (Integers) as values connected with each location.
    static {
        LOCATION_PRIORITY.put("Jerusalem", 1);
        LOCATION_PRIORITY.put("West Jerusalem", 2);
        LOCATION_PRIORITY.put("East Jerusalem", 3);
    }

    /**
     * CUSTOM PRIORITY QUEUE - HAND-BUILT STEP BY STEP
     * This is a completely original priority queue implementation using
     * a chain-based structure where each customer is a node in priority chains.
     */
    private static class EmergencyPriorityQueue {

        /**
         * STEP 1: Create Priority Node Structure
         * Each customer becomes a node with priority value and next pointer
         */
        private class PriorityNode {
            Customer customer;
            double priorityValue;
            PriorityNode next;

            public PriorityNode(Customer customer, double priorityValue) {
                this.customer = customer;
                this.priorityValue = priorityValue;
                this.next = null;
            }
        }

        /**
         * STEP 2: Create Priority Chain Structure
         * Each emergency level has its own chain of nodes
         */
        private class PriorityChain {
            PriorityNode head;    // First node (highest priority in this chain)
            PriorityNode tail;    // Last node (lowest priority in this chain)
            int chainSize;
            int emergencyLevel;

            public PriorityChain(int emergencyLevel) {
                this.head = null;
                this.tail = null;
                this.chainSize = 0;
                this.emergencyLevel = emergencyLevel;
            }

            /**
             * STEP 3: Manual Priority Insertion
             * Insert node in correct position based on priority value
             */
            public void insertByPriority(PriorityNode newNode) {
                // If chain is empty, make this the first node
                if (head == null) {
                    head = newNode;
                    tail = newNode;
                    chainSize++;
                    return;
                }

                // If new node has highest priority, insert at head
                if (newNode.priorityValue > head.priorityValue) {
                    newNode.next = head;
                    head = newNode;
                    chainSize++;
                    return;
                }

                // Find correct position by walking through chain
                PriorityNode current = head;
                PriorityNode previous = null;

                while (current != null && current.priorityValue >= newNode.priorityValue) {
                    previous = current;
                    current = current.next;
                }

                // Insert new node between previous and current
                newNode.next = current;
                if (previous != null) {
                    previous.next = newNode;
                }

                // Update tail if we inserted at end
                if (current == null) {
                    tail = newNode;
                }

                chainSize++;
            }

            /**
             * STEP 4: Manual Priority Extraction
             * Remove and return highest priority node
             */
            public PriorityNode extractHighestPriority() {
                if (head == null) {
                    return null;
                }

                PriorityNode highestPriorityNode = head;
                head = head.next;

                // Update tail if chain becomes empty
                if (head == null) {
                    tail = null;
                }

                chainSize--;

                return highestPriorityNode;
            }

            public boolean isEmpty() {
                return head == null;
            }

            public int getSize() {
                return chainSize;
            }

            /**
             * STEP 5: Chain Balancing
             * Redistribute nodes if chain becomes too unbalanced
             */
            public void balanceChain() {
                if (chainSize <= 5) return; // Only balance if chain is large

                // Split chain into two halves based on priority
                List<PriorityNode> highPriorityNodes = new ArrayList<>();
                List<PriorityNode> lowPriorityNodes = new ArrayList<>();

                // Calculate average priority
                double totalPriority = 0;
                PriorityNode current = head;
                while (current != null) {
                    totalPriority += current.priorityValue;
                    current = current.next;
                }
                double averagePriority = totalPriority / chainSize;

                // Split nodes based on average
                current = head;
                while (current != null) {
                    PriorityNode next = current.next;
                    current.next = null; // Disconnect from chain

                    if (current.priorityValue >= averagePriority) {
                        highPriorityNodes.add(current);
                    } else {
                        lowPriorityNodes.add(current);
                    }
                    current = next;
                }

                // Rebuild chain: high priority first, then low priority
                head = null;
                tail = null;
                chainSize = 0;

                // Re-insert high priority nodes
                for (PriorityNode node : highPriorityNodes) {
                    insertByPriority(node);
                }

                // Re-insert low priority nodes
                for (PriorityNode node : lowPriorityNodes) {
                    insertByPriority(node);
                }
            }
        }

        /**
         * STEP 6: Main Priority Queue Structure
         * Array of priority chains - one for each emergency level
         */
        private PriorityChain[] emergencyChains;
        private int totalCustomers;

        public EmergencyPriorityQueue() {
            // Create 4 priority chains for emergency levels 1-4
            emergencyChains = new PriorityChain[4];
            for (int i = 0; i < 4; i++) {
                emergencyChains[i] = new PriorityChain(i + 1);
            }
            totalCustomers = 0;
        }

        /**
         * STEP 7: Custom Priority Calculation
         * Calculate priority value using original formula
         */
        private double calculateCustomPriority(Customer customer) {
            double priority = 0;

            // Base priority from family size (larger families = higher priority)
            int familySize = customer.getFamilySize();
            priority += familySize * 20; // Base family priority

            // Add exponential boost for very large families
            if (familySize >= 5) {
                priority += Math.pow(familySize - 4, 2) * 10;
            }

            // Location priority multiplier
            int locationPriority = LOCATION_PRIORITY.getOrDefault(customer.getLocation(), 4);
            double locationMultiplier = (5.0 - locationPriority) / 4.0; // 0.25 to 1.0
            priority *= locationMultiplier;

            // Special bonuses
            if (customer.getLocation().equals("Jerusalem")) {
                priority += 30; // Jerusalem bonus
            }

            if (familySize >= 7) {
                priority += 40; // Large family bonus
            }

            // Add small random factor to break ties
            priority += Math.random() * 2;

            return priority;
        }

        /**
         * STEP 8: Priority Queue Insertion
         * Insert customer into appropriate emergency level chain
         */
        public void priorityInsert(Customer customer) {
            // Get emergency level (1-4)
            int emergencyLevel = customer.getEmergencyLevel();
            if (emergencyLevel < 1 || emergencyLevel > 4) {
                emergencyLevel = 4; // Default to lowest emergency priority
            }

            // Calculate priority value
            double priorityValue = calculateCustomPriority(customer);

            // Create new priority node
            PriorityNode newNode = new PriorityNode(customer, priorityValue);

            // Insert into appropriate chain
            PriorityChain targetChain = emergencyChains[emergencyLevel - 1];
            targetChain.insertByPriority(newNode);

            // Balance chain if it gets too large
            if (targetChain.getSize() > 8) {
                targetChain.balanceChain();
            }

            totalCustomers++;
        }

        /**
         * STEP 9: Priority Queue Extraction
         * Extract customer with highest overall priority
         */
        public Customer priorityExtract() {
            // Search through emergency levels 1-4 for highest priority customer
            for (int level = 0; level < 4; level++) {
                PriorityChain chain = emergencyChains[level];

                if (!chain.isEmpty()) {
                    PriorityNode extractedNode = chain.extractHighestPriority();
                    totalCustomers--;
                    return extractedNode.customer;
                }
            }

            return null; // Queue is empty
        }

        public boolean isEmpty() {
            return totalCustomers == 0;
        }

//        public int getTotalCustomers() {
//            return totalCustomers;
//        }
    }

    /**
     * Sort customers using Custom Priority Queue Algorithm
     */
    public static List<Customer> sortCustomers(List<Customer> customers) {
        // STEP 1: Create custom priority queue
        EmergencyPriorityQueue priorityQueue = new EmergencyPriorityQueue();

        // STEP 2: Priority insertion phase
        for (Customer customer : customers) {
            priorityQueue.priorityInsert(customer);
        }

        // STEP 3: Priority extraction phase
        List<Customer> sortedCustomers = new ArrayList<>();

        while (!priorityQueue.isEmpty()) {
            Customer customer = priorityQueue.priorityExtract();
            if (customer != null) {
                sortedCustomers.add(customer);
            }
        }

        return sortedCustomers;
    }

    /**
     * Sort and save customers using custom priority queue
     */
    public static boolean sortAndSaveToFile(String filename, Hashtable<String, Customer> customerTable) {
        try {
            List<Customer> customers = new ArrayList<>(customerTable.values());
            List<Customer> sortedCustomers = sortCustomers(customers);

            // Create backup
            File originalFile = new File(filename);
            if (originalFile.exists()) {
                File backupFile = new File(filename + ".backup");
                try (FileInputStream fis = new FileInputStream(originalFile);
                     FileOutputStream fos = new FileOutputStream(backupFile)) {
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = fis.read(buffer)) > 0) {
                        fos.write(buffer, 0, length);
                    }
                }
            }

            // Write sorted data
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
                for (Customer customer : sortedCustomers) {
                    writer.write(formatCustomerForFile(customer));
                    writer.newLine();
                }
            }

            // Update hashtable
            customerTable.clear();
            for (Customer customer : sortedCustomers) {
                customerTable.put(customer.getId(), customer);
            }

            return true;

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Insert customer using custom priority queue
     */
    public static boolean insertCustomerInSortedOrder(Customer newCustomer, String filename,
                                                      Hashtable<String, Customer> customerTable) {
        try {
            customerTable.put(newCustomer.getId(), newCustomer);
            List<Customer> allCustomers = new ArrayList<>(customerTable.values());
            List<Customer> sortedCustomers = sortCustomers(allCustomers);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
                for (Customer customer : sortedCustomers) {
                    writer.write(formatCustomerForFile(customer));
                    writer.newLine();
                }
            }

            customerTable.clear();
            for (Customer customer : sortedCustomers) {
                customerTable.put(customer.getId(), customer);
            }

            return true;

        } catch (IOException e) {
            customerTable.remove(newCustomer.getId());
            return false;
        }
    }

    private static String formatCustomerForFile(Customer customer) {
        return customer.getId() + ", " +
                customer.getName() + ", " +
                customer.getEmail() + ", " +
                customer.getFamilySize() + ", " +
                customer.getEmergencyLevel() + ", " +
                customer.getLocation() + ", " +
                customer.getNotes();
    }
}