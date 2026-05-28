package Class;

public class BankAccount {
    private int id;
    private String bankName;

    public BankAccount() {
    }

    public BankAccount(String bankName) {
        this.bankName = bankName.toUpperCase();
    }

    public int getId(){
        return this.id;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    @Override
    public String toString() {
        return "BankAccount{" +
                "bankName='" + bankName + '\'' +
                '}';
    }
}
