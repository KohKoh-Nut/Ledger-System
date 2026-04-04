package model.transaction;

import model.Category;
import model.data.Amount;
import model.data.Date;

/**
 * Represents a positive cash flow (money earned).
 * Uses static factory methods to manage instantiation via the Transaction base class.
 */
public final class Income extends Transaction {

    /**
     * Private constructor to ensure Incomes are only created through
     * the provided factory methods.
     */
    private Income(Amount amount, Date timestamp, Category category, String description) {
        super(amount, timestamp, category, description);
    }

    /**
     * Creates an Income for a specific date.
     * @return A new Income instance.
     */
    public static Income of(Amount amount, Date date,
                            Category category, String description) {
        return Transaction.of(amount, date, category, description, Income::new);
    }
}