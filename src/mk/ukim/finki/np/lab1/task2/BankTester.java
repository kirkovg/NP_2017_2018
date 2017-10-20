package mk.ukim.finki.np.lab1.task2;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

class DollarSignUtil {
    public static String clearDollarSign(String amount) {
        return String.valueOf(amount.toCharArray(), 0, amount.length() - 1);
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}

class Account {
    private String name;
    private long id;
    private String balance;

    public Account(String name, String balance) {
        this.name = name;
        this.id = new Random().nextLong();
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return String.format("Name: %s\nBalance: %.2f$\n",
                this.name,
                parseDouble(DollarSignUtil.clearDollarSign(this.balance)));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        if (id != account.id) return false;
        if (name != null ? !name.equals(account.name) : account.name != null) return false;
        return balance != null ? balance.equals(account.balance) : account.balance == null;

    }
}

abstract class Transaction {
    protected long fromId;
    protected long toId;
    protected String description;
    protected String amount;

    public Transaction(long fromId, long toId, String description, String amount) {
        this.fromId = fromId;
        this.toId = toId;
        this.description = description;
        this.amount = amount;
    }

    public long getFromId() {
        return fromId;
    }

    public long getToId() {
        return toId;
    }

    public String getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    abstract public double getTransactionSpecificProperty(Transaction transaction);

    abstract public void calculate(Transaction transaction,
                                   Account account,
                                   double parsedFromAccountBalance,
                                   double parsedTransactionAmount
    );

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Transaction that = (Transaction) o;

        if (fromId != that.fromId) return false;
        if (toId != that.toId) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        return amount != null ? amount.equals(that.amount) : that.amount == null;

    }
}

class FlatAmountProvisionTransaction extends Transaction {
    private String flatProvision;


    public FlatAmountProvisionTransaction(long fromId, long toId, String amount, String flatProvision) {
        super(fromId, toId, "FlatAmount", amount);
        this.flatProvision = flatProvision;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FlatAmountProvisionTransaction that = (FlatAmountProvisionTransaction) o;

        return flatProvision != null ? flatProvision.equals(that.flatProvision) : that.flatProvision == null;
    }

    @Override
    public double getTransactionSpecificProperty(Transaction transaction) {
        return parseDouble(DollarSignUtil.clearDollarSign(flatProvision));
    }

    @Override
    public void calculate(Transaction transaction,
                          Account account,
                          double parsedFromAccountBalance,
                          double parsedTransactionAmount
    ) {
        if (transaction.getToId() != transaction.getFromId()) {
            account.setBalance(
                    Double.toString(
                            parsedFromAccountBalance - parsedTransactionAmount -
                                    parseDouble(DollarSignUtil.clearDollarSign(flatProvision))
                    ) + "$");
        } else {
            account.setBalance(
                    Double.toString(
                            parsedFromAccountBalance  -
                                    parseDouble(DollarSignUtil.clearDollarSign(flatProvision))
                    ) + "$");
        }
    }
}

class FlatPercentProvisionTransaction extends Transaction {
    private int centsPerDollar;

    public FlatPercentProvisionTransaction(long fromId, long toId, String amount, int centsPerDollar) {
        super(fromId, toId, "FlatPercent", amount);
        this.centsPerDollar = centsPerDollar;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FlatPercentProvisionTransaction that = (FlatPercentProvisionTransaction) o;

        return centsPerDollar == that.centsPerDollar;
    }

    @Override
    public double getTransactionSpecificProperty(Transaction transaction) {
        double parsedTransactionAmount = parseDouble(DollarSignUtil.clearDollarSign(transaction.getAmount()));
        double totalCentsToAdd = 0;
        for (int i = 0; i < Math.floor(parsedTransactionAmount); i++) {
            totalCentsToAdd += centsPerDollar;
        }
        return totalCentsToAdd / 100;
    }

    @Override
    public void calculate(Transaction transaction,
                          Account account,
                          double parsedFromAccountBalance,
                          double parsedTransactionAmount
    ) {
        double totalCentsToAdd = 0;
        for (int i = 0; i < Math.floor(parsedTransactionAmount); i++) {
            totalCentsToAdd += centsPerDollar;
        }
        if (transaction.getFromId() != transaction.getToId()) {
            account.setBalance(
                    Double.toString(
                            parsedFromAccountBalance - parsedTransactionAmount - (totalCentsToAdd / 100)
                    ) + "$"
            );
        } else {
            account.setBalance(
                    Double.toString(
                            parsedFromAccountBalance - (totalCentsToAdd / 100)
                    ) + "$"
            );
        }
    }
}

