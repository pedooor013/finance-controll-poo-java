package com.financecontroll.controller;

import com.financecontroll.model.*;
import com.financecontroll.model.dao.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class OperationController implements Initializable {

    @FXML
    private ToggleButton expenseBtn;
    @FXML
    private ToggleButton incomeBtn;
    @FXML
    private ComboBox<BankAccount> bankAccountCombo;
    @FXML
    private TextField descriptionField;
    @FXML
    private TextField valueField;
    @FXML
    private DatePicker datePicker;
    @FXML
    private VBox expenseFields;
    @FXML
    private ComboBox<Category> categoryCombo;
    @FXML
    private ComboBox<PaymentType> paymentTypeCombo;
    @FXML
    private VBox installmentsPanel;
    @FXML
    private TextField installmentsField;
    @FXML
    private TextField responsibleField;
    @FXML
    private CheckBox recurringCheck;
    @FXML
    private Label errorLabel;

    private User currentUser;
    private boolean isExpense = true;

    private final ExpenseDAO expenseDAO = new ExpenseDAO();
    private final IncomeDAO incomeDAO = new IncomeDAO();
    private final TransactionDAO transactionDAO = new TransactionDAO();
    private final CategoryDAO categoryDAO = new CategoryDAO();
    private final BankAccountDAO bankAccountDAO = new BankAccountDAO();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        datePicker.setValue(LocalDate.now());

        ToggleGroup group = new ToggleGroup();
        expenseBtn.setToggleGroup(group);
        incomeBtn.setToggleGroup(group);
        expenseBtn.setSelected(true);
        expenseFields.setVisible(true);
        expenseFields.setManaged(true);

        group.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == expenseBtn) {
                isExpense = true;
                expenseFields.setVisible(true);
                expenseFields.setManaged(true);
                recurringCheck.setText("Despesa recorrente (repete todo mês)");
            } else if (newVal == incomeBtn) {
                isExpense = false;
                expenseFields.setVisible(false);
                expenseFields.setManaged(false);
                recurringCheck.setText("Receita recorrente (repete todo mês)");
            } else {
                oldVal.setSelected(true);
            }
        });

        paymentTypeCombo.getItems().addAll(PaymentType.values());
        paymentTypeCombo.setConverter(new StringConverter<>() {
            @Override
            public String toString(PaymentType p) {
                return p != null ? p.toString() : "";
            }

            @Override
            public PaymentType fromString(String s) {
                return null;
            }
        });

        paymentTypeCombo.valueProperty().addListener((obs, oldVal, newVal) -> {
            boolean isCredito = newVal == PaymentType.CREDITO;
            installmentsPanel.setVisible(isCredito);
            installmentsPanel.setManaged(isCredito);
        });

        try {
            List<Category> categories = categoryDAO.getAllCategories();
            categoryCombo.getItems().addAll(categories);
            categoryCombo.setConverter(new StringConverter<>() {
                @Override
                public String toString(Category c) {
                    return c != null ? c.getName() : "";
                }

                @Override
                public Category fromString(String s) {
                    return null;
                }
            });
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void setUser(User user) {
        this.currentUser = user;
        try {
            List<BankAccount> accounts = bankAccountDAO.findBankAccountsByUserId(user.getId());
            bankAccountCombo.getItems().addAll(accounts);
            bankAccountCombo.setConverter(new StringConverter<>() {
                @Override
                public String toString(BankAccount b) {
                    return b != null ? b.getBankName() : "";
                }

                @Override
                public BankAccount fromString(String s) {
                    return null;
                }
            });
            if (!accounts.isEmpty()) bankAccountCombo.setValue(accounts.get(0));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void setTransaction(Transaction t) {
        descriptionField.setText(t.getDescription());
        valueField.setText(String.valueOf(t.getTransactionValue()));
        datePicker.setValue(t.getDateTimeTransaction());
        recurringCheck.setSelected(t.getIsRecurring());

        if (t instanceof Expense expense) {
            expenseBtn.setSelected(true);
            incomeBtn.setSelected(false);
            isExpense = true;
            expenseFields.setVisible(true);
            expenseFields.setManaged(true);
            categoryCombo.setValue(expense.getCategory());
            paymentTypeCombo.setValue(expense.getPaymentType());
            installmentsField.setText(String.valueOf(expense.getInstallmentsTotal()));
            responsibleField.setText(expense.getPaymentResponsible());
            recurringCheck.setText("Despesa recorrente (repete todo mês)");
        } else {
            incomeBtn.setSelected(true);
            expenseBtn.setSelected(false);
            isExpense = false;
            expenseFields.setVisible(false);
            expenseFields.setManaged(false);
            recurringCheck.setText("Receita recorrente (repete todo mês)");
        }
    }

    @FXML
    private void handleSave() {
        String description = descriptionField.getText();
        String valueText = valueField.getText();
        LocalDate date = datePicker.getValue();
        BankAccount bankAccount = bankAccountCombo.getValue();

        if (description.isEmpty() || valueText.isEmpty() || date == null || bankAccount == null) {
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

        try {
            if (isExpense) {
                if (categoryCombo.getValue() == null || paymentTypeCombo.getValue() == null) {
                    errorLabel.setText("Preencha todos os campos da despesa!");
                    return;
                }
                int installments = 1;
                if (paymentTypeCombo.getValue() == PaymentType.CREDITO) {
                    try {
                        installments = Integer.parseInt(installmentsField.getText().isEmpty() ? "1" : installmentsField.getText());
                    } catch (NumberFormatException e) {
                        errorLabel.setText("Número de parcelas inválido!");
                        return;
                    }
                }
                Expense expense = new Expense(bankAccount.getId(), value, description, recurringCheck.isSelected(), installments, 0,
                        responsibleField.getText(), paymentTypeCombo.getValue(), categoryCombo.getValue());
                expense.setDateTimeTransaction(date);
                expenseDAO.save(expense, transactionDAO);
            } else {
                Income income = new Income(bankAccount.getId(), value, description, recurringCheck.isSelected());
                income.setDateTimeTransaction(date);
                incomeDAO.save(income, transactionDAO);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        ((Stage) descriptionField.getScene().getWindow()).close();
    }
}
