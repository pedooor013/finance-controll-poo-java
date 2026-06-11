package com.financecontroll.model.dao;

import com.financecontroll.model.Category;
import com.financecontroll.model.Expense;
import com.financecontroll.model.PaymentType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

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

        if(rs.next()){
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

    public List<Expense> findByUserId(int userId){
    }

    public List<Expense> findByPeriod(int userId, LocalDate startDate, LocalDate endDate){
    }

    public void delete(int id) throws SQLException {
    }
    public boolean update(Expense expense) throws SQLException{}
}
