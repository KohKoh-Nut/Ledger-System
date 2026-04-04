package model.transaction;

import model.Category;
import model.InvalidDateException;

/**
 * Functional interface used by Fund to add transaction.
 */
@FunctionalInterface
public interface TransactionFactory<T extends Transaction> {
    T create(double amount, int d, int m, int y, Category category, String description) throws InvalidDateException;
}
