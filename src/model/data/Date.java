package model.data;

import model.InvalidDateException;

import java.time.LocalDate;
import java.util.Map;
import java.util.HashMap;

/**
 * Represent a date (e.g. 1-1-2025, 28-2-1999)
 * Implements the Flyweight pattern to ensure memory efficiency by sharing
 * unique Date instances across the application.
 */
public final class Date implements Comparable<Date> {

    /**
     * Maps a day-month-year key to the Date object.
     */
    private static final Map<String, Date> KEY_DATE_POOL = new HashMap<>();

    public final int day;
    public final int month;
    public final int year;

    /**
     * Private constructor to enforce the use of the static factory method
     * {@link #of(int, int, int)} or {@link #current()}.
     */
    private Date(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    /**
     * Factory method to retrieve a cached Date or create a new one.
     * @param day   The day of the month (1-31).
     * @param month The month of the year (1-12).
     * @param year  The full year (e.g., 2026).
     * @return A shared Date instance.
     * @throws InvalidDateException If the combination of day-month-year is not a calendar date
     */
    public static synchronized Date of(int day, int month, int year)
            throws InvalidDateException {
        String key = String.format("%d-%d-%d", day, month, year);
        Date date = KEY_DATE_POOL.get(key);

        // If date doesn't exist in POOL, instantiate and cache it
        if (date == null) {
            if (!isValid(day, month, year))
                throw new InvalidDateException(String.format("%d-%d-%d", day, month, year));

            date = new Date(day, month, year);
            KEY_DATE_POOL.put(key, date);
        }
        return date;
    }

    /**
     * Factory method to retrieve a cached current Date or create a new one.
     * @return A shared Date instance.
     * @throws InvalidDateException if the system date is invalid.
     */
    public static Date current()
            throws InvalidDateException {
        LocalDate now = LocalDate.now();
        int day = now.getDayOfMonth();
        int month = now.getMonthValue();
        int year = now.getYear();

        return of(day, month, year);
    }

    /**
     * Check validity of provided date
     */
    private static boolean isValid(int day, int month, int year) {
        try {
            LocalDate.of(year, month, day);
            return true;
        } catch (java.time.DateTimeException e) {
            return false;
        }
    }

    /**
     * Checks if this date is strictly before another date.
     * @param other The date to compare with
     * @return true if the given date is before this, false if otherwise
     */
    public boolean before(Date other) {
        if (this.year != other.year) {
            return this.year < other.year;
        }
        if (this.month != other.month) {
            return this.month < other.month;
        }
        return this.day < other.day;
    }

    /**
     * Checks if this date is strictly after another date.
     * @param other The date to compare with
     * @return true if the given date is after this, false if otherwise
     */
    public boolean after(Date other) {
        return !this.before(other) && !this.equals(other);
    }

    @Override
    public int compareTo(Date other) {
        if (this.year != other.year) return Integer.compare(this.year, other.year);
        if (this.month != other.month) return Integer.compare(this.month, other.month);
        return Integer.compare(this.day, other.day);
    }

    @Override
    public String toString() {
        return String.format("%2d-%2d-%4d", day, month, year);
    }
}
