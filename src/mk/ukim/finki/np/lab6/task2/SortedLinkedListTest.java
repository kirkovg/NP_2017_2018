package mk.ukim.finki.np.lab6.task2;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.TreeSet;

// FUCK THIS TASK
class SortedLinkedList<T extends Comparable<T>> {
    private static class Node<T extends Comparable<T>> {

        private T element;
        private Node<T> prev, succ;

        public Node(T element, Node<T> prev, Node<T> succ) {
            this.element = element;
            this.prev = prev;
            this.succ = succ;
        }
    }

    private Node<T> first, last;

    public SortedLinkedList() {
        first = null;
        last = null;
    }

    public void add(T element) {
        if (isEmpty()) {
            insertFirst(element);
        } else {
            if (element.compareTo(last.element) > 0) {
                insertLast(element);
            } else if (element.compareTo(last.element) < 0) {
                addOrdered(element);
            }
        }
    }

    public void addOrdered(T element) {
        Node<T> tmp = last;
        while (tmp != null && element.compareTo(tmp.element) < 0) {
            tmp = tmp.prev;
        }
        if (tmp != null) {
            if (element.compareTo(tmp.element) > 0) {
                insertAfter(element, tmp);
            }
        } else {
            insertFirst(element);
        }
    }

    public boolean contains(T element) {
        return find(element) != null;
    }

    public Node<T> find(T element) {
        if (isEmpty()) {
            return null;
        }

        Node<T> tmp = first;
        while (tmp != null && tmp.element.compareTo(element) < 0) {
            tmp = tmp.succ;
        }
        if (tmp != null) {
            if (tmp.element.compareTo(element) == 0) {
                return tmp;
            }
        }
        return null;
    }

    public boolean remove(T element) {
        Node<T> node = find(element);
        if (node != null) {
            delete(node);
            return true;
        }
        return false;
    }

    public boolean isEmpty() {
        return last == null;
    }

    public int size() {
        if (isEmpty()) {
            return 0;
        }
        int result = 1;
        Node<T> tmp = first;
        while (tmp.succ != null) {
            tmp = tmp.succ;
            result++;
        }
        return result;
    }

    public ArrayList<T> toArrayList() {
        ArrayList<T> result = new ArrayList<T>();
        if (isEmpty()) {
            return null;
        }
        Node<T> tmp = first;
        while (tmp != null) {
            result.add(tmp.element);
            tmp = tmp.succ;
        }
        return result;
    }

    public void addAll(SortedLinkedList<? extends T> a) {
        if (!a.isEmpty()) {
            if (isEmpty()) {
                Node<T> tmp = (Node<T>) a.first;
                while (tmp != null) {
                    insertLast(tmp.element);
                    tmp = tmp.succ;
                }
            } else {
                Node<T> tmp = first;
                Node<T> aNode = (Node<T>) a.first;
                while (aNode != null) {
                    while (tmp != null && tmp.element.compareTo(aNode.element) < 0) {
                        tmp = tmp.succ;
                    }
                    if (tmp != null) {
                        if (tmp.element.compareTo(aNode.element) > 0) {
                            insertBefore(aNode.element, tmp);
                        }
                    } else {
                        insertLast(aNode.element);
                    }
                    aNode = aNode.succ;
                }
            }
        }
    }

    public boolean containsAll(SortedLinkedList<? extends T> a) {
        if (isEmpty()) {
            return false;
        }
        if (a.isEmpty()) {
            return true;
        }

        Node<T> tmp = first;
        Node<T> aNode = (Node<T>) a.first;
        while (aNode != null) {
            while (tmp != null && tmp.element.compareTo(aNode.element) < 0) {
                tmp = tmp.succ;
            }
            if (tmp != null) {
                if (tmp.element.compareTo(aNode.element) != 0) {
                    return false;
                }
            } else {
                return false;
            }
            aNode = aNode.succ;
        }
        return true;
    }

