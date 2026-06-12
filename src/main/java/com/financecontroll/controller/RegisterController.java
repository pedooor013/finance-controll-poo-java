package com.financecontroll.controller;

import com.financecontroll.model.User;
import com.financecontroll.model.dao.UserDAO;
import com.financecontroll.util.NavigationUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.SQLException;

public class RegisterController {

    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Label errorLabel;

    private final UserDAO userDAO = new UserDAO();

    @FXML
    private void handleRegister() throws Exception {
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || username.isEmpty()) {
            errorLabel.setText("Preencha todos os campos!");
            return;
        }

        if (userDAO.findByEmail(email) != null) {
            errorLabel.setText("Email já cadastrado!");
            return;
        }

        if (password.equals(confirmPassword)) {
            userDAO.save(new User(username, email, password));
            handleGoToLogin();
        } else {
            errorLabel.setText("As senhas devem ser iguais!");
        }
    }

    @FXML
    private void handleGoToLogin() {
        NavigationUtil.navigateToLogin(errorLabel);
    }
}
