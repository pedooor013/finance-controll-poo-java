package com.financecontroll.model;

enum PaymentType {
    PIX,
    DEBITO,
    CREDITO
}

public class Expense extends Transaction {
    private int installmentsTotal;
    private int installmentsPaid;
    private String paymentResponsible;
    private PaymentType paymentType;
    private Category category;
    public Expense() {
    }

    public Expense(int bankAccountId, double transactionValue, String description, boolean isRecurring, int installmentsTotal, int installmentsPaid, String paymentResponsible, PaymentType paymentType, Category category) {
        super(bankAccountId, transactionValue, description, isRecurring, TransactionType.EXPENSE);
        if (isRecurring && installmentsTotal > 1) {
            throw new IllegalArgumentException("You can't create a recurring expense with more than 1 installment");
        }
        this.installmentsTotal = installmentsTotal;
        this.installmentsPaid = installmentsPaid;
        this.paymentResponsible = paymentResponsible;
        this.paymentType = paymentType;
        this.category = category;
    }

    public int getInstallmentsTotal() {
        return installmentsTotal;
    }

    public void setInstallmentsTotal(int installmentsTotal) {
        this.installmentsTotal = installmentsTotal;
    }

    public int getInstallmentsPaid() {
        return installmentsPaid;
    }

    public void setInstallmentsPaid(int installmentsPaid) {
        this.installmentsPaid = installmentsPaid;
    }

    public String getPaymentResponsible() {
        return paymentResponsible;
    }

    public void setPaymentResponsible(String paymentResponsible) {
        this.paymentResponsible = paymentResponsible;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void paymentOfInstallments() {
        this.installmentsPaid++;
        if (this.installmentsPaid == 1) {
            System.out.println(this.installmentsPaid + "st installment paid!");
        } else if (this.installmentsPaid == 2) {
            System.out.println(this.installmentsPaid + "nd installment paid!");
        } else if (this.installmentsPaid == 3) {
            System.out.println(this.installmentsPaid + "rd installment paid!");
        } else {
            System.out.println(this.installmentsPaid + "th installment paid!");
        }
    }

    private Transaction createNewInstallmentExpense() {
        if(this.installmentsPaid == this.installmentsTotal) {
        }
    }

    @Override
    public Transaction duplicate() {
        return new Expense(this.getBankAccountId(), this.getTransactionValue(), this.getDescription(),
                this.getIsRecurring(), 1, 0, this.paymentResponsible, this.paymentType, this.category);
    }

    @Override
    public String toString() {
        return "Expense{" +
                super.toString() +
                "installmentsPaid=" + installmentsPaid +
                ", paymentResponsible='" + paymentResponsible + '\'' +
                ", paymentType=" + paymentType +
                ", installmentsTotal=" + installmentsTotal +
                ", category=" + category +
                '}';
    }
}
