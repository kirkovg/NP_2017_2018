package mk.ukim.finki.np.lab2.task2;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;

enum Operator {
    ONE,
    VIP,
    TMOBILE
}


abstract class Contact {
    protected Date date;

    public Contact(String date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            this.date = df.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public boolean isNewerThan(Contact c) {
        return this.date.after(c.date);
    }

    public Date getDate() {
        return this.date;
    }

    public abstract String getType();

    public abstract String getPhone();

    public abstract String getEmail();
}

class EmailContact extends Contact {

    private String email;

    public EmailContact(String date, String email) {
        super(date);
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getType() {
        return "Email";
    }

    @Override
    public String getPhone() {
        return "fail";
    }
}

class PhoneContact extends Contact {
    private String phone;
    private Operator operator;

    public PhoneContact(String date, String phone) {
        super(date);
        this.phone = phone;
        this.operator = extractOperatorFromPhone(phone);
    }

    @Override
    public String getType() {
        return "Phone";
    }

    public String getPhone() {
        return phone;
    }

    @Override
    public String getEmail() {
        return "fail";
    }

    public Operator getOperator() {
        return operator;
    }

    private Operator extractOperatorFromPhone(String phone) {
        char digit = phone.charAt(2);
        if (digit == '0' || digit == '1' || digit == '2')
            return Operator.TMOBILE;
        else if (digit == '5' || digit == '6')
            return Operator.ONE;
        else if (digit == '7' || digit == '8')
            return Operator.VIP;
        else
            return null;
    }

    @Override
    public String toString() {
        return phone;
    }
}

class Student {
    private String firstName;
    private String lastName;
    private String city;
    private int age;
    private long index;
    private Contact[] contacts;
    private int numContacts = 0;
    private int numPhoneContacts = 0;
    private int numEmailContacts = 0;


    public Student(String firstName, String lastName, String city, int age, long index) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.age = age;
        this.index = index;
        this.contacts = new Contact[numContacts];
    }

    public int getNumContacts() {
        return this.numContacts;
    }

    public void addEmailContact(String date, String email) {
        this.contacts = Arrays.copyOf(this.contacts, this.contacts.length + 1);
        this.contacts[numContacts] = new EmailContact(date, email);
        numContacts++;
        numEmailContacts++;
    }

    public void addPhoneContact(String date, String phone) {
        this.contacts = Arrays.copyOf(this.contacts, this.contacts.length + 1);
        this.contacts[numContacts] = new PhoneContact(date, phone);

        numContacts++;
        numPhoneContacts++;
    }

    public Contact[] getEmailContacts() {
        return Arrays.stream(this.contacts)
                .filter(contact -> contact.getType().equals("Email"))
                .toArray(Contact[]::new);
    }

    public Contact[] getPhoneContacts() {
        return Arrays.stream(this.contacts)
                .filter(contact -> contact.getType().equals("Phone"))
                .toArray(Contact[]::new);
    }

    public String getCity() {
        return city;
    }

    public long getIndex() {
        return index;
    }

    public String getFullName() {
        return String.format("%s %s", firstName.toUpperCase(), lastName.toUpperCase());
    }

    public Contact getLatestContact() {
        int indexNewest = 0;
        for (int i = 1; i < this.numContacts; i++) {
            if (this.contacts[i].isNewerThan(this.contacts[indexNewest])) {
                indexNewest = i;
            }
        }
        return this.contacts[indexNewest];
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"ime\":" + "\"" + firstName + "\", " + "\"prezime\":" + "\"" + lastName + "\", "
                + "\"vozrast\":" + age + ", " + "\"grad\":" + "\"" + city + "\", " + "\"indeks\":" + index + ", ");

        sb.append("\"telefonskiKontakti\":[");
        Contact[] tc = getPhoneContacts();
        for (int i = 0; i < numPhoneContacts; i++) {
            if (i != numPhoneContacts - 1) {
                sb.append("\"" + tc[i].getPhone() + "\", ");
            } else {
                sb.append("\"" + tc[i].getPhone() + "\"], ");
            }
        }

        if (numPhoneContacts == 0) {
            sb.append("], ");
        }

        sb.append("\"emailKontakti\":[");

        Contact[] ec = getEmailContacts();
        for (int i = 0; i < numEmailContacts; i++) {
            if (i != numEmailContacts - 1) {
                sb.append("\"" + ec[i].getEmail() + "\", ");
            } else {
                sb.append("\"" + ec[i].getEmail() + "\"]}");
            }
        }
        if (numEmailContacts == 0) {
            sb.append("]}");
        }

        return sb.toString();
    }
}

class Faculty {
    private String name;
    private Student[] students;

    public Faculty(String name, Student[] students) {
        this.name = name;
        this.students = students;
    }

    public int countStudentsFromCity(String cityName) {
        return (int) Arrays.stream(students)
                .filter(student -> student.getCity().equals(cityName))
                .count();
    }

