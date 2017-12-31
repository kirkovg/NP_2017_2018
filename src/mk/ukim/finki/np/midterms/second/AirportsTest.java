package mk.ukim.finki.np.midterms.second;

import java.util.*;

public class AirportsTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Airports airports = new Airports();
        int n = scanner.nextInt();
        scanner.nextLine();
        String[] codes = new String[n];
        for (int i = 0; i < n; ++i) {
            String al = scanner.nextLine();
            String[] parts = al.split(";");
            airports.addAirport(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]));
            codes[i] = parts[2];
        }
        int nn = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < nn; ++i) {
            String fl = scanner.nextLine();
            String[] parts = fl.split(";");
            airports.addFlights(parts[0], parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
        }
        int f = scanner.nextInt();
        int t = scanner.nextInt();
        String from = codes[f];
        String to = codes[t];
        System.out.printf("===== FLIGHTS FROM %S =====\n", from);
        airports.showFlightsFromAirport(from);
        System.out.printf("===== DIRECT FLIGHTS FROM %S TO %S =====\n", from, to);
        airports.showDirectFlightsFromTo(from, to);
        t += 5;
        t = t % n;
        to = codes[t];
        System.out.printf("===== DIRECT FLIGHTS TO %S =====\n", to);
        airports.showDirectFlightsTo(to);
    }
}

class Airport {
    private String name;
    private String country;
    private String code;
    private int passengers;
    private Map<String, Set<Flight>> flights;

    public Airport(String name, String country, String code, int passengers) {
        this.name = name;
        this.country = country;
        this.code = code;
        this.passengers = passengers;
        this.flights = new TreeMap<>();
    }

    public Map<String, Set<Flight>> getFlights() {
        return flights;
    }

    public void addFlight(String from, String to, int time, int duration) {
        Set<Flight> flightSet = flights.computeIfAbsent(to, k -> new TreeSet<>());
        flightSet.add(new Flight(from, to, time, duration));
    }

    @Override
    public String toString() {
        return name + " " + "(" + code + ")" + "\n" +
                country + "\n" +
                passengers;
    }
}


class Flight implements Comparable<Flight>{
    private String codeFrom;
    private String codeTo;
    private int time;
    private int duration;

    public Flight(String codeFrom, String codeTo, int time, int duration) {
        this.codeFrom = codeFrom;
        this.codeTo = codeTo;
        this.time = time;
        this.duration = duration;
    }

    @Override
    public String toString() {
        int end = time + duration;
        int plus = end / (24 * 60);
        end %= (24 * 60);
        return String.format("%s-%s %02d:%02d-%02d:%02d%s %dh%02dm", codeFrom, codeTo, time / 60, time % 60,
                end / 60, end % 60, plus > 0 ? " +1d" : "", duration / 60, duration % 60);
    }

    @Override
    public int compareTo(Flight o) {
        int x = Integer.compare(this.time, o.time);
        if (x == 0) {
            return this.codeFrom.compareTo(o.codeFrom);
        }
        return x;
    }
}

class Airports {

    private TreeMap<String, Airport> airportTreeMap;

    public Airports() {
        airportTreeMap = new TreeMap<>();
    }


    public void addAirport(String name, String country, String code, int passengers) {
        airportTreeMap.put(code, new Airport(name, country, code, passengers));
    }

    public void addFlights(String from, String to, int time, int duration) {
        Airport airport = airportTreeMap.get(from);
        airport.addFlight(from, to, time, duration);
    }

    public void showFlightsFromAirport(String code) {
        Airport airport = airportTreeMap.get(code);
        System.out.println(airport);
        int num = 1;
        for (String toCode : airport.getFlights().keySet()) {
            Set<Flight> flightSet = airport.getFlights().get(toCode);
            for (Flight flight : flightSet) {
                System.out.println(String.format("%d. %s", num, flight));
                num++;
            }
        }
    }

    public void showDirectFlightsFromTo(String from, String to) {
        Airport fromAirport = airportTreeMap.get(from);
        Set<Flight> flightSet = fromAirport.getFlights().get(to);
        if (flightSet != null) {
            flightSet.forEach(System.out::println);
        } else {
            System.out.printf("No flights from %s to %s\n", from, to);
        }
    }

    public void showDirectFlightsTo(String to) {
        Set<Flight> flightSet = new TreeSet<>();
        airportTreeMap.values().stream()
                .map(flightsTo -> flightsTo.getFlights().get(to))
                .filter(Objects::nonNull)
                .forEach(flightSet::addAll);

        flightSet.forEach(System.out::println);
    }
}
