package mk.ukim.finki.np.midterms.first.task7;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import static java.lang.Integer.parseInt;

class InvalidTimeException extends Exception {
    public InvalidTimeException(String message) {
        super(message);
    }
}

class UnsupportedFormatException extends Exception {
    public UnsupportedFormatException(String message) {
        super(message);
    }
}

class Time implements Comparable<Time> {
    private int hours;
    private int minutes;

    public Time(int hours, int minutes) {
        this.hours = hours;
        this.minutes = minutes;
    }

    @Override
    public int compareTo(Time o) {
        if (this.hours > o.hours) return 1;
        else if (this.hours < o.hours) return -1;
        else if (this.minutes > o.minutes) return 1;
        else if (this.minutes < o.minutes) return -1;
        else return 0;
    }

    public String convertToAMPM() {
        StringBuilder timeString = new StringBuilder();
        int toReturn = 1;

        if (hours == 0)
            toReturn = 1;
        else if (hours > 0 && hours < 12)
            toReturn = 2;
        if ((hours == 12) && (minutes >= 0 && minutes < 60))
            toReturn = 3;
        else if (hours > 12 && hours < 24)
            toReturn = 4;

        switch (toReturn) {
            case 1:
                hours += 12;
                if (hours >= 0 && hours <= 9) {
                    if (minutes >= 0 && minutes <= 9) {
                        timeString.append(String.format(" %d:0%d AM", hours, minutes));
                    } else {
                        timeString.append(String.format(" %d:%d AM", hours, minutes));
                    }
                } else {
                    if (minutes >= 0 && minutes <= 9) {
                        timeString.append(String.format("%d:0%d AM", hours, minutes));
                    } else {
                        timeString.append(String.format("%d:%d AM", hours, minutes));
                    }
                }
                break;
            case 2:
                if (hours <= 9) {
                    if (minutes >= 0 && minutes <= 9) {
                        timeString.append(String.format(" %d:0%d AM", hours, minutes));
                    } else {
                        timeString.append(String.format(" %d:%d AM", hours, minutes));
                    }
                } else {
                    if (minutes >= 0 && minutes <= 9) {
                        timeString.append(String.format("%d:0%d AM", hours, minutes));
                    } else {
                        timeString.append(String.format("%d:%d AM", hours, minutes));
                    }
                }
                break;
            case 3:
                if (minutes <= 9) {
                    timeString.append(String.format("%d:0%d PM", hours, minutes));
                } else {
                    timeString.append(String.format("%d:%d PM", hours, minutes));
                }
                break;
            case 4:
                hours = Math.abs(hours - 12);
                if (hours >= 0 && hours <= 9) {
                    if (minutes >= 0 && minutes <= 9) {
                        timeString.append(String.format(" %d:0%d PM", hours, minutes));
                    } else {
                        timeString.append(String.format(" %d:%d PM", hours, minutes));
                    }
                } else {
                    if (minutes >= 0 && minutes <= 9) {
                        timeString.append(String.format("%d:0%d PM", hours, minutes));
                    } else {
                        timeString.append(String.format("%d:%d PM", hours, minutes));
                    }
                }

                break;
        }


        return timeString.toString();
    }

    @Override
    public String toString() {
        if (hours >= 0 && hours <= 9) {
            if (minutes >= 0 && minutes <= 9) {
                return String.format(" %d:0%d", hours, minutes);
            } else {
                return String.format(" %d:%d", hours, minutes);
            }
        } else {
            if (minutes >= 0 && minutes <= 9) {
                return String.format("%d:0%d", hours, minutes);
            } else {
                return String.format("%d:%d", hours, minutes);
            }
        }
    }
}

class TimeTable {
    private List<Time> timeList;

    public TimeTable() {
        this.timeList = new ArrayList<>();
    }

    public void readTimes(InputStream inputStream) throws UnsupportedFormatException, InvalidTimeException {
        Scanner scanner = new Scanner(inputStream);
        while (scanner.hasNextLine()) {
            String[] str = scanner.nextLine().split(" ");
            for (String s : str) {
                if (s.charAt(s.length() - 3) != ':' && s.charAt(s.length() - 3) != '.') {
                    throw new UnsupportedFormatException(s);
                }
                String[] hoursAndMinutes = s.split("[:.]");
                int hours = parseInt(hoursAndMinutes[0]);
                int minutes = parseInt(hoursAndMinutes[1]);
                if ((hours < 0 || hours > 23) || (minutes < 0 || minutes > 59)) {
                    throw new InvalidTimeException(s);
                }

                Time t = new Time(hours, minutes);
                timeList.add(t);
            }
        }
        scanner.close();
    }

    public void writeTimes(OutputStream outputStream, TimeFormat timeFormat) {
        PrintWriter printWriter = new PrintWriter(outputStream);
        Collections.sort(timeList);

        if (TimeFormat.FORMAT_24.equals(timeFormat)) {
            timeList.forEach(t -> printWriter.println(t.toString()));
        } else if (TimeFormat.FORMAT_AMPM.equals(timeFormat)) {
            timeList.forEach(t -> printWriter.println(t.convertToAMPM()));
        }
        printWriter.flush();
    }
}


public class TimesTest {

    public static void main(String[] args) {
        TimeTable timeTable = new TimeTable();
        try {
            timeTable.readTimes(System.in);
        } catch (UnsupportedFormatException e) {
            System.out.println("UnsupportedFormatException: " + e.getMessage());
        } catch (InvalidTimeException e) {
            System.out.println("InvalidTimeException: " + e.getMessage());
        }
        System.out.println("24 HOUR FORMAT");
        timeTable.writeTimes(System.out, TimeFormat.FORMAT_24);
        System.out.println("AM/PM FORMAT");
        timeTable.writeTimes(System.out, TimeFormat.FORMAT_AMPM);
    }

}

enum TimeFormat {
    FORMAT_24, FORMAT_AMPM
}
