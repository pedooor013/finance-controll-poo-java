package com.financecontroll.util;

import com.financecontroll.controller.BankAccountController;
import com.financecontroll.controller.DashboardController;
import com.financecontroll.controller.ProfileController;
import com.financecontroll.model.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class NavigationUtil {

    public static void navigateToDashboard(Node node, User user) {
        try {
            FXMLLoader loader = new FXMLLoader(NavigationUtil.class.getResource("/view/dashboard.fxml"));
            loader.load();
            DashboardController controller = loader.getController();
            controller.setUser(user);
            applyScene(node, new Scene(loader.getRoot()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void navigateToProfile(Node node, User user) {
        try {
            FXMLLoader loader = new FXMLLoader(NavigationUtil.class.getResource("/view/profile.fxml"));
            loader.load();
            ProfileController controller = loader.getController();
            controller.setUser(user);
            applyScene(node, new Scene(loader.getRoot()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void navigateToBankAccount(Node node, User user) {
        try {
            FXMLLoader loader = new FXMLLoader(NavigationUtil.class.getResource("/view/bankaccount.fxml"));
            loader.load();
            BankAccountController controller = loader.getController();
            controller.setUser(user);
            applyScene(node, new Scene(loader.getRoot()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void navigateToLogin(Node node) {
        try {
            FXMLLoader loader = new FXMLLoader(NavigationUtil.class.getResource("/view/login.fxml"));
            applyScene(node, new Scene(loader.load()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Stage getStage(Node node) {
        return (Stage) node.getScene().getWindow();
    }

    private static void applyScene(Node node, Scene scene) {
        Stage stage = getStage(node);
        boolean wasMaximized = stage.isMaximized();
        if (wasMaximized) {
            stage.setMaximized(false);
        }
        stage.setScene(scene);
        if (wasMaximized) {
            stage.setMaximized(true);
        }
    }
}
