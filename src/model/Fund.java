package model;

import model.data.Amount;
import model.data.Date;
import model.data.Name;
import model.query.Ledger;
import model.query.QueryOption;
import model.transaction.Expense;
import model.transaction.Income;
import model.transaction.Transaction;
import model.transaction.TransactionCreator;
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
     *
     * @param name The display name of the fund.
     * @return A shared Fund instance.
     */
    public static synchronized Fund of(String name) {
        Name normName = Name.of(name);
        
        Fund existing = REGISTRY.getByName(normName);
        if (existing != null)
            return existing;
        
        // Create new
        String newId = IdManager.generateUniqueId();
        Fund newFund = new Fund(newId, normName);
        
        REGISTRY.register(normName, newId, newFund);
        return newFund;
    }
    
    /**
     * Updates the fund name and synchronizes the lookup maps.
     *
     * @param newName The new name for this fund.
     */
    public synchronized void rename(String newName) {
        Name normName = Name.of(newName);
        
        REGISTRY.updateName(this.name, normName, this.id);
        this.name = normName;
    }
    
    /**
     * The method that handles object resolution and storage.
     *
     * @param creator A functional reference to a specific Transaction factory (e.g., Expense::of).1
     */
    private <T extends Transaction> void addTransaction(Amount amount, Date date, Category category,
                                                        String description, TransactionCreator<T> creator) {
        T transaction = creator.create(amount, date, category, description);
        transactions.add(transaction);
    }
    
    /**
     * Records a new expense in the fund's ledger.
     * <p>
     * This method abstracts the complexity of object creation by resolving the
     * category name into a Flyweight instance and delegating the instantiation
     * to the {@link Expense} factory.
     * </p>
     *
     * @param amount      The amount of the expense.
     * @param date        The date that the transfer occurred.
     * @param category    The category.
     * @param description A brief note describing the transaction.
     */
    public void addExpense(Amount amount, Date date, Category category, String description) {
        addTransaction(amount, date, category, description, Expense::of);
    }
    
    /**
     * Records a new income in the fund's ledger.
     * <p>
     * This method abstracts the complexity of object creation by resolving the
     * category name into a Flyweight instance and delegating the instantiation
     * to the {@link Income} factory.
     * </p>
     *
     * @param amount      The amount of the income.
     * @param date        The date that the transfer occurred.
     * @param category    The category.
     * @param description A brief note describing the transaction.
     */
    public void addIncome(Amount amount, Date date, Category category, String description) {
        addTransaction(amount, date, category, description, Income::of);
    }
    
    /**
     * Executes a cross-fund transfer by recording a local expense and a remote income.
     * <p>
     * This method maintains the integrity of the total system balance by ensuring
     * that every deduction from this fund is matched by a corresponding addition
     * to the destination fund.
     * </p>
     *
     * @param amount      The amount of the income.
     * @param date        The date that the transfer occurred.
     * @param category    The category.
     * @param fund        The destination fund.
     * @param description A brief note describing the transaction.
     */
    public void addTransfer(Amount amount, Date date, Category category, Fund fund, String description) {
        // Record the outflow from the current fund
        this.addExpense(amount, date, category,
                        String.format("%s (To: %s)", description, fund));
        
        // Record the inflow to the target fund
        fund.addIncome(amount, date, category,
                       String.format("%s (From: %s)", description, this));
    }
    
    /**
     * Generates a formatted report of the fund's transaction history.
     * <p>
     * This method applies the provided {@code QueryOption} filters to the ledger,
     * calculates the net balance of the resulting subset, and returns a
     * string representation including the fund's identity header.
     * </p>
     *
     * @param options Variadic array of filters (e.g., date range, category).
     * @return A formatted string showing: [Fund Name] | [Net Sum] \n [Transactions].
     */
    public String getHistory(QueryOption... options) {
        Ledger queried = transactions.query(options);
        return String.format("%s | %.2f \n%s", this, queried.sum(), queried);
    }
    
    @Override
    public String toString() {
        return String.format("%s", name);
    }
}
