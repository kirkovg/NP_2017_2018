package mk.ukim.finki.np.midterms.first.task1;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.IntStream;

import static java.lang.Integer.parseInt;

class Time implements Comparable<Time> {
    private int minutes;
    private int seconds;
    private int miliSeconds;

    public Time(int[] time) {
        this.minutes = time[0];
        this.seconds = time[1];
        this.miliSeconds = time[2];
    }

    @Override
    public int compareTo(Time o) {
        if (this.minutes > o.minutes) return 1;
        else if (this.minutes < o.minutes) return -1;
        else if (this.seconds > o.seconds) return 1;
        else if (this.seconds < o.seconds) return -1;
        else if (this.miliSeconds > o.miliSeconds) return 1;
        else if (this.miliSeconds < o.miliSeconds) return -1;
        else return 0;
    }

    @Override
    public String toString() {
        return String.format("%01d:%02d:%03d", minutes, seconds, miliSeconds);
    }
}

class Driver implements Comparable<Driver> {
    private String name;
    private List<Time> times;

    public Driver(String name, List<Time> times) {
        this.name = name;
        this.times = times;
    }

    private Time getBestTime() {
        Collections.sort(times);
        return times.get(0);
    }

    @Override
    public int compareTo(Driver o) {
        if (this.getBestTime().compareTo(o.getBestTime()) >= 1) {
            return 1;
        } else if (this.getBestTime().compareTo(o.getBestTime()) <= -1) {
            return -1;
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        return String.format("%-10s%10s", this.name, this.getBestTime().toString());
    }
}

class F1Race {
    private List<Driver> drivers;

    public F1Race() {
        this.drivers = new ArrayList<>();
    }

    public void readResults(InputStream inputStream) {
        Scanner scanner = new Scanner(inputStream);
        while (scanner.hasNextLine()) {
            String[] parts = scanner.nextLine().split(" ");
            String name = parts[0];
            Time t1 = new Time(parseTime(parts[1]));
            Time t2 = new Time(parseTime(parts[2]));
            Time t3 = new Time(parseTime(parts[3]));
            Driver driver = new Driver(name, Arrays.asList(t1, t2, t3));
            drivers.add(driver);
        }
    }

    public void printSorted(OutputStream outputStream) {
        PrintWriter printWriter = new PrintWriter(outputStream);
        Collections.sort(drivers);
        IntStream.range(0, drivers.size())
                .forEach(i -> printWriter.printf("%d. %s\n", i + 1, drivers.get(i).toString()));
        printWriter.flush();
    }

    private int[] parseTime(String time) {
        int[] timeArr = new int[3];
        String[] parts = time.split(":");
        timeArr[0] = parseInt(parts[0]); // seconds
        timeArr[1] = parseInt(parts[1]); // minutes
        timeArr[2] = parseInt(parts[2]); // miliSeconds
        return timeArr;
    }

}

public class F1Test {

    public static void main(String[] args) {
        F1Race f1Race = new F1Race();
        f1Race.readResults(System.in);
        f1Race.printSorted(System.out);
    }

}