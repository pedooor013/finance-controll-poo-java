package com.financecontroll;

import com.financecontroll.model.BankAccount;
import com.financecontroll.model.User;
import com.financecontroll.model.dao.BankAccountDAO;
import com.financecontroll.model.dao.DatabaseConnection;
import com.financecontroll.model.dao.UserDAO;

import java.sql.SQLException;

class Main {
    public static void main(String[] args) throws SQLException {
        DatabaseConnection.getInstance().getConnection();
        System.out.println("Connected to database");


        UserDAO userDAO = new UserDAO();


        BankAccountDAO bankAccountDAO = new BankAccountDAO();

        BankAccount bankAccount = new BankAccount("nubank", 1);
        bankAccountDAO.save(bankAccount);
        System.out.println("Bank account saved!");

        BankAccount bankAccount2 = new BankAccount("bradesco", 1);
        bankAccountDAO.save(bankAccount2);
        System.out.println("Bank account saved!");

        System.out.println(bankAccountDAO.findBankAccountsByName("nubank"));
        System.out.println(bankAccountDAO.findBankAccountsByUserId(1));;

    }
}