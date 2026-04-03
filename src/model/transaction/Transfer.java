package model.transaction;

import model.Category;
import model.Date;

/**
 * Represents a negative cash flow (money transferred).
 * Uses static factory methods to manage instantiation via the Transaction base class.
 */
public class Transfer extends Transaction {

    /**
     * Private constructor to ensure Transfers are only created through
     * the provided factory methods.
     */
    private Transfer(double amount, Date timestamp, Category category, String description) {
        super(amount, timestamp, category, description);
    }

    /**
     * Creates a Transfer for a specific date.
     * @return A new Transfer instance.
     */
    public static Transfer of(double amount, int day, int month, int year,
                             Category category, String description) {
        return Transaction.of(amount, day, month, year, category, description, Transfer::new);
    }

    /**
     * Creates a Transfer using the current system date.
     * @return A new Transfer instance.
     */
    public static Transfer current(double amount, Category category, String description) {
        return Transaction.current(amount, category, description, Transfer::new);
    }
}