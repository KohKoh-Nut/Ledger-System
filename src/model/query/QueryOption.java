package model.query;

import model.Category;
import model.InvalidDateException;
import model.data.Date;
import model.transaction.Expense;
import model.transaction.Income;
import model.transaction.Transaction;

import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A Typesafe Enum and Factory class for defining Transaction queries.
 * <p>
 * This class uses the "Stream Transformer" pattern to encapsulate
 * filtering and sorting logic into reusable, composable objects.
 * </p>
 */
public final class QueryOption implements TransactionQuery{

    /**
     * Constants representing "Natural" or fixed operations.
     * These are singletons to minimize memory pressure.
     */
    public static final QueryOption SORT_BY_DATE =
            of(s -> s.sorted(Comparator.comparing(Transaction::getDate)));
    public static final QueryOption SORT_BY_DATE_DESCENDING =
            of(s-> s.sorted(Comparator.comparing(Transaction::getDate).reversed()));
    public static final QueryOption SORT_BY_AMOUNT =
            of(s -> s.sorted(Comparator.comparing(Transaction::getAmount)));
    public static final QueryOption SORT_BY_AMOUNT_DESCENDING =
            of(s -> s.sorted(Comparator.comparing(Transaction::getAmount).reversed()));
    public static final QueryOption SORT_BY_CATEGORY =
            of(s -> s.sorted(Comparator.comparing(Transaction::getCategory)));
    public static final QueryOption SORT_BY_CATEGORY_DESCENDING =
            of(s -> s.sorted(Comparator.comparing(Transaction::getCategory).reversed()));
    public static final QueryOption FILTER_BY_EXPENSE =
            of(s -> s.filter(t -> t instanceof Expense));
    public static final QueryOption FILTER_BY_INCOME =
            of(s -> s.filter(t -> t instanceof Income));

    private final TransactionQuery query;

    /**
     * Private constructor to enforce the use of the static factory method
     * {@link #of(TransactionQuery)}.
     */
    private QueryOption(TransactionQuery query) {
        this.query = query;
    }

    /**
     * Private factory method to enforce the use of the static factory method
     * {@link #min(double)}, {@link #max(double)}, {@link #before(int, int, int)}, {@link #after(int, int, int)},
     * {@link #cat(String...)}, {@link #notCat(String...)}.
     */
    private static QueryOption of(TransactionQuery query) {
        return new QueryOption(query);
    }

    /**
     * Creates a filter for a minimum transaction amount (inclusive).
     * @param amount The lower bound value.
     * @return A {@code QueryOption} that filters for amount >= threshold.
     */
    public static QueryOption min(double amount) {
        return of(s -> s.filter(t -> t.getAmount() >= amount));
    }

    /**
     * Creates a filter for a max transaction amount (inclusive).
     * @param amount The upper bound value.
     * @return A {@code QueryOption} that filters for amount <= threshold.
     */
    public static QueryOption max(double amount) {
        return of(s -> s.filter(t -> t.getAmount() <= amount));
    }

    /**
     * Creates a filter for transactions occurring strictly before the specified date.
     *
     * @param day   The day of the month (1-31).
     * @param month The month of the year (1-12).
     * @param year  The full year (e.g., 2026).
     * @return A {@code QueryOption} that excludes the given date and all dates after it.
     * @throws InvalidDateException If the provided parameters do not form a valid calendar date.
     */
    public static QueryOption before(int day, int month, int year)
            throws InvalidDateException {
        Date date = Date.of(day, month, year);
        return of(s -> s.filter(t -> t.getDate().before(date)));
    }

    /**
     * Creates a filter for transactions occurring strictly after the specified date.
     *
     * @param day   The day of the month (1-31).
     * @param month The month of the year (1-12).
     * @param year  The full year (e.g., 2026).
     * @return A {@code QueryOption} that excludes the given date and all dates after it.
     * @throws InvalidDateException If the provided parameters do not form a valid calendar date.
     */
    public static QueryOption after(int day, int month, int year)
            throws InvalidDateException {
        Date date = Date.of(day, month, year);
        return of(s -> s.filter(t -> t.getDate().after(date)));
    }

    /**
     * Filters transactions by a variable list of categories.
     * Uses a HashSet internally for O(1) membership checking.
     *
     * @param categoriesName The categories name to include in the result.
     * @return A {@code QueryOption} that acts as a whitelist for the given categories.
     */
    public static QueryOption cat(String ... categoriesName) {
        Set<Category> included = Set.of(categoriesName)
                .stream()
                .map(Category::of)
                .collect(Collectors.toSet());
        return of(s -> s.filter(t -> included.contains(t.getCategory())));
    }

    /**
     * Filters transactions by a variable list of categories.
     * Uses a HashSet internally for O(1) membership checking.
     *
     * @param categoriesName The categories name to exclude in the result.
     * @return A {@code QueryOption} that acts as a blacklist for the given categories.
     */
    public static QueryOption notCat(String ... categoriesName) {
        Set<Category> excluded = Set.of(categoriesName)
                .stream()
                .map(Category::of)
                .collect(Collectors.toSet());
        return of(s -> s.filter(t -> !excluded.contains(t.getCategory())));
    }

    /**
     * Applies the encapsulated query logic to the provided transaction stream.
     * <p>
     * This method acts as the entry point for the "Stream Transformer" pattern,
     * allowing this {@code QueryOption} to be piped into a functional chain.
     * </p>
     *
     * @param stream The source {@code Stream} of transactions to be transformed.
     * @return A new {@code Stream} that has been filtered or sorted.
     */
    @Override
    public Stream<Transaction> apply(Stream<Transaction> stream) {
        return query.apply(stream);
    }
}
