package Class;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

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

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }
}
