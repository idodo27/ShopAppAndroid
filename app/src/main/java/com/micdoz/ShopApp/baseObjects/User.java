package com.micdoz.ShopApp.baseObjects;

/**
 * This class is used to store information about the user and retrieved from firebase as a Hashmap.
 */

public class User{
    /**
     * Class Variables
     */

    private String fName;
    private String lName;

    /**
     * Class constructor.
     * @param fName
     * @param lName
     */

    public User(String fName, String lName) {
        setfName(fName);
        setlName(lName);
    }

    /**
     * Getters
     * @return
     */

    public String getfName() {
        return fName;
    }
    public String getlName() {
        return lName;
    }

    /**
     * Setters
     * @param lName
     */

    public void setlName(String lName) {
        this.lName = lName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }
}