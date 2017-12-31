package mk.ukim.finki.np.midterms.second;

import java.util.*;
import java.util.stream.IntStream;

public class StadiumTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        String[] sectorNames = new String[n];
        int[] sectorSizes = new int[n];
        String name = scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            sectorNames[i] = parts[0];
            sectorSizes[i] = Integer.parseInt(parts[1]);
        }
        Stadium stadium = new Stadium(name);
        stadium.createSectors(sectorNames, sectorSizes);
        n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            try {
                stadium.buyTicket(parts[0], Integer.parseInt(parts[1]),
                        Integer.parseInt(parts[2]));
            } catch (SeatNotAllowedException e) {
                System.out.println("SeatNotAllowedException");
            } catch (SeatTakenException e) {
                System.out.println("SeatTakenException");
            }
        }
        stadium.showSectors();
    }
}

class Sector {
    private String code;
    private int numSeats;
    private HashMap<Integer, Integer> takenSeats;
    private HashSet<Integer> types;

    public Sector(String code, int numSeats) {
        this.code = code;
        this.numSeats = numSeats;
        this.takenSeats = new HashMap<>();
        this.types = new HashSet<>();
    }

    public String getCode() {
        return code;
    }

    public boolean isTaken(int seat) {
        return takenSeats.containsKey(seat);
    }

    public void takeSeat(int seat, int type) throws SeatNotAllowedException {
        if (type == 1 && types.contains(2)) throw new SeatNotAllowedException();
        if (type == 2 && types.contains(1)) throw new SeatNotAllowedException();

        types.add(type);
        takenSeats.put(seat, type);
    }

    public int free() {
        return numSeats - takenSeats.size();
    }

    @Override
    public String toString() {
        return String.format("%s\t%d/%d\t%.1f%%", code, free(), numSeats, (numSeats - free()) * 100.0 / numSeats);
    }
}

class Stadium {
    private String name;
    private HashMap<String, Sector> sectors;

    public Stadium(String name) {
        this.name = name;
        this.sectors = new HashMap<>();
    }

    public void createSectors(String[] sectorNames, int[] sectorSizes) {
        IntStream
                .range(0, sectorNames.length)
                .forEach(i -> sectors.put(sectorNames[i], new Sector(sectorNames[i], sectorSizes[i])));
    }

    public void buyTicket(String sectorName, int seat, int type) throws SeatTakenException, SeatNotAllowedException {
        Sector sector = sectors.get(sectorName);
        if (sector.isTaken(seat))
            throw new SeatTakenException();
        sector.takeSeat(seat, type);
    }

    public void showSectors() {
        sectors.values().stream()
                .sorted(Comparator.comparing(Sector::free).reversed().thenComparing(Sector::getCode))
                .forEach(System.out::println);
    }
}

class SeatTakenException extends Exception {

}

class SeatNotAllowedException extends Exception {

}
