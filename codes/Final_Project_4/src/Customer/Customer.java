package Customer;

public class Customer {
    private String id;
    private String name;
    private String email;
    private int familySize;
    private int emergencyLevel;
    private String location;
    private String notes;

    //----------------------constructor with ID--------------------------
    public Customer(String id, String name, String email, int familySize,
                    int emergencyLevel, String location, String notes) {  /*we used this constructor in class Manager in function loadDataFromFile to copy the customers from the
                                                                            file to the hash table and also to give it the same ID*/
        this.id = id;
        this.name = name;
        this.email = email;
        this.familySize = familySize;
        this.emergencyLevel = emergencyLevel;
        this.location = location;
        this.notes = notes != null ? notes : "";
    }

    //----------------------constructor without ID (auto-generate)--------------------------
    public Customer(String name, String email, int familySize, int emergencyLevel, String location, String notes) {
        this.id = generateId();
        this.name = name;
        this.email = email;
        this.familySize = familySize;
        this.emergencyLevel = emergencyLevel;
        this.location = location;
        this.notes = notes != null ? notes : "";         /* condition ? value_if_true : value_if_false;
                                                       this is a ternary operator,(3 operations in one code line)*/
    }

    //--------method to generate unique ID-------------------------------
    //we chose this way to generate an id because it's the best way in java because any other functions may be limited with specific number or very complex
    private String generateId() {
        return "C" + System.currentTimeMillis();
    }

    //------------------------getters--------------------------------------------
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public int getFamilySize() {
        return familySize;
    }

    public int getEmergencyLevel() {
        return emergencyLevel;
    }

    public String getLocation() {
        return location;
    }

    public String getNotes() {
        return notes;
    }

    //---------------------- For repetition cases -------------------------------
    /**
     * Get emergency level description
     */
    public String getEmergencyLevelDescription() {
        switch (emergencyLevel) {
            case 1: return "Displacement/Asylum";
            case 2: return "Disabled people-can't work";
            case 3: return "Elderly";
            case 4: return "Family without breadwinner/Unemploymen";

            default: return "Unknown level";

        }
    }



}