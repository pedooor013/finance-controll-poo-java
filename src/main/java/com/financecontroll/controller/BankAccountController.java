package com.financecontroll.controller;

import com.financecontroll.model.BankAccount;
import com.financecontroll.model.User;
import com.financecontroll.model.dao.BankAccountDAO;
import com.financecontroll.util.NavigationUtil;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.util.List;

public class BankAccountController {

    @FXML private VBox formPanel;
    @FXML private TextField bankNameField;
    @FXML private Label errorLabel;
    @FXML private VBox accountList;
    @FXML private Button relatoriosBtn;

    private User currentUser;
    private final BankAccountDAO bankAccountDAO = new BankAccountDAO();

    public void setUser(User user) {
        this.currentUser = user;
        Tooltip tooltip = new Tooltip("Em Breve");
        Tooltip.install(relatoriosBtn, tooltip);
        loadAccounts();
    }

    private void loadAccounts() {
        accountList.getChildren().clear();
        try {
            List<BankAccount> accounts = bankAccountDAO.findBankAccountsByUserId(currentUser.getId());
            if (accounts.isEmpty()) {
                Label empty = new Label("Nenhuma conta cadastrada.");
                empty.getStyleClass().add("form-subtitle");
                accountList.getChildren().add(empty);
                return;
            }
            for (BankAccount account : accounts) {
                accountList.getChildren().add(buildAccountItem(account));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private HBox buildAccountItem(BankAccount account) {
        HBox row = new HBox(12);
        row.setAlignment(Pos.CENTER_LEFT);
        row.getStyleClass().add("transaction-item");

        Label icon = new Label("🏦");
        icon.getStyleClass().add("transaction-description");

        Label name = new Label(account.getBankName());
        name.getStyleClass().add("transaction-description");
        HBox.setHgrow(name, Priority.ALWAYS);

        Button deleteBtn = new Button("🗑 Remover");
        deleteBtn.getStyleClass().add("delete-button");
        deleteBtn.setOnAction(e -> handleDelete(account));

        row.getChildren().addAll(icon, name, deleteBtn);
        return row;
    }

    private void handleDelete(BankAccount account) {
        try {
            bankAccountDAO.delete(account.getId());
            loadAccounts();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void handleNovaConta() {
        formPanel.setVisible(true);
        formPanel.setManaged(true);
        bankNameField.clear();
        errorLabel.setText("");
    }

    @FXML
    private void handleCancelar() {
        formPanel.setVisible(false);
        formPanel.setManaged(false);
    }

    @FXML
    private void handleSave() throws SQLException {
        String bankName = bankNameField.getText().trim();
        if (bankName.isEmpty()) {
            errorLabel.setText("Informe o nome do banco!");
            return;
        }
        try {
            bankAccountDAO.save(new BankAccount(bankName, currentUser.getId()));
            handleCancelar();
            loadAccounts();
        } catch (SQLException e) {
            errorLabel.setText("Conta já cadastrada!");
        }
    }

    @FXML private void handleDashboard() { NavigationUtil.navigateToDashboard(accountList, currentUser); }
    @FXML private void handlePerfil() { NavigationUtil.navigateToProfile(accountList, currentUser); }
    @FXML private void handleLogout() { NavigationUtil.navigateToLogin(accountList); }
}