class Bank {
    private String name;
    private Account[] accounts;
    private double totalProvisions;
    private double totalTransfers;


    public Bank(String name, Account[] accounts) {
        this.name = name;
        this.accounts = Arrays.copyOf(accounts, accounts.length);
    }

    public boolean makeTransaction(Transaction transaction) {
        Optional<Account> fromAccount = Arrays
                .stream(accounts)
                .filter(account -> account.getId() == transaction.getFromId())
                .findFirst();

        Optional<Account> toAccount = Arrays
                .stream(accounts)
                .filter(account -> account.getId() == transaction.getToId())
                .findFirst();

        if (!fromAccount.isPresent() || !toAccount.isPresent()) {
            return false;
        }

        double parsedTransactionAmount = parseDouble(DollarSignUtil.clearDollarSign(transaction.getAmount()));
        double parsedFromAccountBalance = parseDouble(DollarSignUtil.clearDollarSign(fromAccount.get().getBalance()));
        double parsedToAccountBalance = parseDouble(DollarSignUtil.clearDollarSign(toAccount.get().getBalance()));

        if (parsedTransactionAmount > parsedFromAccountBalance) {
            return false;
        }

        toAccount.get().setBalance(Double.toString(parsedToAccountBalance + parsedTransactionAmount) + "$");

        this.totalTransfers += parsedTransactionAmount;
        this.totalProvisions += transaction.getTransactionSpecificProperty(transaction);
        transaction.calculate(transaction, fromAccount.get(), parsedFromAccountBalance, parsedTransactionAmount);

        return true;
    }

    public String totalTransfers() {
        return String.format("%.2f$", totalTransfers);
    }

    public String totalProvision() {
        return String.format("%.2f$", totalProvisions);
    }

    public Account[] getAccounts() {
        return accounts;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Name: Banka na RM\n\n");
        for (Account acc : accounts) {
            sb.append(acc.toString());
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Bank bank = (Bank) o;

        if (Double.compare(bank.totalProvisions, totalProvisions) != 0) return false;
        if (Double.compare(bank.totalTransfers, totalTransfers) != 0) return false;
        if (name != null ? !name.equals(bank.name) : bank.name != null) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(accounts, bank.accounts);

    }
}


public class BankTester {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        String test_type = jin.nextLine();
        switch (test_type) {
            case "typical_usage":
                testTypicalUsage(jin);
                break;
            case "equals":
                testEquals();
                break;
        }
        jin.close();
    }

