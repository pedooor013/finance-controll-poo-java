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
import java.sql.SQLException;

public class LoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    private final UserDAO userDAO = new UserDAO();

    @FXML
    private void handleLogin() throws SQLException {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Preencha todos os campos!");
            return;
        }

        if (userDAO.checkUser(email, password)) {
            try {
                User user = userDAO.findByEmail(email);
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/dashboard.fxml"));
                loader.load();
                DashboardController controller = loader.getController();
                controller.setUser(user);
                Stage stage = (Stage) emailField.getScene().getWindow();
                stage.setScene(new Scene(loader.getRoot()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            errorLabel.setText("Email ou senha inválidos!");
        }
    }

    @FXML
    private void handleGoToRegister() {
        try {
            Stage stage = (Stage) emailField.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/register.fxml"));
            stage.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
