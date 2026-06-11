package com.financecontroll.model.dao;

import com.financecontroll.model.Category;
import com.financecontroll.model.Expense;
import com.financecontroll.model.PaymentType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static javax.swing.UIManager.getString;

public class ExpenseDAO {

    private Connection connection;

    public ExpenseDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public void save(Expense expense, TransactionDAO transactionDAO) throws SQLException {
        try {
            connection.setAutoCommit(false);
            int transactionId = transactionDAO.save(expense);

            String sql = "INSERT INTO expenses(fk_transactions_id, installments_total, installments_paid, payment_responsible, payment_type, fk_categories_id) VALUES (?, ?, ?, ?, ?, ?)";
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

    //Proximas implementaçoes
    public Expense findById(int id) throws SQLException {
        String sql = "SELECT " +
                "* " +
                "FROM transactions t " +
                "INNER JOIN " +
                "expenses e " +
                "ON " +
                "t.id = e.fk_transactions_id " +
                "WHERE t.id = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            Category category = new CategoryDAO().getCategoryById(rs.getInt("fk_categories_id"));

            return new Expense(rs.getInt("id"),
                    rs.getInt("fk_bank_account_id"),
                    rs.getDate("date_transaction").toLocalDate(),
                    rs.getDouble("transaction_value"),
                    rs.getString("description"),
                    rs.getBoolean("is_recurring"),
                    rs.getInt("installments_total"),
                    rs.getInt("installments_paid"),
                    rs.getString("payment_responsible"),
                    PaymentType.valueOf(rs.getString("payment_type")),
                    category
            );
        }

        return null;
    }
    public List<Expense> findByUserId(int userId) throws SQLException {
        String sql = "SELECT " +
                "t.id AS transaction_id, ba.id AS bank_account_id, ba.name, t.date_transaction, t.transaction_value, t.description, t.is_recurring, e.installments_total, e.installments_paid, e.payment_responsible, e.payment_type, e.fk_categories_id " +
                "FROM " +
                "transactions t " +
                "INNER JOIN " +
                "expenses e " +
                "ON t.id = e.fk_transactions_id " +
                "INNER JOIN " +
                "bank_accounts ba " +
                "ON t.fk_bank_account_id = ba.id " +
                "WHERE ba.fk_user_id = ?";

        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, userId);
        ResultSet rs = stmt.executeQuery();

        List<Expense> expenses = new ArrayList<Expense>();

        while (rs.next()) {
            expenses.add(new Expense(rs.getInt("t.id"), rs.getInt("ba.id"),
                    rs.getDate("t.transaction_date").toLocalDate(), rs.getDouble("t.transaction_value"),
                    rs.getString("t.description"), rs.getBoolean("t.is_recurring"),
                    rs.getInt("e.installments_total "), rs.getInt("e.installments_paid"),
                    rs.getString("e.payment_responsible"), PaymentType.valueOf(rs.getString("e.payment_type")),
                    new CategoryDAO().getCategoryById(rs.getInt("e.fk_categories_id"))));
        }
        return expenses;
    }

    public List<Expense> findByPeriod(int userId, LocalDate startDate, LocalDate endDate) {

    }

    public void delete(int id) throws SQLException {
    }

    public boolean update(Expense expense) throws SQLException {
    }
}
