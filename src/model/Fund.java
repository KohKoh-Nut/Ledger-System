package model;

import model.transaction.Expense;
import model.transaction.Income;
import model.transaction.Transaction;
import model.transaction.TransactionFactory;
import model.util.FlyweightRegistry;
import model.util.IdManager;

import java.util.ArrayList;
import java.util.List;

public final class Fund {

    /**
     * Maps a name & a unique UUID to the Fund object.
     */
    private static final FlyweightRegistry<Fund> REGISTRY = new FlyweightRegistry<>();

    private static final List<Transaction> transactions = new ArrayList<>();

    private final String id;
    private String name;

    /**
     * Private constructor to enforce the use of the static factory method {@link #of(String)}.
     */
    private Fund(String id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Factory method to retrieve an existing fund by name or create a new one.
     * @param name The display name of the fund.
     * @return A shared Fund instance.
     */
    public static synchronized Fund of(String name) {
        Fund existing = REGISTRY.getByName(name);
        if (existing != null) return existing;

        // Create new
        String newId = IdManager.generateUniqueId();
        Fund newFund = new Fund(newId, name.trim());

        REGISTRY.register(name, newId, newFund);
        return newFund;
    }

    /**
     * Updates the fund name and synchronizes the lookup maps.
     * @param newName The new name for this fund.
     */
    public synchronized void rename(String newName) {
        REGISTRY.updateName(this.name, newName, this.id);
        this.name = newName.trim();
    }

    /**
     * The method that handles object resolution and storage.
     * @param creator A functional reference to a specific Transaction factory (e.g., Expense::of).
     */
    private <T extends Transaction> void addTransaction(double amount, int day, int month, int year,
                                                        String categoryName, String description,
                                                        TransactionFactory<T> creator) {
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
     */
    public void addExpense(double amount, int day, int month, int year,
                           String categoryName, String description) {
        addTransaction(amount, day, month, year, categoryName, description, Expense::of);
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
     */
    public void addIncome(double amount, int day, int month, int year, String categoryName, String description) {
        addTransaction(amount, day, month, year, categoryName, description, Income::of);
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
     */
    public void addTransfer(double amount, int day, int month, int year,
                            String categoryName, String fundName, String description) {
        // Record the outflow from the current fund
        addExpense(amount, day, month, year, categoryName,
                String.format("%s (To: %s)", description, fundName));

        // Resolve the target fund instance
        Fund targetFund = Fund.of(fundName);

        // Record the inflow to the target fund
        targetFund.addIncome(amount, day, month, year, categoryName,
                String.format("%s (From: %s)", description, fundName));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Fund other)) return false;
        return this.id.equals(other.id);
    }

    @Override
    public int hashCode() {
        // Objects.hash handles null safety and distribution for you
        return java.util.Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", name, id);
    }
}
