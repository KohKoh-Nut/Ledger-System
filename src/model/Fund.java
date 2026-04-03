package model;

public class Fund {
    // ----- ATTRIBUTE -----
    private final String id;
    private final String name;

    // ----- CONSTRUCTOR -----
    private Fund(String name) {
        this.id = IdManager.generateUniqueId();
        this.name = name;
    }

    // return new fund
    public static Fund of(String name) {
        return new Fund(name);
    }
}
