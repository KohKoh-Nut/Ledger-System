package model.test;

import model.Category;
import model.Fund;
import model.InvalidDateException;
import model.data.Amount;
import model.data.Date;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.SplittableRandom;

public final class LedgerGenerator {
    private static final String[] CURRENCIES = {"SGD", "MYR", "USD"};
    private final Random rand;
    private final SplittableRandom splitRand;
    private final List<Fund> funds;
    
    private LedgerGenerator(int seed, int numFunds) {
        this.rand = new Random(seed);
        this.splitRand = new SplittableRandom(seed);
        
        this.funds = new ArrayList<>(numFunds);
        for (int i = 0; i < numFunds; i++) {
            funds.add(Fund.of("Fund_" + i));
        }
    }
    
    public static List<Fund> of(TestCase test) {
        LedgerGenerator gen = new LedgerGenerator(test.seed(), test.numFunds());
        
        for (int i = 0; i < test.trials(); i++) {
            Fund f = gen.nextFund(test.numFunds());
            Amount a = gen.nextAmount(test.amountLimit());
            Date d = gen.nextDate(test.start(), test.end());
            Category c = gen.nextCategory(test.numCategories());
            if (gen.nextTransactionType()) {
                f.addExpense(a, d, c, "");
            } else {
                f.addIncome(a, d, c, "");
            }
        }
        
        return gen.funds;
    }
    
    private Boolean nextTransactionType() {
        return rand.nextBoolean();
    }
    
    private Fund nextFund(int numFunds) {
        return funds.get(rand.nextInt(0, numFunds));
    }
    
    private Category nextCategory(int numCategories) {
        return Category.of("Cat_" + rand.nextInt(0, numCategories));
    }
    
    private Amount nextAmount(double limit) {
        return Amount.of(rand.nextDouble(1.0, limit),
                         Amount.Currency.valueOf(CURRENCIES[rand.nextInt(CURRENCIES.length)]));
    }
    
    private Date nextDate(Date start, Date end) {
        while (true) {
            try {
                Date generated = Date.of(splitRand.nextInt(1, 32),
                                         splitRand.nextInt(1, 13),
                                         splitRand.nextInt(start.year, end.year + 1));
                if (!generated.before(start) && !generated.after(end)) {
                    return generated;
                }
            } catch (InvalidDateException e) {
            }
        }
    }
}
