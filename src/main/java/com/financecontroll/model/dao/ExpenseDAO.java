package com.financecontroll.model.dao;

import com.financecontroll.model.Expense;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ExpenseDAO {

    private Connection connection;

    public ExpenseDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public void save(Expense expense, TransactionDAO transactionDAO) throws SQLException {
        try {
            connection.setAutoCommit(false);
            int transactionId = transactionDAO.save(expense);

            String sql = "INSERT INTO expenses(fk_transaction_id, installments_total, installments_paid, payment_responsible, payment_type, fk_categories_id) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, transactionId);
            stmt.setInt(2, expense.getInstallmentsTotal());
            stmt.setInt(3, expense.getInstallmentsPaid());
            stmt.setString(4, expense.getPaymentResponsible());
            stmt.setString(5, expense.getPaymentType().toString());
            stmt.setInt(6, expense.getCategory().getId());

            stmt.executeUpdate();
            connection.commit();

        } catch (SQLException e) {
            connection.rollback();
            throw new RuntimeException(e);
        } finally {
            connection.setAutoCommit(true);
        }
    }
}
