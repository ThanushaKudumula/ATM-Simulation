import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ATM {
    private Map<String, Account> accounts;
    private Account currentAccount;

    public ATM() {
        accounts = new HashMap<>();
        loadAccounts();
    }

    @SuppressWarnings("unchecked")
    public void loadAccounts() {
        File file = new File("data/accounts.ser");
        if (!file.exists()) {
            System.out.println("No existing account data found. Starting fresh.");
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            accounts = (Map<String, Account>) ois.readObject();
        } catch (EOFException e) {
            // Handle the case where the file is empty
            System.out.println("File is empty. Starting with no accounts.");
            accounts = new HashMap<>();
        } catch (FileNotFoundException e) {
            System.out.println("No existing account data found. Starting fresh.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void saveAccounts() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("data/accounts.ser"))) {
            oos.writeObject(accounts);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        boolean isRunning = true;

        while (isRunning) {
            System.out.println("\nATM Menu:");
            System.out.println("1. Log In");
            System.out.println("2. Create Account");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline character

            switch (choice) {
                case 1:
                    logIn();
                    break;
                case 2:
                    createAccount(scanner);
                    break;
                case 3:
                    isRunning = false;
                    saveAccounts();
                    System.out.println("Thank you for using the ATM.");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    // Create a new account
    public void createAccount(Scanner scanner) {
        System.out.print("Enter new account number: ");
        String accountNumber = scanner.nextLine();

        if (accounts.containsKey(accountNumber)) {
            System.out.println("Account with this number already exists.");
            return;
        }

        System.out.print("Enter new PIN: ");
        String pin = scanner.nextLine();

        System.out.print("Enter initial balance: ");
        double balance = scanner.nextDouble();
        scanner.nextLine(); // consume newline character

        // Create a new account and add it to the accounts map
        Account newAccount = new Account(accountNumber, pin, balance);
        accounts.put(accountNumber, newAccount);
        saveAccounts();
        System.out.println("Account created successfully!");
    }

    // Log in to existing account
    public void logIn() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter Account Number: ");
        String accountNumber = scanner.nextLine();

        if (!accounts.containsKey(accountNumber)) {
            System.out.println("Account does not exist.");
            return;
        }

        System.out.print("Enter PIN: ");
        String pin = scanner.nextLine();

        currentAccount = accounts.get(accountNumber);

        if (!currentAccount.authenticate(pin)) {
            System.out.println("Incorrect PIN!");
            return;
        }

        // User is authenticated
        boolean isRunning = true;

        while (isRunning) {
            System.out.println("\nATM Menu:");
            System.out.println("1. Check Balance");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transaction History");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.println("Your current balance is: " + currentAccount.getBalance());
                    break;
                case 2:
                    System.out.print("Enter deposit amount: ");
                    double depositAmount = scanner.nextDouble();
                    currentAccount.deposit(depositAmount);
                    break;
                case 3:
                    System.out.print("Enter withdrawal amount: ");
                    double withdrawalAmount = scanner.nextDouble();
                    currentAccount.withdraw(withdrawalAmount);
                    break;
                case 4:
                    currentAccount.printTransactionHistory();
                    break;
                case 5:
                    isRunning = false;
                    saveAccounts();
                    System.out.println("Thank you for using the ATM.");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    public static void main(String[] args) {
        ATM atm = new ATM();
        atm.start();
    }
}
