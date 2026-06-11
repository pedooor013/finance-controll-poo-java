package com.financecontroll;

import com.financecontroll.model.*;
import com.financecontroll.model.dao.*;

import java.sql.SQLException;
import java.util.List;

class Main {
    public static void main(String[] args) throws SQLException {
        // 1. Conecta ao banco
        DatabaseConnection.getInstance();
        System.out.println("Connected to database");

        // 2. Cria e salva usuário
        UserDAO userDAO = new UserDAO();
        User user = new User("Maria", "maria@email.com", "senha123");
        userDAO.save(user);
        User savedUser = userDAO.findByEmail("maria@email.com");
        System.out.println("User saved: " + savedUser.getUsername());

        // 3. Cria e salva conta bancária
        BankAccountDAO bankAccountDAO = new BankAccountDAO();
        BankAccount bankAccount = new BankAccount("INTER", savedUser.getId());
        bankAccountDAO.save(bankAccount);
        List<BankAccount> accounts = bankAccountDAO.findBankAccountsByUserId(savedUser.getId());
        System.out.println("BankAccount saved: " + accounts.get(0).getBankName());

        // 4. Busca categoria
        CategoryDAO categoryDAO = new CategoryDAO();
        Category category = categoryDAO.getCategoryById(2);
        System.out.println("Category: " + category.getName());

        // 5. Cria e salva despesa
        TransactionDAO transactionDAO = new TransactionDAO();
        ExpenseDAO expenseDAO = new ExpenseDAO();
        Expense expense = new Expense(accounts.get(0).getId(), 89.90, "Cinema", false, 1, 0, "Maria", PaymentType.DEBITO, category);
        expenseDAO.save(expense, transactionDAO);
        System.out.println("Expense saved!");
    }
}