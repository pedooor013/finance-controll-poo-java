package com.financecontroll.controller;

import com.financecontroll.model.Expense;
import com.financecontroll.model.Income;
import com.financecontroll.model.Transaction;
import com.financecontroll.model.User;
import com.financecontroll.model.dao.ExpenseDAO;
import com.financecontroll.model.dao.IncomeDAO;
import com.financecontroll.model.dao.TransactionDAO;
import com.financecontroll.util.NavigationUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DashboardController {

    @FXML private Label welcomeLabel;
    @FXML private Label monthLabel;
    @FXML private Label totalIncomeLabel;
    @FXML private Label totalExpenseLabel;
    @FXML private Label balanceLabel;
    @FXML private VBox transactionList;
    @FXML private Button relatoriosBtn;

    private User currentUser;
    private final ExpenseDAO expenseDAO = new ExpenseDAO();
    private final IncomeDAO incomeDAO = new IncomeDAO();

    public void setUser(User user) {
        this.currentUser = user;
        processRecurringExpenses();
        loadDashboard();
    }

    private void processRecurringExpenses() {
        try {
            LocalDate lastMonthStart = LocalDate.now().minusMonths(1).withDayOfMonth(1);
            LocalDate lastMonthEnd = LocalDate.now().withDayOfMonth(1).minusDays(1);
            LocalDate thisMonthStart = LocalDate.now().withDayOfMonth(1);
            LocalDate thisMonthEnd = LocalDate.now();

            List<Expense> lastMonth = expenseDAO.findByPeriod(currentUser.getId(), lastMonthStart, lastMonthEnd);
            List<Expense> thisMonth = expenseDAO.findByPeriod(currentUser.getId(), thisMonthStart, thisMonthEnd);

            for (Expense expense : lastMonth) {
                if (!expense.getIsRecurring()) continue;
                boolean alreadyCreated = thisMonth.stream()
                        .anyMatch(e -> e.getDescription().equals(expense.getDescription()) && e.getIsRecurring());
                if (!alreadyCreated) {
                    Expense newExpense = (Expense) expense.duplicate();
                    newExpense.setDateTimeTransaction(thisMonthStart);
                    expenseDAO.save(newExpense, new TransactionDAO());
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadDashboard() {
        transactionList.getChildren().clear();

        welcomeLabel.setText("Olá, " + currentUser.getUsername() + "!");
        monthLabel.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM yyyy")));

        Tooltip tooltip = new Tooltip("Em Breve");
        Tooltip.install(relatoriosBtn, tooltip);

        try {
            loadCurrentMonth();
            loadPreviousMonths();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadCurrentMonth() throws SQLException {
        LocalDate start = LocalDate.now().withDayOfMonth(1);
        LocalDate end = LocalDate.now();

        List<Expense> expenses = expenseDAO.findByPeriod(currentUser.getId(), start, end);
        List<Income> incomes = incomeDAO.findByPeriod(currentUser.getId(), start, end);

        double totalIncome = incomes.stream().mapToDouble(Income::getTransactionValue).sum();
        double totalExpense = expenses.stream().mapToDouble(Expense::getTransactionValue).sum();
        double balance = totalIncome - totalExpense;

        totalIncomeLabel.setText(String.format("R$ %.2f", totalIncome));
        totalExpenseLabel.setText(String.format("R$ %.2f", totalExpense));
        balanceLabel.setText(String.format("R$ %.2f", balance));

        Label monthTitle = new Label("Este mês");
        monthTitle.getStyleClass().add("transaction-month-label");
        transactionList.getChildren().add(monthTitle);

        List<Transaction> all = new ArrayList<>();
        all.addAll(expenses);
        all.addAll(incomes);
        all.sort(Comparator.comparing(Transaction::getDateTimeTransaction).reversed());

        for (Transaction t : all) {
            transactionList.getChildren().add(buildTransactionItem(t, false));
        }
    }

    private void loadPreviousMonths() throws SQLException {
        LocalDate start = LocalDate.now().minusYears(1).withDayOfMonth(1);
        LocalDate end = LocalDate.now().withDayOfMonth(1).minusDays(1);

        List<Expense> expenses = expenseDAO.findByPeriod(currentUser.getId(), start, end);
        List<Income> incomes = incomeDAO.findByPeriod(currentUser.getId(), start, end);

        List<Transaction> all = new ArrayList<>();
        all.addAll(expenses);
        all.addAll(incomes);
        all.sort(Comparator.comparing(Transaction::getDateTimeTransaction).reversed());

        Map<String, List<Transaction>> grouped = all.stream().collect(
                Collectors.groupingBy(
                        t -> t.getDateTimeTransaction().format(DateTimeFormatter.ofPattern("MMMM yyyy")),
                        LinkedHashMap::new,
                        Collectors.toList()
                )
        );

        grouped.forEach((month, transactions) -> {
            Label monthTitle = new Label(month);
            monthTitle.getStyleClass().add("transaction-month-label");
            transactionList.getChildren().add(monthTitle);

            for (Transaction t : transactions) {
                transactionList.getChildren().add(buildTransactionItem(t, true));
            }
        });
    }

    private HBox buildTransactionItem(Transaction t, boolean old) {
        HBox row = new HBox(12);
        row.setAlignment(Pos.CENTER_LEFT);
        row.getStyleClass().add(old ? "transaction-item-old" : "transaction-item");

        boolean isExpense = t instanceof Expense;

        Label arrow = new Label(isExpense ? "↓" : "↑");
        arrow.getStyleClass().add(isExpense ? "arrow-expense" : "arrow-income");

        VBox info = new VBox(2);
        HBox.setHgrow(info, Priority.ALWAYS);
        Label desc = new Label(t.getDescription());
        desc.getStyleClass().add("transaction-description");
        Label date = new Label(t.getDateTimeTransaction().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        date.getStyleClass().add("transaction-date");
        info.getChildren().addAll(desc, date);

        Label value = new Label(String.format("R$ %.2f", t.getTransactionValue()));
        value.getStyleClass().add(isExpense ? "transaction-value-expense" : "transaction-value-income");

        Button editBtn = new Button("✏");
        editBtn.getStyleClass().add("edit-button");
        editBtn.setOnAction(e -> handleEditTransaction(t));

        Button deleteBtn = new Button("🗑");
        deleteBtn.getStyleClass().add("delete-button");
        deleteBtn.setOnAction(e -> handleDeleteTransaction(t));

        row.getChildren().addAll(arrow, info, value, editBtn, deleteBtn);
        return row;
    }

    private void handleDeleteTransaction(Transaction t) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar exclusão");
        alert.setHeaderText(null);
        alert.setContentText("Deseja realmente excluir \"" + t.getDescription() + "\"?");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    if (t instanceof Expense) {
                        expenseDAO.delete(t.getId());
                    } else {
                        incomeDAO.delete(t.getId());
                    }
                    loadDashboard();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void handleEditTransaction(Transaction t) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/operation.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Editar Operação");
            stage.setScene(new Scene(loader.load()));
            OperationController controller = loader.getController();
            controller.setUser(currentUser);
            controller.setTransaction(t);
            stage.showAndWait();
            loadDashboard();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void handleNovaOperacao() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/operation.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Nova Operação");
        stage.setScene(new Scene(loader.load()));
        OperationController controller = loader.getController();
        controller.setUser(currentUser);
        stage.showAndWait();
        loadDashboard();
    }

    @FXML private void handleDashboard() {}

    @FXML
    private void handlePerfil() {
        NavigationUtil.navigateToProfile(welcomeLabel, currentUser);
    }

    @FXML
    private void handleContas() {
        NavigationUtil.navigateToBankAccount(welcomeLabel, currentUser);
    }

    @FXML
    private void handleLogout() {
        NavigationUtil.navigateToLogin(welcomeLabel);
    }
}
