package com.financecontroll.controller;

import com.financecontroll.model.*;
import com.financecontroll.model.dao.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class OperationController implements Initializable {

    @FXML private ToggleButton expenseBtn;
    @FXML private ToggleButton incomeBtn;
    @FXML private TextField descriptionField;
    @FXML private TextField valueField;
    @FXML private DatePicker datePicker;
    @FXML private javafx.scene.layout.VBox expenseFields;
    @FXML private ComboBox<Category> categoryCombo;
    @FXML private ComboBox<PaymentType> paymentTypeCombo;
    @FXML private TextField installmentsField;
    @FXML private TextField responsibleField;
    @FXML private Label errorLabel;

    private User currentUser;
    private boolean isExpense = true;

    private final ExpenseDAO expenseDAO = new ExpenseDAO();
    private final IncomeDAO incomeDAO = new IncomeDAO();
    private final TransactionDAO transactionDAO = new TransactionDAO();
    private final CategoryDAO categoryDAO = new CategoryDAO();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        datePicker.setValue(LocalDate.now());
        expenseBtn.setSelected(true);
        expenseFields.setVisible(true);
        expenseFields.setManaged(true);

        try {
            List<Category> categories = categoryDAO.getAllCategories();
            categoryCombo.getItems().addAll(categories);
            categoryCombo.setConverter(new javafx.util.StringConverter<>() {
                @Override public String toString(Category c) { return c != null ? c.getName() : ""; }
                @Override public Category fromString(String s) { return null; }
            });
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        paymentTypeCombo.getItems().addAll(PaymentType.values());
    }

    public void setUser(User user) {
        this.currentUser = user;
    }

    @FXML
    private void handleToggle() {
        if (expenseBtn.isSelected()) {
            isExpense = true;
            incomeBtn.setSelected(false);
            expenseFields.setVisible(true);
            expenseFields.setManaged(true);
        } else {
            isExpense = false;
            expenseBtn.setSelected(false);
            expenseFields.setVisible(false);
            expenseFields.setManaged(false);
        }
    }

    @FXML
    private void handleSave() throws SQLException {
        String description = descriptionField.getText();
        String valueText = valueField.getText();
        LocalDate date = datePicker.getValue();

        if (description.isEmpty() || valueText.isEmpty() || date == null) {
            errorLabel.setText("Preencha todos os campos obrigatórios!");
            return;
        }

        double value;
        try {
            value = Double.parseDouble(valueText.replace(",", "."));
        } catch (NumberFormatException e) {
            errorLabel.setText("Valor inválido!");
            return;
        }

        List<com.financecontroll.model.BankAccount> accounts = new BankAccountDAO().findBankAccountsByUserId(currentUser.getId());
        if (accounts.isEmpty()) {
            errorLabel.setText("Cadastre uma conta bancária primeiro!");
            return;
        }

        int bankAccountId = accounts.get(0).getId();

        if (isExpense) {
            if (categoryCombo.getValue() == null || paymentTypeCombo.getValue() == null || responsibleField.getText().isEmpty()) {
                errorLabel.setText("Preencha todos os campos da despesa!");
                return;
            }
            int installments = 1;
            try {
                installments = Integer.parseInt(installmentsField.getText().isEmpty() ? "1" : installmentsField.getText());
            } catch (NumberFormatException e) {
                errorLabel.setText("Número de parcelas inválido!");
                return;
            }
            Expense expense = new Expense(bankAccountId, value, description, false, installments, 0,
                    responsibleField.getText(), paymentTypeCombo.getValue(), categoryCombo.getValue());
            expense.setDateTimeTransaction(date);
            expenseDAO.save(expense, transactionDAO);
        } else {
            Income income = new Income(bankAccountId, value, description, false);
            income.setDateTimeTransaction(date);
            incomeDAO.save(income, transactionDAO);
        }

        ((Stage) descriptionField.getScene().getWindow()).close();
    }
}
