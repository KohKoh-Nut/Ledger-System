package model.test;

import model.InvalidDateException;
import model.data.Date;

public record TestCase(
        int seed,
        int trials,
        int numFunds,
        int numCategories,
        double amountLimit,
        Date start,
        Date end
) {
    public static TestCase ofSafe(int seed, int trials, int funds, int cats, double limit,
                                  int d1, int m1, int y1, int d2, int m2, int y2) {
        try {
            return new TestCase(seed, trials, funds, cats, limit, Date.of(d1, m1, y1), Date.of(d2, m2, y2));
        } catch (InvalidDateException e) {
            return null;
        }
    }
    
    @Override
    public String toString() {
        return String.format(
                "Seed: %12d | Trials: %12d | NoOfFunds: %12d | NoOfCat: %12d \nLimit: %11.2f | Start: %13s | End: %13s",
                seed,
                trials,
                numFunds,
                numCategories,
                amountLimit,
                start,
                end);
    }
}