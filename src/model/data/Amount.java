package model.data;

/**
 * A Value Object representing a monetary value paired with a specific currency.
 * <p>
 * This class is designed to be immutable to ensure thread safety and prevent
 * accidental side effects during financial calculations. It implements
 * {@code Comparable} to allow for logical sorting based on numerical value.
 * </p>
 */
public final class Amount implements Comparable<Amount> {
    
    private final double amount;
    private final Currency currency;
    
    /**
     * Private constructor to enforce the use of the static factory method {@link #of(double, Currency)}.
     */
    private Amount(double amount, Currency currency) {
        this.amount = amount;
        this.currency = currency;
    }
    
    /**
     * Static factory method for creating Amount instances.
     *
     * @param amount   The numerical value of the money.
     * @param currency The {@link Currency} type.
     * @return A new immutable {@code Amount} instance.
     */
    public static Amount of(double amount, Currency currency) {
        return new Amount(amount, currency);
    }
    
    /**
     * Supported currency types within the system.
     */
    public enum Currency {
        MYR, SGD, USD
    }
    
    public double getAmount() {
        return this.amount;
    }
    
    public Currency getCurrency() {
        return this.currency;
    }
    
    /**
     * Compares this amount with another based on their numerical values.
     * <p>
     * <b>Note:</b> This currently compares raw numerical values. For a
     * multi-currency system, this would ideally use an exchange rate
     * provider to normalize values before comparison.
     * </p>
     *
     * @param other The other {@code Amount} to compare against.
     * @return A negative integer, zero, or a positive integer as this
     * amount is less than, equal to, or greater than the specified amount.
     */
    @Override
    public int compareTo(Amount other) {
        // Double.compare is safer than (this - other) due to precision/NaN issues
        return Double.compare(this.amount, other.amount);
    }
    
    @Override
    public String toString() {
        return String.format("%s%10.2f", currency.name(), amount);
    }
}