package model.transaction;

import model.Category;
import model.data.Date;
import model.util.IdManager;
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
     * @throws InvalidDateException if the date provided is invalid.
     */
    protected static <T extends Transaction> T of(double amount, int day, int month, int year,
                                                  Category category, String description,
                                                  TransactionCreator<T> creator)
            throws InvalidDateException {
        return creator.create(amount, Date.of(day, month, year), category, description);
    }

    /**
     * Returns the date this transaction occurred.
     *
     * @return A {@code Date} instance representing the point in time of this entry.
     */
    public Date getDate() {
        return this.timestamp;
    }

    /**
     * Returns the monetary value of this transaction.
     * <p>
     * Positive values typically represent Income, while negative values represent Expenses.
     * </p>
     *
     * @return The raw double value of the transaction.
     */
    public double getAmount() {
        return this.amount;
    }

    /**
     * Returns the category associated with this transaction.
     * <p>
     * This returns a Flyweight {@code Category} instance from the Registry to ensure
     * memory efficiency and consistent identity.
     * </p>
     *
     * @return The categorized label for this transaction.
     */
    public Category getCategory() {
        return this.category;
    }

    @Override
    public String toString() {
        // Updated to %.2f for standard currency formatting
        return String.format("[%s] %s | %1s%10.2f | %s",
                timestamp, category, this instanceof Income ? "" : "-", amount, description);
    }
}