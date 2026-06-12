package com.financecontroll.model.dao;

import com.financecontroll.model.BankAccount;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BankAccountDAO {

    private Connection connection;

    public BankAccountDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public void save(BankAccount bankAccount) throws SQLException {
        if(existsByNameAndUserId(bankAccount.getBankName(), bankAccount.getUserId())){
            throw new SQLException("Bank account already exists");
        }
        String sql = "INSERT INTO bank_accounts (bank_name, fk_user_id) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, bankAccount.getBankName());
            stmt.setInt(2, bankAccount.getUserId());
            stmt.executeUpdate();
        }
    }

    public List<BankAccount> findBankAccountsByName(String bankName) throws SQLException {
        String sql = "SELECT * FROM bank_accounts WHERE bank_name LIKE ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + bankName.toUpperCase() + "%");
            ResultSet rs = stmt.executeQuery();
            List<BankAccount> accounts = new ArrayList<BankAccount>();
            while (rs.next()) {
                accounts.add(new BankAccount(rs.getInt("id"),
                        rs.getString("bank_name"),
                        rs.getInt("fk_user_id")));
            }
            return accounts;
        }
    }


    public List<BankAccount> findBankAccountsByUserId(int user_id) throws SQLException {
        String sql = "SELECT * FROM bank_accounts WHERE fk_user_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, user_id);
            ResultSet rs = stmt.executeQuery();
            List<BankAccount> accounts = new ArrayList<BankAccount>();
            while (rs.next()) {
                accounts.add(new BankAccount(rs.getInt("id"),
                        rs.getString("bank_name"),
                        rs.getInt("fk_user_id")));
            }
            return accounts;
        }
    }

    private boolean existsByNameAndUserId(String bankName, int userId) throws SQLException {
        String sql = "SELECT id FROM bank_accounts WHERE bank_name = ? AND fk_user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, bankName);
            stmt.setInt(2, userId);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
    }
}
