package Class;

public class Income extends Transaction {

    public Income() {
    }

    public Income(int user_id, double transactionValue, String description, String classification, boolean isRecurring) {
        super(user_id, transactionValue, description, classification, isRecurring, TransactionType.INCOME);
    }

    @Override
    public Transaction duplicate() {
        return new Income(this.getUser_id(), this.getTransactionValue(), this.getDescription(),
                this.getClassification(), this.getIsRecurring());
    }

    @Override
    public String toString() {
        return "Income{" +
                "id=" + super.getId() +
                ", user_id=" + super.getUser_id() +
                ", dateTimeTransaction=" + super.getDateTimeTransaction() +
                ", transactionValue=" + super.getTransactionValue() +
                ", description='" + super.getDescription() + '\'' +
                ", classification='" + super.getClassification() + '\'' +
                ", isRecurring=" + super.getIsRecurring() +
                '}';
    }
}
