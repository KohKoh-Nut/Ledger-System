package model.transaction;

import model.Category;
import model.Date;
import model.IdManager;
import model.InvalidDateException;

/**
 * Abstract base for all financial records (Income, Expense).
 * Centralizes common attributes like amount, category, and unique tracking IDs.
 */
public abstract class Transaction {

    private final String id;
    private final double amount;
    private final Date timestamp;
    private final Category category;
    private final String description;

    /**
     * Standard constructor for subclasses. Generates a unique ID automatically.
     */
    protected Transaction(double amount, Date timestamp, Category category, String description) {
        this.id = IdManager.generateUniqueId();
        this.amount = amount;
        this.timestamp = timestamp;
        this.category = category;
        this.description = description;
    }

    /**
     * Generic factory helper to create specific transaction types with a custom date.
     * @param creator A functional interface (lambda) that calls the specific subclass constructor.
     * @throws RuntimeException if the date provided is invalid.
     */
    protected static <T extends Transaction> T of(double amount, int day, int month, int year,
                                                  Category category, String description,
                                                  TransactionCreator<T> creator) {
        try {
            return creator.create(amount, Date.of(day, month, year), category, description);
        } catch (InvalidDateException e) {
            // Unrecoverable at this level; promotes to runtime exception
            throw new RuntimeException(e);
        }
    }

    /**
     * Generic factory helper to create specific transaction types using the current system date.
     */
    protected static <T extends Transaction> T current(double amount, Category category,
                                                       String description, TransactionCreator<T> creator) {
        return creator.create(amount, Date.current(), category, description);
    }

    @Override
    public String toString() {
        // Updated to %.2f for standard currency formatting
        return String.format("[%s] %s | %.2f | %s (Ref: %s)",
                timestamp, category, amount, description, id);
    }
}