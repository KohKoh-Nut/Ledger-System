package demo;

import model.Category;
import model.Fund;
import model.InvalidDateException;
import model.data.Amount;
import model.data.Date;
import model.query.QueryOption;

/**
 * Simple demo class with sample inputs of expenses & incomes.
 * The output can be edited to test sorting & filtering.
 */
public class Demo1
{
    public static void main(String[] args)
    {
        Fund f1 = Fund.of("allowance SGD");
        Fund f2 = Fund.of("Savings MYR");
        Fund f3 = Fund.of("saviNgs sgd");
        Fund f4 = Fund.of("spendings Foreign");
        Fund f5 = Fund.of("cash");

        Fund[] funds = {f1, f2, f3, f4, f5};

        try {
            Category food = Category.of("Food");
            Category transport = Category.of("Transport");
            Category salary = Category.of("Salary");
            Category savings = Category.of("Savings");
            Category leisure = Category.of("Leisure");
            Category shopping = Category.of("Shopping");
            Category subs = Category.of("Subscriptions");

            // ==========================================
            // JANUARY 2026: THE SEMESTER STARTS
            // ==========================================
            f1.addIncome(Amount.of(2000, Amount.Currency.SGD), Date.of(1, 1, 2026), salary, "Internship Stipend");
            f1.addIncome(Amount.of(500, Amount.Currency.SGD), Date.of(1, 1, 2026), Category.of("Allowance"), "From Dad");
            f1.addExpense(Amount.of(12.50, Amount.Currency.SGD), Date.of(1, 1, 2026), food, "New Year Lunch");
            f1.addExpense(Amount.of(3.20, Amount.Currency.SGD), Date.of(2, 1, 2026), transport, "MRT to NUS");
            f1.addExpense(Amount.of(1.50, Amount.Currency.SGD), Date.of(2, 1, 2026), food, "Teh C Peng");
            f1.addExpense(Amount.of(6.00, Amount.Currency.SGD), Date.of(3, 1, 2026), food, "Fine Foods Caifan");
            f4.addExpense(Amount.of(14.99, Amount.Currency.USD), Date.of(3, 1, 2026), subs, "Spotify Family");
            f4.addExpense(Amount.of(20.00, Amount.Currency.USD), Date.of(4, 1, 2026), subs, "OpenAI API Credits");
            f1.addExpense(Amount.of(8.00, Amount.Currency.SGD), Date.of(5, 1, 2026), leisure, "Badminton booking");
            f1.addTransfer(Amount.of(500, Amount.Currency.SGD), Date.of(6, 1, 2026), savings, f2, "Emergency Fund Transfer");
            f1.addTransfer(Amount.of(200, Amount.Currency.SGD), Date.of(6, 1, 2026), savings, f3, "MariBank Savings");
            f1.addExpense(Amount.of(4.50, Amount.Currency.SGD), Date.of(7, 1, 2026), food, "Late night supper");
            f1.addExpense(Amount.of(3.20, Amount.Currency.SGD), Date.of(8, 1, 2026), transport, "Bus back to Hall");
            f5.addExpense(Amount.of(10.00, Amount.Currency.SGD), Date.of(9, 1, 2026), food, "Clementi Hawker");

            // ==========================================
            // FEBRUARY 2026: CHINESE NEW YEAR & BUDGETING
            // ==========================================
            f1.addIncome(Amount.of(2000, Amount.Currency.SGD), Date.of(1, 2, 2026), salary, "Feb Stipend");
            f1.addIncome(Amount.of(888, Amount.Currency.SGD), Date.of(1, 2, 2026), Category.of("Gift"), "Angbao Total");
            f1.addExpense(Amount.of(50.00, Amount.Currency.SGD), Date.of(2, 2, 2026), food, "CNY Reunion Dinner Share");
            f1.addExpense(Amount.of(3.20, Amount.Currency.SGD), Date.of(3, 2, 2026), transport, "Visiting relatives");
            f1.addExpense(Amount.of(2.50, Amount.Currency.SGD), Date.of(5, 2, 2026), food, "Bak Kwa snack");
            f4.addExpense(Amount.of(10.00, Amount.Currency.USD), Date.of(10, 2, 2026), subs, "GitHub Copilot");
            f1.addExpense(Amount.of(15.00, Amount.Currency.SGD), Date.of(12, 2, 2026), leisure, "Movie: Dune 3");
            f1.addExpense(Amount.of(3.20, Amount.Currency.SGD), Date.of(14, 2, 2026), transport, "Valentine Commute");
            f1.addExpense(Amount.of(120.0, Amount.Currency.SGD), Date.of(14, 2, 2026), food, "Valentine Dinner");
            f1.addTransfer(Amount.of(400, Amount.Currency.SGD), Date.of(15, 2, 2026), savings, f2, "Monthly Stash");
            f1.addExpense(Amount.of(5.50, Amount.Currency.SGD), Date.of(18, 2, 2026), food, "Deck Western");
            f1.addExpense(Amount.of(3.20, Amount.Currency.SGD), Date.of(20, 2, 2026), transport, "MRT to Orchard");
            f1.addExpense(Amount.of(45.00, Amount.Currency.SGD), Date.of(22, 2, 2026), shopping, "New Rucking Socks");
            f5.addExpense(Amount.of(2.00, Amount.Currency.SGD), Date.of(25, 2, 2026), food, "Kopi O");
            f1.addExpense(Amount.of(12.00, Amount.Currency.SGD), Date.of(28, 2, 2026), food, "End of month Treat");

            // ==========================================
            // MARCH 2026: JB TRIP (MYR TESTING)
            // ==========================================
            f1.addIncome(Amount.of(2000, Amount.Currency.SGD), Date.of(1, 3, 2026), salary, "March Stipend");
            f1.addTransfer(Amount.of(100, Amount.Currency.SGD), Date.of(2, 3, 2026), transport, f5, "Withdraw cash for JB");
            f5.addExpense(Amount.of(50.00, Amount.Currency.MYR), Date.of(3, 3, 2026), food, "Kam Long Curry Fish Head");
            f5.addExpense(Amount.of(15.00, Amount.Currency.MYR), Date.of(3, 3, 2026), transport, "Grab in JB");
            f5.addExpense(Amount.of(120.0, Amount.Currency.MYR), Date.of(4, 3, 2026), shopping, "Cheap books at Borders");
            f1.addExpense(Amount.of(3.20, Amount.Currency.SGD), Date.of(5, 3, 2026), transport, "Bus back to SG");
            f1.addExpense(Amount.of(7.50, Amount.Currency.SGD), Date.of(8, 3, 2026), food, "Wonton Mee");
            f4.addExpense(Amount.of(5.00, Amount.Currency.USD), Date.of(10, 3, 2026), subs, "Cloud Storage");
            f1.addExpense(Amount.of(3.20, Amount.Currency.SGD), Date.of(12, 3, 2026), transport, "MRT to School");
            f1.addExpense(Amount.of(14.00, Amount.Currency.SGD), Date.of(15, 3, 2026), food, "Project Meeting Lunch");
            f1.addTransfer(Amount.of(600, Amount.Currency.SGD), Date.of(18, 3, 2026), savings, f2, "Quarterly Bonus Savings");
            f1.addExpense(Amount.of(25.00, Amount.Currency.SGD), Date.of(20, 3, 2026), leisure, "Bouldering @ Kallang");
            f1.addExpense(Amount.of(3.20, Amount.Currency.SGD), Date.of(22, 3, 2026), transport, "Sunday trip");
            f1.addExpense(Amount.of(5.50, Amount.Currency.SGD), Date.of(25, 3, 2026), food, "The Terrace Chicken Rice");
            f4.addExpense(Amount.of(80.00, Amount.Currency.SGD), Date.of(28, 3, 2026), shopping, "Keyboard Keycaps");
            f5.addExpense(Amount.of(3.00, Amount.Currency.SGD), Date.of(30, 3, 2026), food, "Supper Kopi");

            // ==========================================
            // APRIL 2026: FINALS PREP
            // ==========================================
            f1.addIncome(Amount.of(2000, Amount.Currency.SGD), Date.of(1, 4, 2026), salary, "April Stipend");
            f1.addIncome(Amount.of(500, Amount.Currency.SGD), Date.of(1, 4, 2026), Category.of("Allowance"), "Dad April");
            f1.addExpense(Amount.of(10.00, Amount.Currency.SGD), Date.of(1, 4, 2026), food, "Study Fuel Coffee");
            f1.addExpense(Amount.of(3.20, Amount.Currency.SGD), Date.of(2, 4, 2026), transport, "Library Commute");
            f1.addExpense(Amount.of(6.50, Amount.Currency.SGD), Date.of(2, 4, 2026), food, "Ban Mian");
            f1.addExpense(Amount.of(1.20, Amount.Currency.SGD), Date.of(3, 4, 2026), food, "Teh O Kosong");
            f1.addExpense(Amount.of(22.00, Amount.Currency.SGD), Date.of(3, 4, 2026), leisure, "Post-Tutorial Drinks");

            // Boundary/Real-time tests using current date
            f1.addExpense(Amount.of(8.00, Amount.Currency.SGD), Date.current(), Category.of("Sport"), "Badminton court");
            f1.addExpense(Amount.of(20.00, Amount.Currency.SGD), Date.current(), food, "Gourmet Chicken Rice");
            f2.addExpense(Amount.of(50.00, Amount.Currency.SGD), Date.current(), shopping, "Hardware testing");
            f1.addTransfer(Amount.of(100.0, Amount.Currency.SGD), Date.current(), savings, f3, "Automated Save");

        } catch (InvalidDateException e) {
            System.out.println("Revision Logic Error: Check your leap years! " + e.getMessage());
        }

        for (Fund f : funds) {
            System.out.println(f.getHistory(
                    QueryOption.SORT_BY_DATE
            ));
        }
    }
}
