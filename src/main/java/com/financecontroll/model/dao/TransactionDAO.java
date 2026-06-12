package com.financecontroll.model.dao;

import com.financecontroll.model.Expense;
import com.financecontroll.model.Transaction;

import java.sql.*;

public class TransactionDAO {

    private Connection connection;

    public TransactionDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public int save(Transaction transaction) throws SQLException {
        String sql = "INSERT INTO transactions (fk_bank_account_id, date_transaction, transaction_value, description, is_recurring, transaction_type) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, transaction.getBankAccountId());
            stmt.setDate(2, Date.valueOf(transaction.getDateTimeTransaction()));
            stmt.setDouble(3, transaction.getTransactionValue());
            stmt.setString(4, transaction.getDescription());
            stmt.setBoolean(5, transaction.getIsRecurring());
            stmt.setString(6, String.valueOf(transaction.getTransactionType()));
            stmt.executeUpdate();
            ResultSet keys = stmt.getGeneratedKeys();
            if(keys.next()){
                return keys.getInt(1);
            }
        }
        throw new RuntimeException("Failed to save transaction");
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM transactions WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }


}
