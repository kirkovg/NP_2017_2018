package mk.ukim.finki.np.midterms.first.task10;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;
import static java.util.Collections.*;
import static java.util.stream.Collectors.toList;

class Sample implements Comparable<Sample> {
    private int dayOfTheYear;
    private List<Double> fahrenheitTemperatures;
    private List<Double> celsiusTemperatures;

    public Sample(int dayOfTheYear, List<Double> fahrenheitTemperatures, List<Double> celsiusTemperatures) {
        this.dayOfTheYear = dayOfTheYear;
        this.fahrenheitTemperatures = fahrenheitTemperatures;
        this.celsiusTemperatures = celsiusTemperatures;
    }

    @Override
    public int compareTo(Sample o) {
        return Integer.compare(this.dayOfTheYear, o.dayOfTheYear);
    }

    public int totalCount() {
        return fahrenheitTemperatures.size() + celsiusTemperatures.size();
    }

    public List<Double> convertAllToFahrenheit() {
        List<Double> temp = celsiusTemperatures.stream()
                .map(x -> ((x * 9) / 5) + 32)
                .collect(toList());

        List<Double> temp2 = fahrenheitTemperatures;
        temp.addAll(temp2);
        return temp;
    }

    public List<Double> convertAllToCelsius() {
        List<Double> temp = fahrenheitTemperatures.stream()
                .map(x -> ((x - 32) * 5) / 9)
                .collect(toList());

        List<Double> temp2 = celsiusTemperatures;
        temp.addAll(temp2);
        return temp;
    }

    public double getFahrenheitMax() {
        List<Double> temp = convertAllToFahrenheit();
        return max(temp);
    }

    public double getCelsiusMax() {
        List<Double> temp = convertAllToCelsius();
        return max(temp);
    }

    public double getFahrenheitMin() {
        List<Double> temp = convertAllToFahrenheit();
        return min(temp);
    }

    public double getCelsiusMin() {
        List<Double> temp = convertAllToCelsius();
        return min(temp);
    }

    public double getFahrenheitAvg() {
        List<Double> temp = convertAllToFahrenheit();
        return temp.stream().mapToDouble(x -> x).average().getAsDouble();
    }

    public double getCelsiusAvg() {
        List<Double> temp = convertAllToCelsius();
        return temp.stream().mapToDouble(x -> x).average().getAsDouble();
    }

    public String toFormattedString(char scale) {
        if (scale == 'C') {
            return String.format("%3d: Count:%4d Min:%7.2fC Max:%7.2fC Avg:%7.2fC",
                    dayOfTheYear,
                    totalCount(),
                    getCelsiusMin(),
                    getCelsiusMax(),
                    getCelsiusAvg()
            );
        } else {
            return String.format("%3d: Count:%4d Min:%7.2fF Max:%7.2fF Avg:%7.2fF",
                    dayOfTheYear,
                    totalCount(),
                    getFahrenheitMin(),
                    getFahrenheitMax(),
                    getFahrenheitAvg()
            );
        }
    }
}

class DailyTemperatures {

    private List<Sample> samples;

    public DailyTemperatures() {
        this.samples = new ArrayList<>();
    }

    public void readTemperatures(InputStream inputStream) {
        Scanner scanner = new Scanner(inputStream);

        while (scanner.hasNextLine()) {
            String[] parts = scanner.nextLine().split(" ");
            int day = parseInt(parts[0]);
            List<Double> fahrenheits = new ArrayList<>();
            List<Double> celsiuses = new ArrayList<>();
            for (String part : parts) {
                if (part.charAt(part.length() - 1) == 'C') {
                    celsiuses.add(parseTempToInteger(part));
                } else if (part.charAt(part.length() - 1) == 'F'){
                    fahrenheits.add(parseTempToInteger(part));
                }
            }
            Sample sample = new Sample(day, fahrenheits, celsiuses);
            samples.add(sample);
        }

        scanner.close();
    }

    public void writeDailyStats(OutputStream outputStream, char scale) {
        PrintWriter printWriter = new PrintWriter(outputStream);
        sort(samples);
        samples.forEach(
                sample -> printWriter.printf("%s\n", sample.toFormattedString(scale)));
        printWriter.flush();
    }

    public double parseTempToInteger(String temp) {
        return parseDouble(temp.substring(0, temp.length() - 1));
    }
}

public class DailyTemperatureTest {
    public static void main(String[] args) {
        DailyTemperatures dailyTemperatures = new DailyTemperatures();
        dailyTemperatures.readTemperatures(System.in);
        System.out.println("=== Daily temperatures in Celsius (C) ===");
        dailyTemperatures.writeDailyStats(System.out, 'C');
        System.out.println("=== Daily temperatures in Fahrenheit (F) ===");
        dailyTemperatures.writeDailyStats(System.out, 'F');
    }
}

