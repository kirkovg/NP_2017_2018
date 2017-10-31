package mk.ukim.finki.np.lab3.task2;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

class InvalidNameException extends Exception {
    public String name;

    public InvalidNameException(String name) {
        this.name = name;
    }
}

class MaximumSizeExceddedException extends Exception {
    public MaximumSizeExceddedException() {
        super();
    }
}

class InvalidNumberException extends Exception {
    public InvalidNumberException() {
        super();
    }
}

class Contact implements Comparable<Contact> {
    private String name;
    private String[] phones;

    public Contact(String name, String... phones) throws InvalidNameException,
            MaximumSizeExceddedException, InvalidNumberException {
        parseName(name);
        this.name = name;

        if (phones.length > 5) throw new MaximumSizeExceddedException();

        for (String p : phones) {
            this.parseNumber(p);
        }

        this.phones = phones;
    }

    private void parseName(String name) throws InvalidNameException {
        String regex = "[a-zA-Z\\d]{4,10}";
        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(name);
        if (!matcher.matches()) throw new InvalidNameException(name);
    }

    private void parseNumber(String number) throws InvalidNumberException {
        String regex = "^07[0125678]\\d{6}";
        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(number);
        if (!matcher.matches()) throw new InvalidNumberException();
    }

    public String getName() {
        return this.name;
    }

    public String[] getNumbers() {
        String[] phonesCopy = Arrays.copyOf(this.phones, this.phones.length);
        Arrays.sort(phonesCopy);
        return phonesCopy;
    }

    public void addNumber(String number) throws InvalidNumberException {
        parseNumber(number);

        this.phones = Arrays.copyOf(this.phones, this.phones.length + 1);
        this.phones[this.phones.length - 1] = number;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s\n%d\n", this.name, this.phones.length));
        Arrays.stream(this.getNumbers())
                .forEach(p -> sb.append(String.format("%s\n", p)));

        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Contact contact = (Contact) o;

        if (name != null ? !name.equals(contact.name) : contact.name != null) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(phones, contact.phones);
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + Arrays.hashCode(phones);
        return result;
    }

