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
        this.installmentsTotal = installmentsTotal;
        this.installmentsPaid = installmentsPaid;
        this.paymentResponsible = paymentResponsible;
        this.paymentType = paymentType;
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
