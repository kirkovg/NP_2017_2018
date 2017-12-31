package mk.ukim.finki.np.midterms.second;


import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.IntStream;

public class EventCalendarTest {
    public static void main(String[] args) throws ParseException {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        int year = scanner.nextInt();
        scanner.nextLine();
        EventCalendar eventCalendar = new EventCalendar(year);
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            String name = parts[0];
            String location = parts[1];
            LocalDateTime dateTime = LocalDateTime.parse(parts[2], df);
            try {
                eventCalendar.addEvent(name, location, dateTime);
            } catch (WrongDateException e) {
                System.out.println(e.getMessage());
            }
        }
        LocalDateTime dateTime = LocalDateTime.parse(scanner.nextLine(), df);
        eventCalendar.listEvents(dateTime);
        eventCalendar.listByMonth();
    }
}

class WrongDateException extends Exception {
    public WrongDateException(String message) {
        super(message);
    }
}


class Event implements Comparable<Event> {
    private String name;
    private String location;
    private LocalDateTime date;

    public Event(String name, String location, LocalDateTime date) {
        this.name = name;
        this.location = location;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public static String getKey(LocalDateTime date) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd.MM");
        return date.format(df);
    }

    @Override
    public String toString() {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd MMM, YYYY HH:mm");
        return String.format("%s at %s, %s", date.format(df), location, name);
    }

    @Override
    public int compareTo(Event o) {
        int cmp = date.compareTo(o.getDate());
        if (cmp == 0) {
            return name.compareTo(o.getName());
        }
        return cmp;
    }
}

class EventCalendar {
    private HashMap<String, TreeSet<Event>> events;
    private HashMap<Integer, Integer> eventCount;
    private int year;

    public EventCalendar(int year) {
        this.year = year;
        this.events = new HashMap<>();
        this.eventCount = new HashMap<>();
        IntStream.range(1, 13).forEach(i -> eventCount.put(i, 0));
    }

    public void addEvent(String name, String location, LocalDateTime date) throws WrongDateException {
        if (date.getYear() != year) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy");
            ZonedDateTime zonedDateTime = ZonedDateTime.of(date, ZoneId.of("UTC"));
            throw new WrongDateException("Wrong date: " + zonedDateTime.format(dateTimeFormatter));
        }
        Event event = new Event(name, location, date);

        String key = Event.getKey(date);
        TreeSet<Event> eventTreeSet = events.computeIfAbsent(key, k -> new TreeSet<>());
        eventTreeSet.add(event);

        Integer cnt = eventCount.get(date.getMonth().getValue());
        if (cnt == null) {
            cnt = 0;
        }
        cnt++;
        eventCount.put(date.getMonth().getValue(), cnt);
    }

    public void listEvents(LocalDateTime date) {
        TreeSet<Event> eventTreeSet = events.get(Event.getKey(date));
        if (eventTreeSet == null) {
            System.out.println("No events on this day!");
        } else {
            eventTreeSet.forEach(System.out::println);
        }
    }

    public void listByMonth() {
        eventCount.keySet().forEach(key -> {
            Integer count = eventCount.get(key);
            if (count == null) {
                count = 0;
            }
            System.out.printf("%d : %d\n", key, count);
        });
    }
}