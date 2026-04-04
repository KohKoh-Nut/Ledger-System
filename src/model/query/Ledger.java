package model.query;

import model.transaction.Income;
import model.transaction.Transaction;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * A specialized, immutable-first collection for managing financial records.
 * <p>
 * The Ledger serves as a functional container, allowing for
 * data transformations (filtering, sorting) while preserving the integrity
 * of the original transaction history.
 * </p>
 */
public final class Ledger {

    /**
     * Internal state - private to enforce encapsulation.
     */
    private final List<Transaction> transactions;

    /**
     * Private constructor to enforce the use of static factory methods.
     * @param transactionList The underlying list of transactions.
     */
    private Ledger(List<Transaction> transactionList) {
        this.transactions = transactionList;
    }

    /**
     * Static factory method to initialize an empty Ledger.
     * @return A new instance of {@code Ledger} with an empty internal list.
     */
    public static Ledger of() {
        return new Ledger(new ArrayList<>());
    }

    /**
     * Internal factory to wrap a list of transactions into a Ledger.
     * Often used to wrap the results of a stream transformation.
     * @param transactionList The list to be wrapped.
     * @return A new {@code Ledger} instance.
     */
    private static Ledger of(List<Transaction> transactionList) {
        return new Ledger(new ArrayList<>(transactionList));
    }

    /**
     * Appends a new transaction to the current ledger.
     * @param t The {@code Transaction} to add.
     */
    public void add(Transaction t) {
        transactions.add(t);
    }

    /**
     * Executes a series of transformations on the ledger data.
     * <p>
     * This method follows a "Stream Transformer" pattern. Since it returns
     * a new {@code Ledger}, queries can be incrementally built or passed
     * back into the system for further processing.
     * </p>
     *
     * @param options A varargs array of {@code QueryOption} transformers.
     * @return A new {@code Ledger} containing the filtered/sorted results.
     */
    public Ledger query(QueryOption... options) {
        Stream<Transaction> s = transactions.stream();

        for (QueryOption o : options) {
            s = o.apply(s);
        }

        // Returns a new Ledger instance, maintaining the functional chain
        return Ledger.of(s.toList());
    }

    /**
     * Computes the net balance in a single stream pass.
     * Expenses are treated as negative values, Income as positive.
     * @return The net total (Income - Expenses).
     */
    public double sum() {
        return transactions.stream()
                .mapToDouble(t -> t instanceof Income ? t.getAmount().getAmount() : -t.getAmount().getAmount())
                .sum();
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (Transaction t : transactions) {
            s.append(t.toString())
                    .append("\n");
        }

        return s.toString();
    }
}