    public boolean samePrefix(String prefix) {
        for (String phone : this.phones) {
            if (phone.substring(0, prefix.length()).compareTo(prefix) == 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int compareTo(Contact o) {
        return Integer.compare(this.name.compareTo(o.name), 0);
    }
}

class PhoneBook {
    private Contact[] contacts;
    private int totalContacts;

    public PhoneBook() {
        this.contacts = new Contact[0];
        this.totalContacts = 0;
    }

    public void addContact(Contact contact) throws MaximumSizeExceddedException, InvalidNameException, InvalidNumberException {
        if (contacts.length > 250) throw new MaximumSizeExceddedException();

        if (findContact(contact.getName()) != -1) {
            throw new InvalidNameException(contact.getName());
        }

        this.contacts = Arrays.copyOf(this.contacts, this.contacts.length + 1);
        this.contacts[this.contacts.length - 1] = contact;
        this.totalContacts++;
    }

    public Contact getContactForName(String name) throws InvalidNumberException, InvalidNameException, MaximumSizeExceddedException {
        int idx = findContact(name);
        if (idx != -1) {
            return this.contacts[idx];
        }
        return null;
    }

    public int findContact(String name) {
        return IntStream
                .range(0, this.contacts.length)
                .filter(i -> this.contacts[i].getName().equals(name))
                .findFirst()
                .orElse(-1);
    }

    public int numberOfContacts() {
        return this.totalContacts;
    }

    public Contact[] getContacts() {
        Contact[] contactsCopy = Arrays.copyOf(this.contacts, this.totalContacts);
        Arrays.sort(contactsCopy);
        return contactsCopy;
    }


    public boolean removeContact(String name) {
        OptionalInt indexToDelete = IntStream.range(0, this.totalContacts)
                .filter(i -> this.contacts[i].getName().equals(name))
                .findFirst();

        if (indexToDelete.isPresent()) {
            this.contacts[indexToDelete.getAsInt()] = this.contacts[this.totalContacts - 1];
            this.totalContacts--;

            this.contacts = Arrays.copyOf(this.contacts, this.totalContacts);
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Arrays.stream(this.getContacts())
                .forEach(c -> sb.append(c.toString()).append("\n"));

        return sb.toString();
    }

    public static boolean saveAsTextFile(PhoneBook phoneBook, String path) throws IOException {
        try {
            FileOutputStream fout = new FileOutputStream(path);
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(phoneBook);
            oos.close();
            fout.close();

        } catch (Exception ex) {
            return false;
        }

        return true;
    }

    public static PhoneBook loadFromTextFile(String path) {
        PhoneBook phonebook;

        try {
            FileInputStream fin = new FileInputStream(path);
            ObjectInputStream ois = new ObjectInputStream(fin);
            phonebook = (PhoneBook) ois.readObject();
            ois.close();
            fin.close();
            return phonebook;

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public Contact[] getContactsForNumber(String numberPrefix) {
        Contact[] contactsArr = new Contact[this.totalContacts];

        int k = 0;
        for (int i = 0; i < this.contacts.length; i++) {
            if (this.contacts[i].samePrefix(numberPrefix)) {
                contactsArr[k] = this.contacts[i];
                k++;
            }
        }

        contactsArr = Arrays.copyOf(contactsArr, contactsArr.length - countNullElems(contactsArr));
        Arrays.sort(contactsArr);

        return contactsArr;
    }

    private int countNullElems(Contact[] arr) {
        return (int) Arrays.stream(arr)
                .filter(Objects::isNull)
                .count();
    }
}

public class PhonebookTester {

    public static void main(String[] args) throws Exception {
        Scanner jin = new Scanner(System.in);
        String line = jin.nextLine();
        switch (line) {
            case "test_contact":
                testContact(jin);
                break;
            case "test_phonebook_exceptions":
                testPhonebookExceptions(jin);
                break;
            case "test_usage":
                testUsage(jin);
                break;
        }
    }

    private static void testFile(Scanner jin) throws Exception {
        PhoneBook phonebook = new PhoneBook();
        while (jin.hasNextLine())
            phonebook.addContact(new Contact(jin.nextLine(), jin.nextLine().split("\\s++")));
        String text_file = "phonebook.txt";
        PhoneBook.saveAsTextFile(phonebook, text_file);
        PhoneBook pb = PhoneBook.loadFromTextFile(text_file);
        if (!pb.equals(phonebook)) System.out.println("Your file saving and loading doesn't seem to work right");
        else System.out.println("Your file saving and loading works great. Good job!");
    }

    private static void testUsage(Scanner jin) throws Exception {
        PhoneBook phonebook = new PhoneBook();
        while (jin.hasNextLine()) {
            String command = jin.nextLine();
            switch (command) {
                case "add":
                    phonebook.addContact(new Contact(jin.nextLine(), jin.nextLine().split("\\s++")));
                    break;
                case "remove":
                    phonebook.removeContact(jin.nextLine());
                    break;
                case "print":
                    System.out.println(phonebook.numberOfContacts());
                    System.out.println(Arrays.toString(phonebook.getContacts()));
                    System.out.println(phonebook.toString());
                    break;
                case "get_name":
                    System.out.println(phonebook.getContactForName(jin.nextLine()));
                    break;
                case "get_number":
                    System.out.println(Arrays.toString(phonebook.getContactsForNumber(jin.nextLine())));
                    break;
            }
        }
    }

    private static void testPhonebookExceptions(Scanner jin) {
        PhoneBook phonebook = new PhoneBook();
        boolean exception_thrown = false;
        try {
            while (jin.hasNextLine()) {
                phonebook.addContact(new Contact(jin.nextLine()));
            }
        } catch (InvalidNameException e) {
            System.out.println(e.name);
            exception_thrown = true;
        } catch (Exception e) {
        }
        if (!exception_thrown) System.out.println("Your addContact method doesn't throw InvalidNameException");
        /*
        exception_thrown = false;
		try {
		phonebook.addContact(new Contact(jin.nextLine()));
		} catch ( MaximumSizeExceddedException e ) {
			exception_thrown = true;
		}
		catch ( Exception e ) {}
		if ( ! exception_thrown ) System.out.println("Your addContact method doesn't throw MaximumSizeExcededException");
        */
    }

    private static void testContact(Scanner jin) throws Exception {
        boolean exception_thrown = true;
        String names_to_test[] = {"And\nrej", "asd", "AAAAAAAAAAAAAAAAAAAAAA", "Ð�Ð½Ð´Ñ€ÐµÑ˜A123213", "Andrej#", "Andrej<3"};
        for (String name : names_to_test) {
            try {
                new Contact(name);
                exception_thrown = false;
            } catch (InvalidNameException e) {
                exception_thrown = true;
            }
            if (!exception_thrown) System.out.println("Your Contact constructor doesn't throw an InvalidNameException");
        }
        String numbers_to_test[] = {"+071718028", "number", "078asdasdasd", "070asdqwe", "070a56798", "07045678a", "123456789", "074456798", "073456798", "079456798"};
        for (String number : numbers_to_test) {
            try {
                new Contact("Andrej", number);
                exception_thrown = false;
            } catch (InvalidNumberException e) {
                exception_thrown = true;
            }
            if (!exception_thrown)
                System.out.println("Your Contact constructor doesn't throw an InvalidNumberException");
        }
        String nums[] = new String[10];
        for (int i = 0; i < nums.length; ++i) nums[i] = getRandomLegitNumber();
        try {
            new Contact("Andrej", nums);
            exception_thrown = false;
        } catch (MaximumSizeExceddedException e) {
            exception_thrown = true;
        }
        if (!exception_thrown)
            System.out.println("Your Contact constructor doesn't throw a MaximumSizeExceddedException");
        Random rnd = new Random(5);
        Contact contact = new Contact("Andrej", getRandomLegitNumber(rnd), getRandomLegitNumber(rnd), getRandomLegitNumber(rnd));
        System.out.println(contact.getName());
        System.out.println(Arrays.toString(contact.getNumbers()));
        System.out.println(contact.toString());
        contact.addNumber(getRandomLegitNumber(rnd));
        System.out.println(Arrays.toString(contact.getNumbers()));
        System.out.println(contact.toString());
        contact.addNumber(getRandomLegitNumber(rnd));
        System.out.println(Arrays.toString(contact.getNumbers()));
        System.out.println(contact.toString());
    }

    static String[] legit_prefixes = {"070", "071", "072", "075", "076", "077", "078"};
    static Random rnd = new Random();

    private static String getRandomLegitNumber() {
        return getRandomLegitNumber(rnd);
    }

    private static String getRandomLegitNumber(Random rnd) {
        StringBuilder sb = new StringBuilder(legit_prefixes[rnd.nextInt(legit_prefixes.length)]);
        for (int i = 3; i < 9; ++i)
            sb.append(rnd.nextInt(10));
        return sb.toString();
    }


}

