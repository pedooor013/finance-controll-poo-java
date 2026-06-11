package com.financecontroll.model;

import java.time.LocalDate;

enum TransactionType {
    EXPENSE,
    INCOME
}

public abstract class Transaction {
    private int id;
    private int bankAccountId;
    private LocalDate dateTimeTransaction;
    private double transactionValue;
    private String description;
    private boolean isRecurring;
    private TransactionType transactionType;

    public Transaction() {
    }

    public Transaction(int bankAccountId, double transactionValue, String description, boolean isRecurring, TransactionType transactionType) {
        this.bankAccountId = bankAccountId;
        this.dateTimeTransaction = LocalDate.now();
        this.transactionValue = transactionValue;
        this.description = description;
        this.isRecurring = isRecurring;
        this.transactionType = transactionType;
    }
    public Transaction(int id, int bankAccountId, double transactionValue, String description, boolean isRecurring, TransactionType transactionType) {
        this.id = id;
        this.bankAccountId = bankAccountId;
        this.dateTimeTransaction = LocalDate.now();
        this.transactionValue = transactionValue;
        this.description = description;
        this.isRecurring = isRecurring;
        this.transactionType = transactionType;
    }
    public Transaction(int id, int bankAccountId, LocalDate dateTimeTransaction, double transactionValue, String description, boolean isRecurring, TransactionType transactionType) {
        this.id = id;
        this.bankAccountId = bankAccountId;
        this.dateTimeTransaction = dateTimeTransaction;
        this.transactionValue = transactionValue;
        this.description = description;
        this.isRecurring = isRecurring;
        this.transactionType = transactionType;
    }

    public int getId() {
        return id;
    }

    public int getBankAccountId() {
        return bankAccountId;
    }

    public void setBankAccountId(int bankAccountId) {
        this.bankAccountId = bankAccountId;
    }

    public LocalDate getDateTimeTransaction() {
        return dateTimeTransaction;
    }

    public void setDateTimeTransaction(LocalDate dateTimeTransaction) {
        this.dateTimeTransaction = dateTimeTransaction;
    }

    public double getTransactionValue() {
        return transactionValue;
    }

    public void setTransactionValue(double transactionValue) {
        this.transactionValue = transactionValue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getIsRecurring() {
        return isRecurring;
    }

    public void setRecurring(boolean recurring) {
        isRecurring = recurring;
    }

    public TransactionType getTransactionType() {
        return this.transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public abstract Transaction duplicate();

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", bankAccountId=" + bankAccountId +
                ", dateTimeTransaction=" + dateTimeTransaction +
                ", transactionValue=" + transactionValue +
                ", description='" + description + '\'' +
                ", isRecurring=" + isRecurring +
                ", transactionType=" + transactionType +
                '}';
    }
}
