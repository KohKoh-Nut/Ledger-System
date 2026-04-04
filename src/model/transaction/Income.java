package model.transaction;

import model.Category;
import model.data.Date;
import model.InvalidDateException;

/**
 * Represents a positive cash flow (money earned).
 * Uses static factory methods to manage instantiation via the Transaction base class.
 */
public final class Income extends Transaction {

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
     * @throws InvalidDateException if the date provided is invalid.
     */
    public static Income of(double amount, int day, int month, int year,
                            Category category, String description)
            throws InvalidDateException {
        return Transaction.of(amount, day, month, year, category, description, Income::new);
    }
}