package com.financecontroll.model.dao;

import com.financecontroll.model.Category;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {
    Connection connection;

    public CategoryDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public List<Category> getAllCategories() throws SQLException {
        String sql = "SELECT * FROM categories";
        PreparedStatement stmt = connection.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();

        List<Category> categories = new ArrayList<>();

        while(rs.next()){
            categories.add(new Category(rs.getInt("id"), rs.getString("name")));
        }
        return categories;
    }

    public Category getCategoryById(int id) throws SQLException {
        String sql = "SELECT * FROM categories WHERE id = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        if(rs.next()){
            return new Category(rs.getInt("id"), rs.getString("name"));
        }
        return null;
    }
}