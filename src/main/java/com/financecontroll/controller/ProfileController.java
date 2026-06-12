package com.financecontroll.controller;

import com.financecontroll.model.User;
import com.financecontroll.model.dao.UserDAO;
import com.financecontroll.util.NavigationUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class ProfileController {

    @FXML
    private TextField usernameField;

    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private Label errorLabel;
    @FXML
    private Label successLabel;

    private final UserDAO userDAO = new UserDAO();
    private User currentUser;

    public void setUser(User user) {
        this.currentUser = user;
        usernameField.setText(user.getUsername());
        emailField.setText(user.getUserEmail());
    }

    @FXML
    public void handleSave() throws SQLException {
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (username.isEmpty() || email.isEmpty()) {
            errorLabel.setText("Preencha todos os campos!");
            return;
        }

        if (!password.equals(confirmPassword)) {
            errorLabel.setText("As senhas devem ser iguais!");
            return;
        }

        currentUser.setUsername(username);
        currentUser.setUserEmail(email);

        if (!password.isEmpty()) {
            currentUser.setPassword(password);
        }

        userDAO.update(currentUser);

        successLabel.setText("Dados atualizados com sucesso!");
    }

    @FXML
    private void handleDashboard() {
        NavigationUtil.navigateToDashboard(usernameField, currentUser);
    }

    @FXML
    private void handleContas() {
        NavigationUtil.navigateToBankAccount(usernameField, currentUser);
    }

    @FXML
    private void handleLogout() {
        NavigationUtil.navigateToLogin(usernameField);
    }


}