    public void insertFirst(T o) {
        Node<T> ins = new Node<T>(o, null, first);
        if (first == null)
            last = ins;
        else
            first.prev = ins;
        first = ins;
    }

    public void insertLast(T o) {
        if (first == null)
            insertFirst(o);
        else {
            Node<T> ins = new Node<T>(o, last, null);
            last.succ = ins;
            last = ins;
        }
    }

    public void insertAfter(T o, Node<T> after) {
        if (after == last) {
            insertLast(o);
            return;

        }
        Node<T> ins = new Node<T>(o, after, after.succ);
        after.succ.prev = ins;
        after.succ = ins;
    }

    public void insertBefore(T o, Node<T> before) {
        if (before == first) {
            insertFirst(o);
            return;
        }
        Node<T> ins = new Node<T>(o, before.prev, before);
        before.prev.succ = ins;
        before.prev = ins;
    }

    public T deleteFirst() {
        if (first != null) {
            Node<T> tmp = first;
            first = first.succ;
            if (first != null)
                first.prev = null;
            if (first == null)
                last = null;
            return tmp.element;
        } else

            return null;
    }

    public T deleteLast() {
        if (first != null) {
            if (first.succ == null)
                return deleteFirst();
            else {
                Node<T> tmp = last;
                last = last.prev;
                last.succ = null;
                return tmp.element;
            }
        }
        return null;

    }

    public T delete(Node<T> node) {
        if (node == first) {
            deleteFirst();
            return node.element;
        }

        if (node == last) {
            deleteLast();
            return node.element;
        }
        node.prev.succ = node.succ;
        node.succ.prev = node.prev;
        return node.element;
    }

    public Node<T> getFirst() {
        return first;
    }

}

