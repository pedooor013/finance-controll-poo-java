package com.financecontroll.model;

public class Income extends Transaction {

    public Income() {
    }

    public Income(int bankAccountId, double transactionValue, String description, boolean isRecurring) {
        super(bankAccountId, transactionValue, description, isRecurring, TransactionType.INCOME);
    }

    @Override
    public Transaction duplicate() {
        return new Income(this.getBankAccountId(), this.getTransactionValue(), this.getDescription(), this.getIsRecurring());
    }

    @Override
    public String toString() {
        return "Income{" +
                "id=" + super.getId() +
                ", =" + super.getBankAccountId() +
                ", dateTimeTransaction=" + super.getDateTimeTransaction() +
                ", transactionValue=" + super.getTransactionValue() +
                ", description='" + super.getDescription() + '\'' +
                ", isRecurring=" + super.getIsRecurring() +
                '}';
    }
}
