package com.financecontroll.controller;

import com.financecontroll.model.User;
import com.financecontroll.model.dao.UserDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class RegisterController {

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
            return;
        }

    }

    @FXML
    private void handleGoToLogin() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login.fxml"));
        Stage stage = (Stage) errorLabel.getScene().getWindow();
        stage.setScene(new Scene(loader.load()));
    }

}
