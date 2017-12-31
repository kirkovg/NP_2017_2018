package mk.ukim.finki.np.midterms.second;

import java.util.*;
import java.util.stream.Collectors;

public class NamesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        Names names = new Names();
        for (int i = 0; i < n; ++i) {
            String name = scanner.nextLine();
            names.addName(name);
        }
        n = scanner.nextInt();
        System.out.printf("===== PRINT NAMES APPEARING AT LEAST %d TIMES =====\n", n);
        names.printN(n);
        System.out.println("===== FIND NAME =====");
        int len = scanner.nextInt();
        int index = scanner.nextInt();
        System.out.println(names.findName(len, index));
        scanner.close();

    }
}

class Names {
    private TreeMap<String, Integer> names;

    public Names() {
        names = new TreeMap<>();
    }

    public void addName(String name) {
        Integer count = names.get(name);
        if (count == null) {
            count = 0;
        }
        count++;
        names.put(name, count);
    }

    public void printN(int n) {
        names.entrySet().stream()
                .filter(entry -> entry.getValue() >= n)
                .map(entry -> String.format("%s (%d) %d", entry.getKey(), entry.getValue(), countUniqueChars(entry.getKey())))
                .forEach(System.out::println);
    }

    public String findName(int len, int x) {
        List<String> filtered = names.keySet().stream()
                .filter(name -> name.length() < len)
                .collect(Collectors.toList());


        x = x % filtered.size();
        return filtered.get(x);
    }

    private long countUniqueChars(String name) {
        return name.toLowerCase().chars()
                .distinct()
                .count();
    }
}