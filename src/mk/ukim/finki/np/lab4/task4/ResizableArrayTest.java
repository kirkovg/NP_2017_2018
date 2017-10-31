package mk.ukim.finki.np.lab4.task4;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.stream.IntStream;


class ResizableArray<T> {
    private T[] elements;
    private int totalElements;

    public ResizableArray() {
        this.elements = (T[]) new Object[0];
        this.totalElements = 0;
    }

    public void addElement(T elem) {
        T[] tmpArr = Arrays.copyOf(this.elements, this.totalElements + 1);
        tmpArr[this.totalElements++] = elem;
        this.elements = tmpArr;
    }

    public boolean removeElement(T elem) {
        int idx = getElementIndex(elem);
        if (idx == -1) return false;

        this.elements[idx] = this.elements[count() - 1]; // replace the removing item with the last one;
        this.totalElements--;

        // create a copy of the existing array but with one element less (effectively deleting the last element)
        this.elements = Arrays.copyOf(this.elements, this.totalElements);
        return true;
    }

    public int getElementIndex(T elem) {
        for (int i = 0; i < this.elements.length; i++) {
            if (this.elements[i].equals(elem)) {
                return i;
            }
        }
        return -1;
    }

    public boolean contains(T element) {
        T elem = Arrays.stream(this.elements)
                .filter(e -> e.equals(element))
                .findFirst()
                .orElse(null);
        return elem != null;
    }

    public Object[] toArray() {
        return Arrays.stream(this.elements).toArray();
    }

    public boolean isEmpty() {
        return this.totalElements == 0;
    }

    public int count() {
        return this.totalElements;
    }

    public T elementAt(int idx) {
        if (this.elements[idx] != null) {
            return this.elements[idx];
        }
        throw new ArrayIndexOutOfBoundsException(idx);
    }


    public static <T> void copyAll(ResizableArray<? super T> dest, ResizableArray<? extends T> src) {
        int startIdx = dest.elements.length;
        int k = 0;
        dest.elements = Arrays.copyOf((T[]) dest.elements, dest.elements.length + src.elements.length);
        for (int i = startIdx; i < dest.elements.length; i++) {
            dest.elements[i] = src.elements[k];
            k++;
        }
        dest.totalElements = dest.elements.length;
    }
}


class IntegerArray extends ResizableArray<Integer> {
    public double sum() {
        return IntStream
                .range(0, count())
                .mapToDouble(this::elementAt)
                .sum();
    }

    public double mean() {
        return sum() / count();
    }

    public int countNonZero() {
        return (int)IntStream
                .range(0, count())
                .filter(idx -> elementAt(idx) != 0)
                .count();
    }

    public IntegerArray distinct() {
        IntegerArray uniqueArr = new IntegerArray();
        IntStream
                .range(0, count())
                .filter(idx -> !uniqueArr.contains(elementAt(idx)))
                .forEach(idx -> uniqueArr.addElement(super.elementAt(idx)));

        return uniqueArr;
    }

    public IntegerArray increment(int offset) {
        IntegerArray offsetArr = new IntegerArray();
        IntStream
                .range(0, count())
                .forEach(idx -> offsetArr.addElement(super.elementAt(idx) + offset));

        return offsetArr;
    }
}

public class ResizableArrayTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int test = jin.nextInt();
        if (test == 0) { //test ResizableArray on ints
            ResizableArray<Integer> a = new ResizableArray<Integer>();
            System.out.println(a.count());
            int first = jin.nextInt();
            a.addElement(first);
            System.out.println(a.count());
            int last = first;
            while (jin.hasNextInt()) {
                last = jin.nextInt();
                a.addElement(last);
            }
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(a.removeElement(first));
            System.out.println(a.contains(first));
            System.out.println(a.count());
        }
        if (test == 1) { //test ResizableArray on strings
            ResizableArray<String> a = new ResizableArray<String>();
            System.out.println(a.count());
            String first = jin.next();
            a.addElement(first);
            System.out.println(a.count());
            String last = first;
            for (int i = 0; i < 4; ++i) {
                last = jin.next();
                a.addElement(last);
            }
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(a.removeElement(first));
            System.out.println(a.contains(first));
            System.out.println(a.count());
            ResizableArray<String> b = new ResizableArray<String>();
            ResizableArray.copyAll(b, a);
            System.out.println(b.count());
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(b.contains(first));
            System.out.println(b.contains(last));
            ResizableArray.copyAll(b, a);
            System.out.println(b.count());
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(b.contains(first));
            System.out.println(b.contains(last));
            System.out.println(b.removeElement(first));
            System.out.println(b.contains(first));
            System.out.println(b.removeElement(first));
            System.out.println(b.contains(first));

            System.out.println(a.removeElement(first));
            ResizableArray.copyAll(b, a);
            System.out.println(b.count());
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(b.contains(first));
            System.out.println(b.contains(last));
        }
        if (test == 2) { //test IntegerArray
            IntegerArray a = new IntegerArray();
            System.out.println(a.isEmpty());
            while (jin.hasNextInt()) {
                a.addElement(jin.nextInt());
            }
            jin.next();
            System.out.println(a.sum());
            System.out.println(a.mean());
            System.out.println(a.countNonZero());
            System.out.println(a.count());
            IntegerArray b = a.distinct();
            System.out.println(b.sum());
            IntegerArray c = a.increment(5);
            System.out.println(c.sum());
            if (a.sum() > 100)
                ResizableArray.copyAll(a, a);
            else
                ResizableArray.copyAll(a, b);
            System.out.println(a.sum());
            System.out.println(a.removeElement(jin.nextInt()));
            System.out.println(a.sum());
            System.out.println(a.removeElement(jin.nextInt()));
            System.out.println(a.sum());
            System.out.println(a.removeElement(jin.nextInt()));
            System.out.println(a.sum());
            System.out.println(a.contains(jin.nextInt()));
            System.out.println(a.contains(jin.nextInt()));
        }
        if (test == 3) { //test insanely large arrays
            LinkedList<ResizableArray<Integer>> resizable_arrays = new LinkedList<ResizableArray<Integer>>();
            for (int w = 0; w < 500; ++w) {
                ResizableArray<Integer> a = new ResizableArray<Integer>();
                int k = 2000;
                int t = 1000;
                for (int i = 0; i < k; ++i) {
                    a.addElement(i);
                }

                a.removeElement(0);
                for (int i = 0; i < t; ++i) {
                    a.removeElement(k - i - 1);
                }
                resizable_arrays.add(a);
            }
            System.out.println("You implementation finished in less then 3 seconds, well done!");
        }
    }

}

