package Class;

enum PaymentType {
    PIX,
    DÉBITO,
    CRÉDITO
}

public class Expense extends Transaction {
    public int installmentsTotal;
    public int installmentsPaid;
    public String paymentResponsible;
    public PaymentType paymentType;

    public Expense() {
    }

    public Expense(int user_id, double transactionValue, String description, String classification, boolean isRecurring, int installmentsTotal, int installmentsPaid, String paymentResponsible, PaymentType paymentType) {
        super(user_id, transactionValue, description, classification, isRecurring);
        if (isRecurring && installmentsTotal > 1) {
            throw new IllegalArgumentException("You can't create a recurring expense with more than 1 installment");
        }
        this.installmentsTotal = installmentsTotal;
        this.installmentsPaid = installmentsPaid;
        this.paymentResponsible = paymentResponsible;
        this.paymentType = paymentType;
    }

    //Metodos
    public void paymentOfInstallments() {
        this.installmentsPaid++;
        if (this.installmentsPaid == 1) {
            System.out.println(this.installmentsPaid + "st installment paid!");
        }else if(this.installmentsPaid == 2){
            System.out.println(this.installmentsPaid + "nd installment paid!");
        }else if(this.installmentsPaid == 3){
            System.out.println(this.installmentsPaid + "rd installment paid!");
        }else{
        System.out.println(this.installmentsPaid + "th installment paid!");
        }
    }

    @Override
    public Transaction duplicate() {
        return new Expense(this.getUser_id(), this.getTransactionValue(), this.getDescription(),
                this.getClassification(), this.getIsRecurring(), 1, 0, this.paymentResponsible, this.paymentType);
    }

    @Override
    public String toString() {
        return "Expense{" +
                super.toString() +
                "installmentsPaid=" + installmentsPaid +
                ", paymentResponsible='" + paymentResponsible + '\'' +
                ", paymentType=" + paymentType +
                ", installmentsTotal=" + installmentsTotal +
                '}';
    }
}
