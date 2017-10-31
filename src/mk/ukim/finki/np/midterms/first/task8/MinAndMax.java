package mk.ukim.finki.np.midterms.first.task8;


import java.util.Scanner;

class MinMax<T extends Comparable<T>> {
    private T min;
    private T max;
    private int maxCount;
    private int minCount;
    private int total;

    public MinMax() {
        maxCount = minCount = total = 0;
    }

    public void update(T element) {
        if (total == 0) {
            max = min = element;
        }
        total++;

        if (element.compareTo(max) > 0) {
            max = element;
            maxCount = 1;
        } else if (element.compareTo(max) == 0) {
            maxCount++;
        }

        if (element.compareTo(min) < 0) {
            min = element;
            minCount = 1;
        } else if (element.compareTo(min) == 0) {
            minCount++;
        }
    }

    public T max() {
        return max;
    }

    public T min() {
        return min;
    }

    @Override
    public String toString() {
        return String.format("%s %s %d\n", min, max, total - (maxCount + minCount));
    }
}

public class MinAndMax {
    public static void main(String[] args) throws ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        MinMax<String> strings = new MinMax<String>();
        for (int i = 0; i < n; ++i) {
            String s = scanner.next();
            strings.update(s);
        }
        System.out.println(strings);
        MinMax<Integer> ints = new MinMax<Integer>();
        for (int i = 0; i < n; ++i) {
            int x = scanner.nextInt();
            ints.update(x);
        }
        System.out.println(ints);
    }
}