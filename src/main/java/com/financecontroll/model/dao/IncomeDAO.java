package com.financecontroll.model.dao;

import com.financecontroll.model.Income;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class IncomeDAO {

    private Connection connection;

    public IncomeDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public void save(Income income, TransactionDAO transactionDAO) throws SQLException {
        try {
            connection.setAutoCommit(false);
            transactionDAO.save(income);
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw new RuntimeException(e);
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public Income findById(int id) throws SQLException {
        String sql = "SELECT t.id AS transaction_id, t.fk_bank_account_id AS bank_account_id, t.date_transaction, t.transaction_value, t.description, t.is_recurring " +
                "FROM transactions t " +
                "WHERE t.id = ? AND t.transaction_type = 'INCOME'";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return buildIncome(rs);
            }
            return null;
        }
    }

    public List<Income> findByUserId(int userId) throws SQLException {
        String sql = "SELECT t.id AS transaction_id, t.fk_bank_account_id AS bank_account_id, t.date_transaction, t.transaction_value, t.description, t.is_recurring " +
                "FROM transactions t " +
                "INNER JOIN bank_accounts ba ON t.fk_bank_account_id = ba.id " +
                "WHERE ba.fk_user_id = ? AND t.transaction_type = 'INCOME'";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            List<Income> incomes = new ArrayList<>();
            while (rs.next()) {
                incomes.add(buildIncome(rs));
            }
            return incomes;
        }
    }

    public List<Income> findByPeriod(int userId, LocalDate startDate, LocalDate endDate) throws SQLException {
        String sql = "SELECT t.id AS transaction_id, t.fk_bank_account_id AS bank_account_id, t.date_transaction, t.transaction_value, t.description, t.is_recurring " +
                "FROM transactions t " +
                "INNER JOIN bank_accounts ba ON t.fk_bank_account_id = ba.id " +
                "WHERE ba.fk_user_id = ? AND t.transaction_type = 'INCOME' AND t.date_transaction BETWEEN ? AND ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setDate(2, Date.valueOf(startDate));
            stmt.setDate(3, Date.valueOf(endDate));
            ResultSet rs = stmt.executeQuery();
            List<Income> incomes = new ArrayList<>();
            while (rs.next()) {
                incomes.add(buildIncome(rs));
            }
            return incomes;
        }
    }

    public void update(Income income) throws SQLException {
        TransactionDAO transactionDAO = new TransactionDAO();
        transactionDAO.update(income);
    }

    public void delete(int id) throws SQLException {
        TransactionDAO transactionDAO = new TransactionDAO();
        transactionDAO.delete(id);
    }

    private Income buildIncome(ResultSet rs) throws SQLException {
        return new Income(
                rs.getInt("transaction_id"),
                rs.getInt("bank_account_id"),
                rs.getDate("date_transaction").toLocalDate(),
                rs.getDouble("transaction_value"),
                rs.getString("description"),
                rs.getBoolean("is_recurring")
        );
    }
}
