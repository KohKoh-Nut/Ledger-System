package model.transaction;

import model.Category;

/**
 * Functional interface used by Fund to add transaction.
 */
@FunctionalInterface
public interface TransactionFactory<T extends Transaction> {
    T create(double amount, int d, int m, int y, Category category, String description);
}
