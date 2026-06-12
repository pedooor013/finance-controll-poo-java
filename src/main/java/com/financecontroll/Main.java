package com.financecontroll;

import com.financecontroll.model.*;
import com.financecontroll.model.dao.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

class Main {
    public static void main(String[] args) throws SQLException {
        // 1. Conecta ao banco
        DatabaseConnection.getInstance();
        System.out.println("[OK] Connected to database");

        // 2. Testa UserDAO
        UserDAO userDAO = new UserDAO();
        User user = new User("TestUser", "testuser@email.com", "senha123");
        userDAO.save(user);
        User savedUser = userDAO.findByEmail("testuser@email.com");
        System.out.println("[OK] User saved: " + savedUser);

        boolean loginOk = userDAO.checkUser("testuser@email.com", "senha123");
        System.out.println("[OK] checkUser: " + loginOk);

        // 3. Testa BankAccountDAO
        BankAccountDAO bankAccountDAO = new BankAccountDAO();
        BankAccount bankAccount = new BankAccount("INTER", savedUser.getId());
        bankAccountDAO.save(bankAccount);
        List<BankAccount> accounts = bankAccountDAO.findBankAccountsByUserId(savedUser.getId());
        System.out.println("[OK] BankAccount saved: " + accounts);

        List<BankAccount> accountsByName = bankAccountDAO.findBankAccountsByName("INTER");
        System.out.println("[OK] BankAccount findByName: " + accountsByName);

        // 4. Testa CategoryDAO
        CategoryDAO categoryDAO = new CategoryDAO();
        Category category = categoryDAO.getCategoryById(1);
        System.out.println("[OK] Category: " + category);

        List<?> allCategories = categoryDAO.getAllCategories();
        System.out.println("[OK] All categories: " + allCategories);

        // 5. Testa ExpenseDAO - save
        TransactionDAO transactionDAO = new TransactionDAO();
        ExpenseDAO expenseDAO = new ExpenseDAO();
        Expense expense = new Expense(accounts.get(0).getId(), 89.90, "Cinema", false, 1, 0, "TestUser", PaymentType.DEBITO, category);
        expenseDAO.save(expense, transactionDAO);
        System.out.println("[OK] Expense saved!");

        // 6. Testa findByUserId
        List<Expense> expensesByUser = expenseDAO.findByUserId(savedUser.getId());
        System.out.println("[OK] Expenses by user: " + expensesByUser);

        // 7. Testa findByPeriod
        List<Expense> expensesByPeriod = expenseDAO.findByPeriod(savedUser.getId(), LocalDate.now().minusMonths(1), LocalDate.now());
        System.out.println("[OK] Expenses by period: " + expensesByPeriod);

        // 8. Testa findById
        if (!expensesByUser.isEmpty()) {
            int expenseId = expensesByUser.get(0).getId();
            Expense foundExpense = expenseDAO.findById(expenseId);
            System.out.println("[OK] Expense findById: " + foundExpense);

            // 9. Testa update
            foundExpense.setDescription("Cinema Updated");
            expenseDAO.update(foundExpense);
            System.out.println("[OK] Expense updated!");

            // 10. Testa delete
            expenseDAO.delete(expenseId);
            System.out.println("[OK] Expense deleted!");
        }

        // 11. Testa IncomeDAO - save
        IncomeDAO incomeDAO = new IncomeDAO();
        Income income = new Income(accounts.get(0).getId(), 5000.00, "Salario", false);
        incomeDAO.save(income, transactionDAO);
        System.out.println("[OK] Income saved!");

        // 12. Testa findByUserId
        List<Income> incomesByUser = incomeDAO.findByUserId(savedUser.getId());
        System.out.println("[OK] Incomes by user: " + incomesByUser);

        // 13. Testa findByPeriod
        List<Income> incomesByPeriod = incomeDAO.findByPeriod(savedUser.getId(), LocalDate.now().minusMonths(1), LocalDate.now());
        System.out.println("[OK] Incomes by period: " + incomesByPeriod);

        // 14. Testa findById
        if (!incomesByUser.isEmpty()) {
            int incomeId = incomesByUser.get(0).getId();
            Income foundIncome = incomeDAO.findById(incomeId);
            System.out.println("[OK] Income findById: " + foundIncome);

            // 15. Testa update
            foundIncome.setDescription("Salario Updated");
            incomeDAO.update(foundIncome);
            System.out.println("[OK] Income updated!");

            // 16. Testa delete
            incomeDAO.delete(incomeId);
            System.out.println("[OK] Income deleted!");
        }

        System.out.println("\n--- All tests completed! ---");
    }
}