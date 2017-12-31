package mk.ukim.finki.np.midterms.second;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class WeatherStationTest {
    public static void main(String[] args) throws ParseException {
        Scanner scanner = new Scanner(System.in);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        int n = scanner.nextInt();
        scanner.nextLine();
        WeatherStation ws = new WeatherStation(n);
        while (true) {
            String line = scanner.nextLine();
            if (line.equals("=====")) {
                break;
            }
            String[] parts = line.split(" ");
            float temp = Float.parseFloat(parts[0]);
            float wind = Float.parseFloat(parts[1]);
            float hum = Float.parseFloat(parts[2]);
            float vis = Float.parseFloat(parts[3]);
            line = scanner.nextLine();
            Date date = df.parse(line);
            ws.addMeasurement(temp, wind, hum, vis, date);
        }
        String line = scanner.nextLine();
        Date from = df.parse(line);
        line = scanner.nextLine();
        Date to = df.parse(line);
        scanner.close();
        System.out.println(ws.total());
        try {
            ws.status(from, to);
        } catch (RuntimeException e) {
            System.out.println(e);
        }
    }
}

class Measurement implements Comparable<Measurement> {
    private float temperature;
    private float humidity;
    private float windSpeed;
    private float visibility;
    private Date date;

    public Measurement(float temperature, float humidity, float windSpeed, float visibility, Date date) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.visibility = visibility;
        this.date = date;
    }

    public float getTemperature() {
        return temperature;
    }

    public float getHumidity() {
        return humidity;
    }

    public float getWindSpeed() {
        return windSpeed;
    }

    public float getVisibility() {
        return visibility;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public String toString() {
        DateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz YYY");
        return String
                .format("%.1f %.1f km/h %.1f%% %.1f km %s", temperature, windSpeed, humidity, visibility, df.format(date));
    }

    @Override
    public int compareTo(Measurement o) {
        long time1 = this.date.getTime() / 1000;
        long time2 = o.date.getTime() / 1000;
        if (Math.abs(time1 - time2) < 150) {
            return 0;
        }
        return date.compareTo(o.date);
    }
}

class WeatherStation {
    private int days;
    private TreeSet<Measurement> measurements;

    public WeatherStation(int days) {
        this.days = days;
        this.measurements = new TreeSet<>();
    }

    public void addMeasurement(float temperature, float wind, float humidity, float visibility, Date date) {
        Measurement m = new Measurement(temperature, humidity, wind, visibility, date);
        if (!measurements.add(m)) {
            return;
        }
        Iterator<Measurement> it = measurements.iterator();

        while (it.hasNext()) {
            m = it.next();
            long time1 = m.getDate().getMonth() * 31 + m.getDate().getDate();
            long time2 = date.getMonth() * 31 + date.getDate();
            if (time2 - time1 >= days) {
                it.remove();
            }
        }
    }

    public int total() {
        return measurements.size();
    }

    public void status(Date from, Date to) {
        Predicate<Measurement> inRange =
                measurement -> (measurement.getDate().after(from) || measurement.getDate().equals(from) )
                        && (measurement.getDate().before(to) || measurement.getDate().equals(to));

        List<Measurement> entries = measurements.stream()
                .filter(inRange)
                .collect(Collectors.toList());
        if (entries.size() == 0) {
            throw new RuntimeException();
        } else {
            double avg = measurements.stream()
                    .filter(inRange)
                    .mapToDouble(Measurement::getTemperature)
                    .average()
                    .orElse(0);

            entries.forEach(System.out::println);
            System.out.println(String.format("Average temperature: %.2f", avg));
        }

    }

}