    public Student getStudent(long index) {
        return Arrays.stream(students)
                .filter(student -> student.getIndex() == index)
                .findFirst().get();
    }

    public double getAverageNumberOfContacts() {
        int sumContacts =
                Arrays.stream(students)
                        .mapToInt(Student::getNumContacts)
                        .sum();

        return sumContacts * 1.00 / students.length;
    }

    public Student getStudentWithMostContacts() {
        int indexMostContacts = 0;

        for (int i = 1; i < students.length; i++) {
            if (students[i].getNumContacts() > students[indexMostContacts].getNumContacts()) {
                indexMostContacts = i;
            }
            if (students[i].getNumContacts() == students[indexMostContacts].getNumContacts()) {
                if (students[i].getIndex() > students[indexMostContacts].getIndex()) {
                    indexMostContacts = i;
                }
            }
        }
        return students[indexMostContacts];
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"fakultet\":" + "\"" + name + "\", " + "\"studenti\":[");
        for (int i = 0; i < students.length; i++) {
            if (i != students.length-1) {
                sb.append(students[i].toString() + ", ");
            } else {
                sb.append(students[i].toString() + "]}");
            }
        }
        return sb.toString();
    }
}

public class ContactsTester {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int tests = scanner.nextInt();
        Faculty faculty = null;

        int rvalue = 0;
        long rindex = -1;

        DecimalFormat df = new DecimalFormat("0.00");

        for (int t = 0; t < tests; t++) {

            rvalue++;
            String operation = scanner.next();

            switch (operation) {
                case "CREATE_FACULTY": {
                    String name = scanner.nextLine().trim();
                    int N = scanner.nextInt();

                    Student[] students = new Student[N];

                    for (int i = 0; i < N; i++) {
                        rvalue++;

                        String firstName = scanner.next();
                        String lastName = scanner.next();
                        String city = scanner.next();
                        int age = scanner.nextInt();
                        long index = scanner.nextLong();

                        if ((rindex == -1) || (rvalue % 13 == 0))
                            rindex = index;

                        Student student = new Student(firstName, lastName, city,
                                age, index);
                        students[i] = student;
                    }

                    faculty = new Faculty(name, students);
                    break;
                }

                case "ADD_EMAIL_CONTACT": {
                    long index = scanner.nextInt();
                    String date = scanner.next();
                    String email = scanner.next();

                    rvalue++;

                    if ((rindex == -1) || (rvalue % 3 == 0))
                        rindex = index;

                    faculty.getStudent(index).addEmailContact(date, email);
                    break;
                }

                case "ADD_PHONE_CONTACT": {
                    long index = scanner.nextInt();
                    String date = scanner.next();
                    String phone = scanner.next();

                    rvalue++;

                    if ((rindex == -1) || (rvalue % 3 == 0))
                        rindex = index;

                    faculty.getStudent(index).addPhoneContact(date, phone);
                    break;
                }

                case "CHECK_SIMPLE": {
                    System.out.println("Average number of contacts: "
                            + df.format(faculty.getAverageNumberOfContacts()));

                    rvalue++;

                    String city = faculty.getStudent(rindex).getCity();
                    System.out.println("Number of students from " + city + ": "
                            + faculty.countStudentsFromCity(city));

                    break;
                }

                case "CHECK_DATES": {

                    rvalue++;

                    System.out.print("Latest contact: ");
                    Contact latestContact = faculty.getStudent(rindex)
                            .getLatestContact();
                    if (latestContact.getType().equals("Email"))
                        System.out.println(((EmailContact) latestContact)
                                .getEmail());
                    if (latestContact.getType().equals("Phone"))
                        System.out.println(((PhoneContact) latestContact)
                                .getPhone()
                                + " ("
                                + ((PhoneContact) latestContact).getOperator()
                                .toString() + ")");

                    if (faculty.getStudent(rindex).getEmailContacts().length > 0
                            && faculty.getStudent(rindex).getPhoneContacts().length > 0) {
                        System.out.print("Number of email and phone contacts: ");
                        System.out
                                .println(faculty.getStudent(rindex)
                                        .getEmailContacts().length
                                        + " "
                                        + faculty.getStudent(rindex)
                                        .getPhoneContacts().length);

                        System.out.print("Comparing dates: ");
                        int posEmail = rvalue
                                % faculty.getStudent(rindex).getEmailContacts().length;
                        int posPhone = rvalue
                                % faculty.getStudent(rindex).getPhoneContacts().length;

                        System.out.println(faculty.getStudent(rindex)
                                .getEmailContacts()[posEmail].isNewerThan(faculty
                                .getStudent(rindex).getPhoneContacts()[posPhone]));
                    }

                    break;
                }

                case "PRINT_FACULTY_METHODS": {
                    System.out.println("Faculty: " + faculty.toString());
                    System.out.println("Student with most contacts: "
                            + faculty.getStudentWithMostContacts().toString());
                    break;
                }

            }

        }

        scanner.close();
    }
}

