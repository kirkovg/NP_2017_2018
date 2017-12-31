package mk.ukim.finki.np.midterms.second;

import java.util.*;

public class AuditionTest {
    public static void main(String[] args) {
        Audition audition = new Audition();
        List<String> cities = new ArrayList<String>();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            if (parts.length > 1) {
                audition.addParticipant(parts[0], parts[1], parts[2],
                        Integer.parseInt(parts[3]));
            } else {
                cities.add(line);
            }
        }
        for (String city : cities) {
            System.out.printf("+++++ %s +++++\n", city);
            audition.listByCity(city);
        }
        scanner.close();
    }
}

class Participant {
    private String code;
    private String name;
    private int age;

    public Participant(String code, String name, int age) {
        this.code = code;
        this.name = name;
        this.age = age;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    @Override
    public String toString() {
        return String.format("%s %s %d", code, name, age);
    }

    @Override
    public boolean equals(Object object) {
        Participant p = (Participant) object;
        return this.code.equals(p.code);
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }
}

class Audition {
    private TreeMap<String, HashSet<Participant>> map;

    public Audition() {
        map = new TreeMap<>();
    }

    public void addParticipant(String city, String code, String name, int age) {
        Participant participant = new Participant(code, name, age);
        HashSet<Participant> participantSet = map.computeIfAbsent(city, k -> new HashSet<>());
        participantSet.add(participant);
    }

    public void listByCity(String city) {
        HashSet<Participant> set = map.get(city);
        List<Participant> byCity = new ArrayList<>(set);
        byCity.stream()
                .sorted(Comparator.comparing(Participant::getName).thenComparing(Participant::getAge))
                .forEach(System.out::println);
    }
}
