package model;

import java.time.LocalDate;

public class Date {
    // ----- ATTRIBUTE -----
    private final int day;
    private final int month;
    private final int year;

    // ----- CONSTRUCTOR -----
    private Date(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    // return new date with provided date, throws exception if invalid date
    public static Date of(int day, int month, int year) throws InvalidDateException {
        Date date = new Date(day, month, year);

        if (!date.isValid()) throw new InvalidDateException(date.toString());
        return date;
    }

    // return new current date
    public static Date current() {
        LocalDate now = LocalDate.now();
        int day = now.getDayOfMonth();
        int month = now.getMonthValue();
        int year = now.getYear();

        return new Date(day, month, year);
    }

    // ----- DATE -----
    // check validity of date
    public boolean isValid() {
        try {
            LocalDate.of(this.year, this.month, this.day);
            return true;
        } catch (java.time.DateTimeException e) {
            return false;
        }
    }

    // ----- GENERAL -----
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Date date)) return false;

        return this.day == date.day &&
                this.month == date.month &&
                this.year == date.year;
    }

    // return date in format (DD/MM/YY)
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder()
                .append(day)
                .append("/")
                .append(month)
                .append("/")
                .append(year);

        return s.toString();
    }
}
