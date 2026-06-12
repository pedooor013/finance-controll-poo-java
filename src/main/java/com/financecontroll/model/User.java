package com.financecontroll.model;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import static com.financecontroll.util.PasswordIUtils.hashPassword;

public class User {
    private int id;
    private String username;
    private String userEmail;
    private String password;
    private ArrayList<BankAccount> userBankAccount;

    public User() {
    }

    public User(String username, String userEmail, String password) {
        this.username = username;
        this.userEmail = userEmail;
        this.password = hashPassword(password);
        this.userBankAccount = new ArrayList<BankAccount>();
    }

    public User(int id, String username, String userEmail) {
        this.id = id;
        this.username = username;
        this.userEmail = userEmail;
        this.userBankAccount = new ArrayList<BankAccount>();
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = hashPassword(password);
    }

    public ArrayList<BankAccount> getUserBankAccount() {
        return userBankAccount;
    }

    public void setUserBankAccount(ArrayList<BankAccount> userBankAccount) {
        this.userBankAccount = userBankAccount;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", userEmail='" + userEmail + '\'' +
                '}';
    }

}
