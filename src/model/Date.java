package model;

import java.time.LocalDate;
import java.util.Map;
import java.util.HashMap;

/**
 * Represent a date (e.g. 1-1-2025, 28-2-1999)
 * Implements the Flyweight pattern to ensure memory efficiency by sharing
 * unique Date instances across the application.
 */
public class Date {

    /**
     * Maps a day-month-year key to the Date object.
     */
    private static final Map<String, Date> KEY_DATE_POOL = new HashMap<>();

    private final int day;
    private final int month;
    private final int year;

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
     * @param day The day of the month.
     * @param month The month of the year.
     * @param year The full year.
     * @return A shared Date instance.
     * @throws InvalidDateException If the combination of day-month-year is not a calendar date
     */
    public static synchronized Date of(int day, int month, int year) throws InvalidDateException {
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
     */
    public static Date current() {
        try {
            LocalDate now = LocalDate.now();
            int day = now.getDayOfMonth();
            int month = now.getMonthValue();
            int year = now.getYear();

            return of(day, month, year);
        } catch (InvalidDateException e) {
            // This should technically be unreachable
            throw new RuntimeException("System clock provided an invalid date", e);
        }
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Date date)) return false;
        return this.day == date.day &&
                this.month == date.month &&
                this.year == date.year;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(day, month, year);
    }

    @Override
    public String toString() {
        return String.format("%d-%d-%d", day, month, year);
    }
}
