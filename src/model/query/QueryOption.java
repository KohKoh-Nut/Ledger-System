package model.query;

import model.Category;
import model.data.Amount;
import model.data.Date;
import model.transaction.Expense;
import model.transaction.Income;
import model.transaction.Transaction;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * A Factory and Provider class for {@link TransactionQuery} operations.
 * <p>
 * This class implements the <b>Stream Transformer Pattern</b>. It provides static
 * factories to create composable filters and sorts that can be applied to a
 * {@code Stream<Transaction>} without breaking the functional pipeline.
 * </p>
 */
public final class QueryOption implements TransactionQuery {
    
    /**
     * Sorts transactions by date in ascending order.
     */
    public static final Comparator<Transaction> BY_DATE = Comparator.comparing(Transaction::getDate);
    
    /**
     * Sorts transactions by date in descending order (newest first).
     */
    public static final Comparator<Transaction> BY_DATE_DESCENDING = BY_DATE.reversed();
    
    /**
     * Sorts transactions by their absolute amount value.
     */
    public static final Comparator<Transaction> BY_AMOUNT = Comparator.comparing(Transaction::getAmount);
    
    /**
     * Sorts transactions by their absolute amount value in descending order.
     */
    public static final Comparator<Transaction> BY_AMOUNT_DESCENDING = BY_AMOUNT.reversed();
    
    /**
     * Sorts transactions alphabetically by their category name.
     */
    public static final Comparator<Transaction> BY_CATEGORY = Comparator.comparing(Transaction::getCategory);
    
    /**
     * Sorts transactions alphabetically by their category name in reverse.
     */
    public static final Comparator<Transaction> BY_CATEGORY_DESCENDING = BY_CATEGORY.reversed();
    
    /**
     * Sorts transactions based on their ISO currency code.
     */
    public static final Comparator<Transaction> BY_CURRENCY = Comparator.comparing(t -> t.getAmount()
                                                                                         .getCurrency());
    
    /**
     * Sorts transactions based on their ISO currency code in reverse.
     */
    public static final Comparator<Transaction> BY_CURRENCY_DESCENDING = BY_CURRENCY.reversed();
    
    /**
     * Predicate that matches only {@link Expense} instances.
     */
    public static final Predicate<Transaction> BY_EXPENSE = t -> t instanceof Expense;
    
    /**
     * Predicate that matches only {@link Income} instances.
     */
    public static final Predicate<Transaction> BY_INCOME = t -> t instanceof Income;
    
    private final TransactionQuery query;
    
    private QueryOption(TransactionQuery query) {
        this.query = query;
    }
    
    private static QueryOption of(TransactionQuery query) {
        return new QueryOption(query);
    }
    
    /**
     * Composes multiple comparators into a single sort operation.
     * <p>
     * Uses {@link Comparator#thenComparing(Comparator)} to ensure a stable,
     * hierarchical sort where subsequent comparators resolve ties from previous ones.
     * </p>
     *
     * @param comparators An ordered array of comparators (primary, secondary, etc.).
     * @return A {@code QueryOption} that applies the combined sort to the stream.
     */
    public static QueryOption sort(Comparator<Transaction>... comparators) {
        return of(s -> s.sorted(Arrays.stream(comparators)
                                      .reduce(Comparator::thenComparing)
                                      .orElse((t1, t2) -> 0)));
    }
    
    /**
     * Composes multiple predicates into a single filter operation.
     * <p>
     * Uses logical AND reduction. A transaction must satisfy <b>all</b>
     * provided predicates to remain in the stream.
     * </p>
     *
     * @param predicates Variable list of predicates to apply.
     * @return A {@code QueryOption} that filters the stream by all predicates.
     */
    public static QueryOption filterAll(Predicate<Transaction>... predicates) {
        return of(s -> s.filter(Arrays.stream(predicates)
                                      .reduce(p -> true, Predicate::and)));
    }
    
    /**
     * Composes multiple predicates into a single filter operation.
     * <p>
     * Uses logical OR reduction. A transaction must satisfy <b>any</b>
     * provided predicates to remain in the stream.
     * </p>
     *
     * @param predicates Variable list of predicates to apply.
     * @return A {@code QueryOption} that filters the stream by all predicates.
     */
    public static QueryOption filterAny(Predicate<Transaction>... predicates) {
        return of(s -> s.filter(Arrays.stream(predicates)
                                      .reduce(p -> false, Predicate::or)));
    }
    
    /**
     * Creates a threshold predicate for a minimum amount.
     * <p>
     * <b>Currency Logic:</b> If the transaction currency differs from the threshold,
     * it returns {@code true} (passes). If the currency matches, it must be
     * {@code >= threshold}.
     * </p>
     *
     * @param threshold The minimum amount required for matching currencies.
     * @return A predicate for filtering minimum values.
     */
    public static Predicate<Transaction> min(Amount threshold) {
        return t -> {
            Amount tAmount = t.getAmount();
            if (tAmount.getCurrency() != threshold.getCurrency()) {
                return true;
            }
            return tAmount.getAmount() >= threshold.getAmount();
        };
    }
    
    /**
     * Creates a threshold predicate for a maximum amount.
     * <p>
     * <b>Currency Logic:</b> If the transaction currency differs from the threshold,
     * it returns {@code true} (passes). If the currency matches, it must be
     * {@code <= threshold}.
     * </p>
     *
     * @param threshold The maximum amount allowed for matching currencies.
     * @return A predicate for filtering maximum values.
     */
    public static Predicate<Transaction> max(Amount threshold) {
        return t -> {
            Amount tAmount = t.getAmount();
            if (tAmount.getCurrency() != threshold.getCurrency()) {
                return true;
            }
            return tAmount.getAmount() <= threshold.getAmount();
        };
    }
    
    /**
     * Filters for transactions occurring strictly before the given date.
     *
     * @param date The boundary date (exclusive).
     * @return A predicate that matches earlier transactions.
     */
    public static Predicate<Transaction> before(Date date) {
        return t -> t.getDate()
                     .before(date);
    }
    
    /**
     * Filters for transactions occurring strictly after the given date.
     *
     * @param date The boundary date (exclusive).
     * @return A predicate that matches later transactions.
     */
    public static Predicate<Transaction> after(Date date) {
        return t -> t.getDate()
                     .after(date);
    }
    
    /**
     * Creates a whitelist filter for specific categories.
     *
     * @param categories Categories to be included.
     * @return A predicate that matches if a transaction is in the specified categories.
     */
    public static Predicate<Transaction> cat(Category... categories) {
        Set<Category> included = Set.of(categories);
        return t -> included.contains(t.getCategory());
    }
    
    /**
     * Creates a blacklist filter for specific categories.
     *
     * @param categories Categories to be excluded.
     * @return A predicate that matches if a transaction is NOT in the specified categories.
     */
    public static Predicate<Transaction> notCat(Category... categories) {
        Set<Category> excluded = Set.of(categories);
        return t -> !excluded.contains(t.getCategory());
    }
    
    /**
     * Transforms the input stream using the encapsulated query logic.
     *
     * @param stream The source stream of transactions.
     * @return The transformed (filtered/sorted) stream.
     */
    @Override
    public Stream<Transaction> apply(Stream<Transaction> stream) {
        return query.apply(stream);
    }
}