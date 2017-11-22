package mk.ukim.finki.np.lab5.task2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;
import java.util.stream.IntStream;


class IntegerList {
    private ArrayList<Integer> integerList;

    public IntegerList() {
        integerList = new ArrayList<>();
    }

    public IntegerList(Integer... numbers) {
        integerList = new ArrayList<>(Arrays.asList(numbers));
    }

    public int remove(int idx) {
        return integerList.remove(idx);
    }

    public void add(int el, int idx) {
        if (idx <= integerList.size()) {
            integerList.add(idx, el);
        } else {
            IntStream.range(integerList.size(), idx).forEach(i -> integerList.add(i, 0));
            integerList.add(idx, el);
        }
    }

    public void set(int el, int idx) {
        integerList.set(idx, el);
    }

    public int get(int idx) {
        return integerList.get(idx);
    }

    public int size() {
        return integerList.size();
    }

    public int count(int el) {
        return (int) integerList.stream().filter(e -> e == el).count();
    }

    public void removeDuplicates() {
        ArrayList<Integer> tmp = integerList;
        integerList = new ArrayList<>();
        IntStream.range(0, tmp.size())
                .boxed()
                .sorted(Collections.reverseOrder())
                .filter(i -> !integerList.contains(tmp.get(i)))
                .forEach(i -> integerList.add(0, tmp.get(i)));

        /*
        * Any other time in a normal solution
        * ArrayList<Integer> tmp = new ArrayList<>();
        * integerList.stream().distinct().forEach(tmp::add);
        * integerList = tmp;
        * */
    }

    public int sumFirst(int k) {
        if (k < 0) return 0;
        if (k > size()) {
            return IntStream.range(0, size()).map(i -> integerList.get(i)).sum();
        }
        return IntStream.range(0, k).map(i -> integerList.get(i)).sum();
    }

    public int sumLast(int k) {
        if (k < 0) return 0;
        if (k > size()) {
            return IntStream.range(0, size()).map(i -> integerList.get(i)).sum();
        }
        return IntStream.range(size() - k, size()).map(i -> integerList.get(i)).sum();
    }

    public void shiftRight(int idx, int k) {
        if (idx < 0 || idx > size() - 1) throw new ArrayIndexOutOfBoundsException();

        ArrayList<Integer> shiftedList = new ArrayList<>();
        IntStream.range(0, size()).filter(i -> i != idx).forEach(i -> shiftedList.add(integerList.get(i)));

        if (idx + k < size()) {
            shiftedList.add(idx + k, integerList.get(idx));
        } else {
            int trim = idx + k;
            while (trim > integerList.size()) {
                trim -= integerList.size();
            }
            shiftedList.add(trim, integerList.get(idx));
        }
        integerList = shiftedList;
    }

    public void shiftLeft(int idx, int k) {
        if (idx < 0 || idx > size() - 1) throw new ArrayIndexOutOfBoundsException();

        ArrayList<Integer> shiftedList = new ArrayList<>();
        IntStream.range(0, size()).filter(i -> i != idx).forEach(i -> shiftedList.add(integerList.get(i)));

        if (idx - k >= 0) {
            shiftedList.add(idx - k, integerList.get(idx));
        } else {
            int trim = idx - k;
            while (trim < 0) {
                trim += integerList.size();
            }
            shiftedList.add(trim, integerList.get(idx));
        }
        integerList = shiftedList;
    }

    public IntegerList addValue(int value) {
        Integer[] arr = integerList.stream().map(x -> x + value).toArray(Integer[]::new);
        return new IntegerList(arr);
    }
}

public class IntegerListTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if (k == 0) { //test standard methods
            int subtest = jin.nextInt();
            if (subtest == 0) {
                IntegerList list = new IntegerList();
                while (true) {
                    int num = jin.nextInt();
                    if (num == 0) {
                        list.add(jin.nextInt(), jin.nextInt());
                    }
                    if (num == 1) {
                        list.remove(jin.nextInt());
                    }
                    if (num == 2) {
                        print(list);
                    }
                    if (num == 3) {
                        break;
                    }
                }
            }
            if (subtest == 1) {
                int n = jin.nextInt();
                Integer a[] = new Integer[n];
                for (int i = 0; i < n; ++i) {
                    a[i] = jin.nextInt();
                }
                IntegerList list = new IntegerList(a);
                print(list);
            }
        }
        if (k == 1) { //test count,remove duplicates, addValue
            int n = jin.nextInt();
            Integer a[] = new Integer[n];
            for (int i = 0; i < n; ++i) {
                a[i] = jin.nextInt();
            }
            IntegerList list = new IntegerList(a);
            while (true) {
                int num = jin.nextInt();
                if (num == 0) { //count
                    System.out.println(list.count(jin.nextInt()));
                }
                if (num == 1) {
                    list.removeDuplicates();
                }
                if (num == 2) {
                    print(list.addValue(jin.nextInt()));
                }
                if (num == 3) {
                    list.add(jin.nextInt(), jin.nextInt());
                }
                if (num == 4) {
                    print(list);
                }
                if (num == 5) {
                    break;
                }
            }
        }
        if (k == 2) { //test shiftRight, shiftLeft, sumFirst , sumLast
            int n = jin.nextInt();
            Integer a[] = new Integer[n];
            for (int i = 0; i < n; ++i) {
                a[i] = jin.nextInt();
            }
            IntegerList list = new IntegerList(a);
            while (true) {
                int num = jin.nextInt();
                if (num == 0) { //count
                    list.shiftLeft(jin.nextInt(), jin.nextInt());
                }
                if (num == 1) {
                    list.shiftRight(jin.nextInt(), jin.nextInt());
                }
                if (num == 2) {
                    System.out.println(list.sumFirst(jin.nextInt()));
                }
                if (num == 3) {
                    System.out.println(list.sumLast(jin.nextInt()));
                }
                if (num == 4) {
                    print(list);
                }
                if (num == 5) {
                    break;
                }
            }
        }
    }

    public static void print(IntegerList il) {
        if (il.size() == 0) System.out.print("EMPTY");
        for (int i = 0; i < il.size(); ++i) {
            if (i > 0) System.out.print(" ");
            System.out.print(il.get(i));
        }
        System.out.println();
    }

}
