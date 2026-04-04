package model.transaction;

import model.Category;
import model.data.Amount;
import model.data.Date;
import model.util.IdManager;

/**
 * Abstract base for all financial records (Income, Expense).
 * Centralizes common attributes like amount, category, and unique tracking IDs.
 */
public abstract class Transaction {

    private final String id;
    private final Amount amount;
    private final Date timestamp;
    private final Category category;
    private final String description;

    /**
     * Standard constructor for subclasses. Generates a unique ID automatically.
     */
    protected Transaction(Amount amount, Date timestamp, Category category, String description) {
        this.id = IdManager.generateUniqueId();
        this.amount = amount;
        this.timestamp = timestamp;
        this.category = category;
        this.description = description;
    }

    /**
     * Generic factory helper to create specific transaction types with a custom date.
     * @param creator A functional interface (lambda) that calls the specific subclass constructor.
     */
    protected static <T extends Transaction> T of(Amount amount, Date date, Category category,
                                                  String description, TransactionCreator<T> creator) {
        return creator.create(amount, date, category, description);
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
     * @return The Amount of the transaction.
     */
    public Amount getAmount() {
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
        return String.format("[%s] %s | %1s %s | %s",
                timestamp, category, this instanceof Income ? "" : "-", amount, description);
    }
}