public class SortedLinkedListTest {
    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        System.out.println("Test#" + k);
        System.out.print("testing SortedLinkedList::toArrayList():ArrayList<T> ,");
        if (k == 0) {
            System.out.println(
                    " SortedLinkedList::add(T), SortedLinkedList::isEmpty():boolean , SortedLinkedList::remove(T):boolean , SortedLinkedList::size():int , T is Integer");

            SortedLinkedList<Integer> list = new SortedLinkedList<Integer>();
            System.out.println("List is empty? " + list.isEmpty());
            System.out.println("Adding elements:");
            boolean flag = false;
            while (jin.hasNextInt()) {
                System.out.print(flag ? " " : "");
                int i = jin.nextInt();
                list.add(i);
                System.out.print(i);
                flag = true;
            }
            System.out.println();
            System.out.println("List size? " + list.size());
            jin.next();
            flag = false;
            System.out.println("Removing elements:");
            while (jin.hasNextInt()) {
                System.out.print(flag ? " " : "");
                int i = jin.nextInt();
                list.remove(i);
                System.out.print(i);
                flag = true;
            }
            System.out.println();
            System.out.println("List size? " + list.size());
            System.out.println("Final list: " + list.toArrayList());
        }
        if (k == 1) {
            System.out.println(" SortedLinkedList::add(T) , T is Integer");
            System.out.println("Adding elements:");
            SortedLinkedList<Integer> list = new SortedLinkedList<Integer>();
            boolean flag = false;
            while (jin.hasNextInt()) {
                System.out.print(flag ? " " : "");
                int i = jin.nextInt();
                list.add(i);
                System.out.print(i);
                flag = true;
            }
            System.out.println();
            System.out.print("Final list: ");
            System.out.println(list.toArrayList());
        }
        if (k == 2) {
            System.out.println(
                    " SortedLinkedList::add(T) , SortedLinkedList::addAll(SortedLinkedList<? etends T>) , T is Integer");

            int num_testcases = jin.nextInt();
            for (int w = 0; w < num_testcases; ++w) {
                System.out.println("Subtest #" + (w + 1));
                SortedLinkedList<Integer> list = new SortedLinkedList<Integer>();
                while (jin.hasNextInt()) {
                    list.add(jin.nextInt());
                }
                SortedLinkedList<Integer> query = new SortedLinkedList<Integer>();
                jin.next();
                while (jin.hasNextInt()) {
                    query.add(jin.nextInt());
                }
                System.out.println("List a=" + list.toArrayList());
                System.out.println("List b=" + query.toArrayList());
                list.addAll(query);
                System.out.println("Add all elements from b to a");
                System.out.println("List a=" + list.toArrayList());
                jin.next();
            }
        }
        if (k == 3) {
            System.out.println(
                    " SortedLinkedList::add(T) , SortedLinkedList::containsAll(SortedLinkedList<? etends T>) , T is Integer");
            int num_testcases = jin.nextInt();
            for (int w = 0; w < num_testcases; ++w) {
                System.out.println("Subtest #" + (w + 1));
                SortedLinkedList<Integer> list = new SortedLinkedList<Integer>();
                while (jin.hasNextInt()) {
                    list.add(jin.nextInt());
                }
                SortedLinkedList<Integer> query = new SortedLinkedList<Integer>();
                jin.next();
                while (jin.hasNextInt()) {
                    query.add(jin.nextInt());
                }
                System.out.println("List a=" + list.toArrayList());
                System.out.println("List b=" + query.toArrayList());
                System.out.println("List a contains all elements in list b? " + list.containsAll(query));
                jin.next();
            }
        }
        if (k == 4) {
            System.out.println(
                    " SortedLinkedList::add(T) , SortedLinkedList::remove(T):boolean , SortedLinkedList::contains(T) , T is String");
            SortedLinkedList<String> list = new SortedLinkedList<String>();
            TreeSet<String> control_list = new TreeSet<String>();
            ArrayList<String> all = new ArrayList<String>();
            all.add("Sample");
            boolean same = true;
            for (int i = 0; i < 1000; ++i) {
                double rand = Math.random();
                if (rand > 0.3) { // addelement
                    String srand = randomString();
                    if (Math.random() < 0.1) {
                        srand = all.get((int) (Math.random() * all.size()));
                    }
                    control_list.add(srand);
                    list.add(srand);
                }
                if (rand >= 0.3 && rand < 0.8) {// query
                    String srand = randomString();
                    if (Math.random() < 0.6) {
                        srand = all.get((int) (Math.random() * all.size()));
                    }
                    same &= control_list.contains(srand) == list.contains(srand);
                }
                if (rand >= 0.8) {// remove
                    String srand = randomString();
                    if (Math.random() < 0.8) {
                        srand = all.get((int) (Math.random() * all.size()));
                    }
                    control_list.remove(srand);
                    list.remove(srand);
                }
            }
            System.out.println("Your list outputs compared to the built in java structure were the same? " + same);

        }
        if (k == 5) {
            System.out.println(" SortedLinkedList::add(T) , SortedLinkedList::remove(T):boolean , T is Long");
            int n = jin.nextInt();
            SortedLinkedList<Long> list = new SortedLinkedList<Long>();
            ArrayList<Long> all = new ArrayList<Long>();
            all.add(684165189745L);
            for (int i = 0; i < n; ++i) {
                double rand = Math.random();
                if (rand < 0.7) { // addelement
                    Long srand = (long) (Math.random() * 45668948941984L);
                    if (Math.random() < 0.1) {
                        srand = all.get((int) (Math.random() * all.size()));
                    }
                    list.add(srand);
                }
                if (rand >= 0.7) {
                    Long srand = (long) (Math.random() * 45668948941984L);
                    if (Math.random() < 0.1) {
                        srand = all.get((int) (Math.random() * all.size()));
                    }
                    list.remove(srand);
                }
            }
            System.out.println("Your program was really fast. You are a great developer!");
        }
    }

    private static String randomString() {
        byte buf[] = new byte[(int) (Math.random() * 10) + 1];
        new Random().nextBytes(buf);
        return new String(buf);
    }

}