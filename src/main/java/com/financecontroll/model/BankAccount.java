package com.financecontroll.model;

public class BankAccount {
    private int id;
    private String bankName;
    private int userId;

    public BankAccount() {
    }

    public BankAccount(String bankName, int userId) {
        this.bankName = bankName.toUpperCase();
        this.userId = userId;
    }

    public int getId(){
        return this.id;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName.toUpperCase();
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "BankAccount{" +
                "id=" + id +
                ", bankName='" + bankName + '\'' +
                ", userId=" + userId +
                '}';
    }
}
