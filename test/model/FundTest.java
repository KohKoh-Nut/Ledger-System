package model;

import model.query.QueryOption;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class FundTest {

    @Test
    @DisplayName("Should add an expense and reflect it in the history")
    void testAddExpense() throws InvalidDateException {
        Fund wallet = Fund.of("Wallet");
        wallet.addExpense(50.0, "Food", "Lunch at Deck");

        String history = wallet.getHistory();
        assertTrue(history.contains("Wallet"), "History should contain fund name");
        assertTrue(history.contains("Food"), "History should contain category name");
        assertTrue(history.contains("50.0"), "History should contain the amount");
    }

    @Test
    @DisplayName("Transfer Logic: Should create an expense in source and income in target")
    void testAddTransfer() throws InvalidDateException {
        Fund savings = Fund.of("Savings");
        Fund investment = Fund.of("Stocks");

        savings.addTransfer(100.0, "Transfer", "Stocks", "Monthly Investment");

        // Verify Savings (Source) recorded an outflow
        assertTrue(savings.getHistory().contains("To: Stocks"));

        // Verify Investment (Target) recorded an inflow
        assertTrue(investment.getHistory().contains("From: Stocks"));
    }

    @Test
    @DisplayName("Querying: Should return a filtered view of the transactions")
    void testGetHistoryWithFilters() throws InvalidDateException {
        Fund travel = Fund.of("Travel");
        travel.addExpense(10.0, "Food", "Snack");
        travel.addExpense(500.0, "Flights", "To Japan");

        // Query only for expensive items
        String filteredHistory = travel.getHistory(QueryOption.min(100.0));

        assertTrue(filteredHistory.contains("Flights"));
        assertFalse(filteredHistory.contains("Snack"), "Low amount should be filtered out");
    }
}