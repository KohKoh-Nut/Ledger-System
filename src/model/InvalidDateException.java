package model;

/**
 * Custom exception thrown when a date provided does not exist on the Gregorian calendar.
 * <p>
 * This is a checked exception used primarily by the {@link Date#of(int, int, int)}
 * factory method to enforce validation.
 * </p>
 */
public class InvalidDateException extends Exception
{
    // ----- CONSTRUCTOR -----

    /**
     * Constructs a new exception with a formatted error message.
     * * @param date The string representation of the invalid date that triggered the error.
     */
    public InvalidDateException(String date)
    {
        super(String.format("The provided date '%s' is not a valid calendar date. " +
                "Please ensure it follows the (DD/MM/YYYY) format and exists (e.g., no Feb 30).", date));
    }
}