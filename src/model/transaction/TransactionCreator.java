package model.transaction;

import model.Category;
import model.Date;

/**
 * Functional interface used by Transaction factories to instantiate subclasses.
 */
@FunctionalInterface
public interface TransactionCreator<T extends Transaction> {
    T create(double amount, Date timestamp, Category category, String description);
}