    private static void testEquals() {
        Account a1 = new Account("Andrej", "20.00$");
        Account a2 = new Account("Andrej", "20.00$");
        Account a3 = new Account("Andrej", "30.00$");
        Account a4 = new Account("Gajduk", "20.00$");
        List<Account> all = Arrays.asList(a1, a2, a3, a4);
        if (!(a1.equals(a1)&&!a1.equals(a2)&&!a2.equals(a1) && !a3.equals(a1)
                && !a4.equals(a1)
                && !a1.equals(null))) {
            System.out.println("Your account equals method does not work properly.");
            return;
        }
        Set<Long> ids = all.stream().map(Account::getId).collect(Collectors.toSet());
        if (ids.size() != all.size()) {
            System.out.println("Different accounts have the same IDS. This is not allowed");
            return;
        }
        FlatAmountProvisionTransaction fa1 = new FlatAmountProvisionTransaction(10, 20, "20.00$", "10.00$");
        FlatAmountProvisionTransaction fa2 = new FlatAmountProvisionTransaction(20, 20, "20.00$", "10.00$");
        FlatAmountProvisionTransaction fa3 = new FlatAmountProvisionTransaction(20, 10, "20.00$", "10.00$");
        FlatAmountProvisionTransaction fa4 = new FlatAmountProvisionTransaction(10, 20, "50.00$", "50.00$");
        FlatAmountProvisionTransaction fa5 = new FlatAmountProvisionTransaction(30, 40, "20.00$", "10.00$");
        FlatPercentProvisionTransaction fp1 = new FlatPercentProvisionTransaction(10, 20, "20.00$", 10);
        FlatPercentProvisionTransaction fp2 = new FlatPercentProvisionTransaction(10, 20, "20.00$", 10);
        FlatPercentProvisionTransaction fp3 = new FlatPercentProvisionTransaction(10, 10, "20.00$", 10);
        FlatPercentProvisionTransaction fp4 = new FlatPercentProvisionTransaction(10, 20, "50.00$", 10);
        FlatPercentProvisionTransaction fp5 = new FlatPercentProvisionTransaction(10, 20, "20.00$", 30);
        FlatPercentProvisionTransaction fp6 = new FlatPercentProvisionTransaction(30, 40, "20.00$", 10);
        if (fa1.equals(fa1) &&
                !fa2.equals(null) &&
                fa2.equals(fa1) &&
                fa1.equals(fa2) &&
                fa1.equals(fa3) &&
                !fa1.equals(fa4) &&
                !fa1.equals(fa5) &&
                !fa1.equals(fp1) &&
                fp1.equals(fp1) &&
                !fp2.equals(null) &&
                fp2.equals(fp1) &&
                fp1.equals(fp2) &&
                fp1.equals(fp3) &&
                !fp1.equals(fp4) &&
                !fp1.equals(fp5) &&
                !fp1.equals(fp6)) {
            System.out.println("Your transactions equals methods do not work properly.");
            return;
        }
        Account accounts[] = new Account[]{a1, a2, a3, a4};
        Account accounts1[] = new Account[]{a2, a1, a3, a4};
        Account accounts2[] = new Account[]{a1, a2, a3};
        Account accounts3[] = new Account[]{a1, a2, a3, a4};

        Bank b1 = new Bank("Test", accounts);
        Bank b2 = new Bank("Test", accounts1);
        Bank b3 = new Bank("Test", accounts2);
        Bank b4 = new Bank("Sample", accounts);
        Bank b5 = new Bank("Test", accounts3);

        if (!(b1.equals(b1) &&
                !b1.equals(null) &&
                !b1.equals(b2) &&
                !b2.equals(b1) &&
                !b1.equals(b3) &&
                !b3.equals(b1) &&
                !b1.equals(b4) &&
                b1.equals(b5))) {
            System.out.println("Your bank equals method do not work properly.");
            return;
        }
        accounts[2] = a1;
        if (!b1.equals(b5)) {
            System.out.println("Your bank equals method do not work properly.");
            return;
        }
        long from_id = a2.getId();
        long to_id = a3.getId();
        Transaction t = new FlatAmountProvisionTransaction(from_id, to_id, "3.00$", "3.00$");
        b1.makeTransaction(t);
        if (b1.equals(b5)) {
            System.out.println("Your bank equals method do not work properly.");
            return;
        }
        b5.makeTransaction(t);
        if (!b1.equals(b5)) {
            System.out.println("Your bank equals method do not work properly.");
            return;
        }
        System.out.println("All your equals methods work properly.");
    }

    private static void testTypicalUsage(Scanner jin) {
        String bank_name = jin.nextLine();
        int num_accounts = jin.nextInt();
        jin.nextLine();
        Account accounts[] = new Account[num_accounts];
        for (int i = 0; i < num_accounts; ++i)
            accounts[i] = new Account(jin.nextLine(), jin.nextLine());
        Bank bank = new Bank(bank_name, accounts);
        while (true) {
            String line = jin.nextLine();
            switch (line) {
                case "stop":
                    return;
                case "transaction":
                    String descrption = jin.nextLine();
                    String amount = jin.nextLine();
                    String parameter = jin.nextLine();
                    int from_idx = jin.nextInt();
                    int to_idx = jin.nextInt();
                    jin.nextLine();
                    Transaction t = getTransaction(descrption, from_idx, to_idx, amount, parameter, bank);
                    System.out.println("Transaction amount: " + t.getAmount());
                    System.out.println("Transaction description: " + t.getDescription());
                    System.out.println("Transaction successful? " + bank.makeTransaction(t));
                    break;
                case "print":
                    System.out.println(bank.toString());
                    System.out.println("Total provisions: " + bank.totalProvision());
                    System.out.println("Total transfers: " + bank.totalTransfers());
                    System.out.println();
                    break;
            }
        }
    }

    private static Transaction getTransaction(String description, int from_idx, int to_idx, String amount, String o, Bank bank) {
        switch (description) {
            case "FlatAmount":
                return new FlatAmountProvisionTransaction(bank.getAccounts()[from_idx].getId(),
                        bank.getAccounts()[to_idx].getId(), amount, o);
            case "FlatPercent":
                return new FlatPercentProvisionTransaction(bank.getAccounts()[from_idx].getId(),
                        bank.getAccounts()[to_idx].getId(), amount, parseInt(o));
        }
        return null;
    }


}



