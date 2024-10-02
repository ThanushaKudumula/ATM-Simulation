import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Account implements Serializable {
    private String accountNumber;
    private String pin;
    private double balance;
    private List<Transaction> transactionHistory;

    public Account(String accountNumber, String pin, double balance) {
        this.accountNumber = accountNumber;
        this.pin = pin;
        this.balance = balance;
        this.transactionHistory = new ArrayList<>();
    }

    public boolean authenticate(String inputPin) {
        return this.pin.equals(inputPin);
    }

    public void withdraw(double amount) {
        if (amount <= balance) {
            balance -= amount;
            addTransaction("Withdraw", amount);
            System.out.println("Withdrawal successful! New balance: " + balance);
        } else {
            System.out.println("Insufficient funds!");
        }
    }

    public void deposit(double amount) {
        balance += amount;
        addTransaction("Deposit", amount);
        System.out.println("Deposit successful! New balance: " + balance);
    }

    public double getBalance() {
        return balance;
    }

    public void addTransaction(String type, double amount) {
        transactionHistory.add(new Transaction(type, amount));
    }

    public void printTransactionHistory() {
        System.out.println("Transaction History:");
        for (Transaction t : transactionHistory) {
            System.out.println(t);
        }
    }

    public String getAccountNumber() {
        return accountNumber;
    }
}
