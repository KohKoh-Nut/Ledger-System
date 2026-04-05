package model;

import model.data.Amount;
import model.data.Amount.Currency;
import model.data.Date;
import model.query.QueryOption;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

class FundTest {
    
    @Test
    @DisplayName("Should add an expense and reflect it in the history")
    void testAddExpense()
            throws
            InvalidDateException {
        Fund wallet = Fund.of("Wallet");
        wallet.addExpense(Amount.of(50, Currency.SGD), Date.current(), Category.of("Food"), "Lunch at Deck");
        
        String history = wallet.getHistory();
        assertTrue(history.contains("Wallet"), "History should contain fund name");
        assertTrue(history.contains("Food"), "History should contain category name");
        assertTrue(history.contains("50.0"), "History should contain the amount");
    }
    
    @Test
    @DisplayName("Transfer Logic: Should create an expense in source and income in target")
    void testAddTransfer()
            throws
            InvalidDateException {
        Fund savings = Fund.of("Savings");
        Fund investment = Fund.of("Stocks");
        
        savings.addTransfer(Amount.of(100, Currency.SGD), Date.current(), Category.of("Transfer"), Fund.of("stocks"),
                            "Monthly Investment");
        
        // Check Source: Should show it sent money TO Stocks
        String savingsHistory = savings.getHistory();
        assertTrue(savingsHistory.contains("To: Stocks"),
                   "Savings should show 'To: Stocks'. Found: " + savingsHistory);
        
        // Check Target: Should show it received money FROM Savings
        String stocksHistory = investment.getHistory();
        assertTrue(stocksHistory.contains("From: Savings"),
                   "Stocks should show 'From: Savings'. Found: " + stocksHistory);
    }
    
    @Test
    @DisplayName("Querying: Should return a filtered view of the transactions")
    void testGetHistoryWithFilters()
            throws
            InvalidDateException {
        Fund travel = Fund.of("Travel");
        travel.addExpense(Amount.of(10, Currency.SGD), Date.current(), Category.of("Food"), "Snack");
        travel.addExpense(Amount.of(500, Currency.SGD), Date.current(), Category.of("Flights"), "To Japan");
        
        // Query only for expensive items
        String filteredHistory = travel.getHistory(
                QueryOption.filterAll(QueryOption.min(Amount.of(100, Currency.SGD))));
        
        assertTrue(filteredHistory.contains("Flights"));
        assertFalse(filteredHistory.contains("Snack"), "Low amount should be filtered out");
    }
}