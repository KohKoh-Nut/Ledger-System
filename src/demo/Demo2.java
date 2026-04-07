package demo;

import model.Category;
import model.Fund;
import model.InvalidDateException;
import model.data.Amount;
import model.data.Date;
import model.test.LedgerGenerator;
import model.test.TestCase;

import java.util.List;

/**
 * Simple demo class with sample inputs of expenses & incomes.
 * The output can be edited to test sorting & filtering.
 */
public class Demo2 {
    static void main(String[] args) {
        long startTime = System.nanoTime();
        
        TestCase test = null;
        try {
            test = new TestCase(0,
                                1000,
                                100,
                                100,
                                999999,
                                Date.of(1, 1, 2020),
                                Date.of(1, 1, 2026));
        } catch (InvalidDateException e) {
        }
        
        List<Fund> funds = LedgerGenerator.of(test);
        
        for (Fund f : funds) {
            System.out.println(f.getHistory());
        }
        
        long endTime = System.nanoTime();
        System.out.printf("Total run time: %d ns%n", (endTime - startTime));
    }
}
