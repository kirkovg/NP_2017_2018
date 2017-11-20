package mk.ukim.finki.np.lab5;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.stream.IntStream;

class SuperString {
    private LinkedList<String> stringList;
    private LinkedList<String> latestAdded;

    public SuperString() {
        stringList = new LinkedList<>();
        latestAdded = new LinkedList<>();
    }

    public void append(String s) {
        stringList.add(s);
        latestAdded.addFirst(s);
    }

    public void insert(String s) {
        stringList.addFirst(s);
        latestAdded.addFirst(s);
    }

    public boolean contains(String s) {
        String wholeStr = this.toString();
        return wholeStr.contains(s);
    }

    public void reverse() {
        Collections.reverse(stringList);
        LinkedList<String> copy = new LinkedList<>();
        stringList.forEach(s -> copy.add(reverseStr(s)));
        stringList = copy;
        LinkedList<String> copyLatest = new LinkedList<>();
        latestAdded.forEach(s -> copyLatest.add(reverseStr(s)));
        latestAdded = copyLatest;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        stringList.forEach(sb::append);
        return sb.toString();
    }

    public void removeLast(int k) {
        IntStream.range(0, k).forEach(x -> {
            stringList.remove(latestAdded.getFirst());
            latestAdded.removeFirst();
        });
    }

    private String reverseStr(String s) {
        StringBuilder sb = new StringBuilder();
        sb.append(s);
        sb.reverse();
        return sb.toString();
    }
}


public class SuperStringTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if (k == 0) {
            SuperString s = new SuperString();
            while (true) {
                int command = jin.nextInt();
                if (command == 0) {//append(String s)
                    s.append(jin.next());
                }
                if (command == 1) {//insert(String s)
                    s.insert(jin.next());
                }
                if (command == 2) {//contains(String s)
                    System.out.println(s.contains(jin.next()));
                }
                if (command == 3) {//reverse()
                    s.reverse();
                }
                if (command == 4) {//toString()
                    System.out.println(s);
                }
                if (command == 5) {//removeLast(int k)
                    s.removeLast(jin.nextInt());
                }
                if (command == 6) {//end
                    break;
                }
            }
        }
    }

}
