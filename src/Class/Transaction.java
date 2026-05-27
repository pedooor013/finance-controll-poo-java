package Class;

import java.time.LocalDate;
import java.time.LocalDateTime;

enum TransactionType {
    EXPENSE,
    INCOME
}

public abstract class Transaction {
    static int idTransaction = 1;
    private int id;
    private int user_id;
    private LocalDate dateTimeTransaction;
    private double transactionValue;
    private String description;
    private String classification;
    private boolean isRecurring;
    private TransactionType transactionType;

    public Transaction(){}

    public Transaction(int user_id, double transactionValue, String description, String classification, boolean isRecurring, TransactionType transactionType) {
        this.id = idTransaction++;
        this.user_id = user_id;
        this.dateTimeTransaction = LocalDate.from(LocalDateTime.now());
        this.transactionValue = transactionValue;
        this.description = description;
        this.classification = classification;
        this.isRecurring = isRecurring;
        this.transactionType = transactionType;
    }

    public int getId(){
        return id;
    }
    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
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

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
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

    public void  setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }
    public abstract Transaction duplicate();

    @Override
    public String toString() {
        return "id=" + id +
                ", user_id=" + user_id +
                ", dateTimeTransaction=" + dateTimeTransaction +
                ", transactionValue=" + transactionValue +
                ", description='" + description + '\'' +
                ", classification='" + classification + '\'' +
                ", isRecurring=" + isRecurring +
                ", transactionType=" + this.transactionType;
    }
}
