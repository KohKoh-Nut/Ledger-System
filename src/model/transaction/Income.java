package model.transaction;

import model.Category;
import model.Date;

/**
 * Represents a positive cash flow (money earned).
 * Uses static factory methods to manage instantiation via the Transaction base class.
 */
public class Income extends Transaction {

    /**
     * Private constructor to ensure Incomes are only created through
     * the provided factory methods.
     */
    private Income(double amount, Date timestamp, Category category, String description) {
        super(amount, timestamp, category, description);
    }

    /**
     * Creates an Income for a specific date.
     * @return A new Income instance.
     */
    public static Income of(double amount, int day, int month, int year,
                             Category category, String description) {
        return Transaction.of(amount, day, month, year, category, description, Income::new);
    }

    /**
     * Creates an Income using the current system date.
     * @return A new Income instance.
     */
    public static Income current(double amount, Category category, String description) {
        return Transaction.current(amount, category, description, Income::new);
    }
}