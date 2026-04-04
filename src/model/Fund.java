package model;

import model.data.Date;
import model.query.Ledger;
import model.data.Name;
import model.query.QueryOption;
import model.transaction.Expense;
import model.transaction.Income;
import model.transaction.Transaction;
import model.transaction.TransactionFactory;
import model.util.FlyweightRegistry;
import model.util.IdManager;

public final class Fund {

    /**
     * Maps a name & a unique UUID to the Fund object.
     */
    private static final FlyweightRegistry<Fund> REGISTRY = new FlyweightRegistry<>();

    private final Ledger transactions = Ledger.of();

    private final String id;
    private Name name;

    /**
     * Private constructor to enforce the use of the static factory method {@link #of(String)}.
     */
    private Fund(String id, Name name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Factory method to retrieve an existing fund by name or create a new one.
     * @param name The display name of the fund.
     * @return A shared Fund instance.
     */
    public static synchronized Fund of(String name) {
        Name normName = Name.of(name);

        Fund existing = REGISTRY.getByName(normName);
        if (existing != null) return existing;

        // Create new
        String newId = IdManager.generateUniqueId();
        Fund newFund = new Fund(newId, normName);

        REGISTRY.register(normName, newId, newFund);
        return newFund;
    }

    /**
     * Updates the fund name and synchronizes the lookup maps.
     * @param newName The new name for this fund.
     */
    public synchronized void rename(String newName) {
        Name normName = Name.of(newName);

        REGISTRY.updateName(this.name, normName, this.id);
        this.name = normName;
    }

    /**
     * The method that handles object resolution and storage.
     * @param creator A functional reference to a specific Transaction factory (e.g., Expense::of).1
     */
    private <T extends Transaction> void addTransaction(double amount, int day, int month, int year,
                                                        String categoryName, String description,
                                                        TransactionFactory<T> creator)
            throws InvalidDateException {
        Category category = Category.of(categoryName);
        T transaction = creator.create(amount, day, month, year, category, description);
        transactions.add(transaction);
    }

    /**
     * Records a new expense in the fund's ledger.
     * <p>
     * This method abstracts the complexity of object creation by resolving the
     * category name into a Flyweight instance and delegating the instantiation
     * to the {@link Expense} factory.
     * </p>
     * @param amount       The monetary value of the expense.
     * @param day          The day of the month (1-31).
     * @param month        The month of the year (1-12).
     * @param year         The full year (e.g., 2026).
     * @param categoryName The name of the category; automatically resolved to a shared instance.
     * @param description  A brief note describing the transaction.
     * @throws InvalidDateException if the date provided is invalid.
     */
    public void addExpense(double amount, int day, int month, int year,
                           String categoryName, String description)
            throws InvalidDateException {
        addTransaction(amount, day, month, year, categoryName, description, Expense::of);
    }

    /**
     * Records an expense occurring today.
     * <p>
     * This is a convenience overload that automatically resolves the date
     * using {@link Date#current()}.
     * </p>
     * @see #addExpense(double, int, int, int, String, String)
     */
    public void addExpense(double amount, String categoryName, String description)
            throws InvalidDateException {
        addExpense(amount, Date.current().day, Date.current().month, Date.current().year,
                categoryName, description);
    }

    /**
     * Records a new income in the fund's ledger.
     * <p>
     * This method abstracts the complexity of object creation by resolving the
     * category name into a Flyweight instance and delegating the instantiation
     * to the {@link Income} factory.
     * </p>
     * @param amount       The monetary value of the income.
     * @param day          The day of the month (1-31).
     * @param month        The month of the year (1-12).
     * @param year         The full year (e.g., 2026).
     * @param categoryName The name of the category; automatically resolved to a shared instance.
     * @param description  A brief note describing the transaction.
     * @throws InvalidDateException if the date provided is invalid.
     */
    public void addIncome(double amount, int day, int month, int year, String categoryName, String description)
            throws InvalidDateException {
        addTransaction(amount, day, month, year, categoryName, description, Income::of);
    }

    /**
     * Records an income occurring today.
     * <p>
     * This is a convenience overload that automatically resolves the date
     * using {@link Date#current()}.
     * </p>
     * @see #addIncome(double, int, int, int, String, String)
     */
    public void addIncome(double amount, String categoryName, String description)
            throws InvalidDateException {
        addIncome(amount, Date.current().day, Date.current().month, Date.current().year,
                categoryName, description);
    }

    /**
     * Executes a cross-fund transfer by recording a local expense and a remote income.
     * <p>
     * This method maintains the integrity of the total system balance by ensuring
     * that every deduction from this fund is matched by a corresponding addition
     * to the destination fund.
     * </p>
     *
     * @param amount       The monetary value of the income.
     * @param day          The day of the month (1-31).
     * @param month        The month of the year (1-12).
     * @param year         The full year (e.g., 2026).
     * @param categoryName The name of the category; automatically resolved to a shared instance.
     * @param fundName     The name of the destination fund; automatically resolved to a shared instance.
     * @param description  A brief note describing the transaction.
     * @throws InvalidDateException if the date provided is invalid.
     */
    public void addTransfer(double amount, int day, int month, int year,
                            String categoryName, String fundName, String description)
            throws InvalidDateException {
        // Record the outflow from the current fund
        addExpense(amount, day, month, year, categoryName,
                String.format("%s (To: %s)", description, fundName));

        // Resolve the target fund instance
        Fund targetFund = Fund.of(fundName);

        // Record the inflow to the target fund
        targetFund.addIncome(amount, day, month, year, categoryName,
                String.format("%s (From: %s)", description, fundName));
    }

    /**
     * Records a transfer occurring today.
     * <p>
     * This is a convenience overload that automatically resolves the date
     * using {@link Date#current()}.
     * </p>
     * @see #addTransfer(double, int, int, int, String, String, String)
     */
    public void addTransfer(double amount, String categoryName, String fundName, String description)
            throws InvalidDateException {
        addTransfer(amount, Date.current().day, Date.current().month, Date.current().year,
                categoryName, fundName, description);
    }

    public String getHistory(QueryOption ... options) {
        Ledger queried = transactions.query(options);
        return this + "\n" + queried;
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", name, id);
    }
}
