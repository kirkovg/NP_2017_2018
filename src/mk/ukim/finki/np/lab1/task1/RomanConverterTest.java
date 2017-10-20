package mk.ukim.finki.np.lab1.task1;

import java.util.Scanner;
import java.util.TreeMap;
import java.util.stream.IntStream;

public class RomanConverterTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        IntStream.range(0, n)
                .forEach(x -> System.out.println(RomanConverter.toRoman(scanner.nextInt())));
        scanner.close();
    }
}


class RomanConverter {

    private static final TreeMap<Integer ,String> decimalToRomanMap = new TreeMap<>();

    static {
        decimalToRomanMap.put(1000, "M");
        decimalToRomanMap.put(900, "CM");
        decimalToRomanMap.put(500, "D");
        decimalToRomanMap.put(400, "CD");
        decimalToRomanMap.put(100, "C");
        decimalToRomanMap.put(90, "XC");
        decimalToRomanMap.put(50, "L");
        decimalToRomanMap.put(40, "XL");
        decimalToRomanMap.put(10, "X");
        decimalToRomanMap.put(9, "IX");
        decimalToRomanMap.put(5, "V");
        decimalToRomanMap.put(4, "IV");
        decimalToRomanMap.put(1, "I");
    }
    /**
     * Roman to decimal converter
     *
     * @param n number in decimal format
     * @return string representation of the number in Roman numeral
     */
    public static String toRoman(int n) {
        // your solution here
        int l = decimalToRomanMap.floorKey(n);
        if (n == l) {
            return decimalToRomanMap.get(n);
        }
        return decimalToRomanMap.get(l) + toRoman(n - l);
    }

}