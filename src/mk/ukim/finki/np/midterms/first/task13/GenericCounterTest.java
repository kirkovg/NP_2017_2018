package mk.ukim.finki.np.midterms.first.task13;

import java.util.*;
import java.util.stream.IntStream;

public class GenericCounterTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        GenericCounter<Integer> counterInt = new GenericCounter<Integer>();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            int x = scanner.nextInt();
            counterInt.count(x);
        }
        System.out.println("=====INTEGERS=====");
        System.out.println(counterInt);
        n = scanner.nextInt();
        scanner.nextLine();
        GenericCounter<String> counterString = new GenericCounter<String>();
        for (int i = 0; i < n; i++) {
            String s = scanner.nextLine();
            counterString.count(s);
        }
        System.out.println("=====STRINGS=====");
        System.out.println(counterString);
        n = scanner.nextInt();
        scanner.nextLine();
        GenericCounter<Float> counterFloat = new GenericCounter<Float>();
        for (int i = 0; i < n; i++) {
            float f = scanner.nextFloat();
            counterFloat.count(f);
        }
        System.out.println("=====FLOATS=====");
        System.out.println(counterFloat);
        scanner.close();
    }
}

class GenericCounter<T> {

    private HashMap<T, Integer> counterMap;

    public GenericCounter() {
        this.counterMap = new HashMap<>();
    }

    public void count (T element) {
        Integer timesAppeared = counterMap.get(element);
        if (timesAppeared != null) {
            timesAppeared++;
            counterMap.put(element, timesAppeared);
        } else {
            counterMap.put(element, 1);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        counterMap.forEach((key, value) -> sb.append(key).append("|").append(getNumOfStars(value)).append("\n"));
        return sb.toString();
    }

    private String getNumOfStars(Integer num) {
        StringBuilder sb = new StringBuilder();
        IntStream.range(0, num)
                .forEach(x -> sb.append("*"));
        return sb.toString();
    }
}