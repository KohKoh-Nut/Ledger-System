package model;

public class Transaction {
    // ----- ATTRIBUTE -----
    private final String id;
    private final double amount;
    private final Date timestamp;

    // ----- CONSTRUCTOR -----
    private Transaction(String id, double amount, Date timestamp) {
        this.id = id;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    private static Transaction of(String id, double amount, Date timestamp) {
        return new Transaction(id, amount, timestamp);
    }

    public static Transaction of(String id, double amount, int day, int month, int year) {
        try {
            return of(id, amount, Date.of(day, month, year));
        } catch (InvalidDateException e) {
            throw new RuntimeException(e);
        }
    }

    public static Transaction current(String id, double amount) {
        return of(id, amount, Date.current());
    }

    // ----- TRANSACTION -----
}
