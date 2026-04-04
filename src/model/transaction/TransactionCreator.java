package model.transaction;

import model.Category;
import model.InvalidDateException;
import model.data.Amount;
import model.data.Date;

/**
 * Functional interface used by Transaction & Fund to instantiate subclasses & transactions.
 */
@FunctionalInterface
public interface TransactionCreator<T extends Transaction> {
    T create(Amount amount, Date timestamp, Category category, String description);
}
