package model.query;

import model.transaction.Transaction;

import java.util.stream.Stream;

/**
 * Functional interface used by Ledger to manage query options
 */
@FunctionalInterface
public interface TransactionQuery {
    // This takes a stream and returns a modified stream
    Stream<Transaction> apply(Stream<Transaction> stream);
}
