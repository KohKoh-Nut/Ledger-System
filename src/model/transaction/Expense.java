package model.transaction;

import model.Category;
import model.data.Amount;
import model.data.Date;

/**
 * Represents a negative cash flow (money spent).
 * Uses static factory methods to manage instantiation via the Transaction base class.
 */
public final class Expense extends Transaction {

    /**
     * Private constructor to ensure Expenses are only created through
     * the provided factory methods.
     */
    private Expense(Amount amount, Date timestamp, Category category, String description) {
        super(amount, timestamp, category, description);
    }

    /**
     * Creates an Expense for a specific date.
     * @return A new Expense instance.
     */
    public static Expense of(Amount amount, Date date,
                             Category category, String description) {
        return Transaction.of(amount, date, category, description, Expense::new);
    }
}