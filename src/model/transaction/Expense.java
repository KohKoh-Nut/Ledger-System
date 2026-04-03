package model.transaction;

import model.Category;
import model.Date;

/**
 * Represents a negative cash flow (money spent).
 * Uses static factory methods to manage instantiation via the Transaction base class.
 */
public class Expense extends Transaction {

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
     */
    public static Expense of(double amount, int day, int month, int year,
                             Category category, String description) {
        return Transaction.of(amount, day, month, year, category, description, Expense::new);
    }

    /**
     * Creates an Expense using the current system date.
     * @return A new Expense instance.
     */
    public static Expense current(double amount, Category category, String description) {
        return Transaction.current(amount, category, description, Expense::new);
    }
}