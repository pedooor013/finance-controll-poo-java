package com.financecontroll;

import com.financecontroll.model.User;
import com.financecontroll.model.dao.DatabaseConnection;
import com.financecontroll.model.dao.UserDAO;

import java.sql.SQLException;

class Main {
    public static void main(String[] args) throws SQLException {
        DatabaseConnection.getInstance().getConnection();
        System.out.println("Connected to database");


        UserDAO userDAO = new UserDAO();

    // Cadastra um usuário
        User user = new User("Pedro", "pedro@email.com", "123456");
        userDAO.save(user);
        System.out.println("User saved!");

    // Verifica login
        boolean login = userDAO.checkUser("pedro@email.com", "123456");
        System.out.println("Login: " + login);
    }
}