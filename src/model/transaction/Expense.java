package model.transaction;

import model.Category;
import model.data.Date;
import model.InvalidDateException;

/**
 * Represents a negative cash flow (money spent).
 * Uses static factory methods to manage instantiation via the Transaction base class.
 */
public final class Expense extends Transaction {

    /**
     * Private constructor to ensure Expenses are only created through
     * the provided factory methods.
     */
    private Expense(double amount, Date timestamp, Category category, String description) {
        super(amount, timestamp, category, description);
    }

    /**
     * Creates an Expense for a specific date.
     * @return A new Expense instance.
     * @throws InvalidDateException if the date provided is invalid.
     */
    public static Expense of(double amount, int day, int month, int year,
                             Category category, String description)
            throws InvalidDateException {
        return Transaction.of(amount, day, month, year, category, description, Expense::new);
    }
}