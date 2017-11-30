package mk.ukim.finki.np.lab6.task2;

import java.util.*;


class Node<T> {
    private T data;
    private Node<T> next;
    private Node<T> prev;

    public Node(T data, Node<T> next, Node<T> prev) {
        this.data = data;
        this.next = next;
        this.prev = prev;
    }

    public T getData() {
        return data;
    }

    public Node<T> getNext() {
        return next;
    }

    public Node<T> getPrev() {
        return prev;
    }
}

class SortedLinkedList<T extends Comparable<T>> {
    private Node<T> first;
    private int size;

    public SortedLinkedList() {
        first = null;
        size = 0;
    }

    public void add(T element) {

    }

    public boolean contains(T element) {
        Node<T> tmp = first;
        while (tmp.getNext() != null) {
            if (tmp.getData().compareTo(element) == 0) {
                return true;
            }
            tmp = tmp.getNext();
        }
        return false;
    }

    public boolean isEmpty() {
        return first == null;
    }

    public int size() {
        return size;
    }

    public ArrayList<T> toArrayList() {
        ArrayList<T> list = new ArrayList<>();
        Node<T> tmp = first;
        while (tmp.getNext() != null) {
            list.add(tmp.getData());
            tmp = tmp.getNext();
        }
        return list;
    }

    public void addAll(SortedLinkedList<? extends T> a) {
        return;
    }

    public boolean containsAll(SortedLinkedList<? extends T> a) {
        return false;
    }

    public void remove(T element) {}

}



public class SortedLinkedListTest {
    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        System.out.println("Test#"+k);
        System.out.print("testing SortedLinkedList::toArrayList():ArrayList<T> ,");
        if ( k == 0 ) {
            System.out.println(" SortedLinkedList::add(T), SortedLinkedList::isEmpty():boolean , SortedLinkedList::remove(T):boolean , SortedLinkedList::size():int , T is Integer");

            SortedLinkedList<Integer> list = new SortedLinkedList<Integer>();
            System.out.println("List is empty? "+list.isEmpty());
            System.out.println("Adding elements:");
            boolean flag = false;
            while ( jin.hasNextInt() ) {
                System.out.print(flag?" ":"");
                int i = jin.nextInt();
                list.add(i);
                System.out.print(i);
                flag = true;
            }
            System.out.println();
            System.out.println("List size? "+list.size());
            jin.next();
            flag = false;
            System.out.println("Removing elements:");
            while ( jin.hasNextInt() ) {
                System.out.print(flag?" ":"");
                int i = jin.nextInt();
                list.remove(i);
                System.out.print(i);
                flag = true;
            }System.out.println();
            System.out.println("List size? "+list.size());
            System.out.println("Final list: "+list.toArrayList());
        }
        if ( k == 1 ) {
            System.out.println(" SortedLinkedList::add(T) , T is Integer");
            System.out.println("Adding elements:");
            SortedLinkedList<Integer> list = new SortedLinkedList<Integer>();
            boolean flag = false;
            while ( jin.hasNextInt() ) {
                System.out.print(flag?" ":"");
                int i = jin.nextInt();
                list.add(i);
                System.out.print(i);
                flag = true;
            }
            System.out.println();
            System.out.print("Final list: ");
            System.out.println(list.toArrayList());
        }
        if ( k == 2 ) {
            System.out.println(" SortedLinkedList::add(T) , SortedLinkedList::addAll(SortedLinkedList<? etends T>) , T is Integer");

            int num_testcases = jin.nextInt();
            for ( int w = 0 ; w < num_testcases ; ++w ) {
                System.out.println("Subtest #"+(w+1));
                SortedLinkedList<Integer> list = new SortedLinkedList<Integer>();
                while ( jin.hasNextInt() ) {
                    list.add(jin.nextInt());
                }
                SortedLinkedList<Integer> query = new SortedLinkedList<Integer>();
                jin.next();
                while ( jin.hasNextInt() ) {
                    query.add(jin.nextInt());
                }
                System.out.println("List a="+list.toArrayList());
                System.out.println("List b="+query.toArrayList());
                list.addAll(query);
                System.out.println("Add all elements from b to a");
                System.out.println("List a="+list.toArrayList());
                jin.next();
            }
        }
        if ( k == 3 ) {
            System.out.println(" SortedLinkedList::add(T) , SortedLinkedList::containsAll(SortedLinkedList<? etends T>) , T is Integer");
            int num_testcases = jin.nextInt();
            for ( int w = 0 ; w < num_testcases ; ++w ) {
                System.out.println("Subtest #"+(w+1));
                SortedLinkedList<Integer> list = new SortedLinkedList<Integer>();
                while ( jin.hasNextInt() ) {
                    list.add(jin.nextInt());
                }
                SortedLinkedList<Integer> query = new SortedLinkedList<Integer>();
                jin.next();
                while ( jin.hasNextInt() ) {
                    query.add(jin.nextInt());
                }
                System.out.println("List a="+list.toArrayList());
                System.out.println("List b="+query.toArrayList());
                System.out.println("List a contains all elements in list b? "+list.containsAll(query));
                jin.next();
            }
        }
        if ( k == 4 ) {
            System.out.println(" SortedLinkedList::add(T) , SortedLinkedList::remove(T):boolean , SortedLinkedList::contains(T) , T is String");
            SortedLinkedList<String> list = new SortedLinkedList<String>();
            TreeSet<String> control_list = new TreeSet<String>();
            ArrayList<String> all = new ArrayList<String>();
            all.add("Sample");
            boolean same = true;
            for ( int i = 0 ; i < 1000 ; ++i ) {
                double rand = Math.random();
                if ( rand > 0.3 ) { //addelement
                    String srand = randomString();
                    if ( Math.random() < 0.1 ) {
                        srand = all.get((int)(Math.random()*all.size()));
                    }
                    control_list.add(srand);list.add(srand);
                }
                if ( rand >= 0.3&&rand < 0.8 ) {//query
                    String srand = randomString();
                    if ( Math.random() < 0.6 ) {
                        srand = all.get((int)(Math.random()*all.size()));
                    }
                    same &= control_list.contains(srand)==list.contains(srand);
                }
                if ( rand >= 0.8 ) {//remove
                    String srand = randomString();
                    if ( Math.random() < 0.8 ) {
                        srand = all.get((int)(Math.random()*all.size()));
                    }
                    control_list.remove(srand);list.remove(srand);
                }
            }
            System.out.println("Your list outputs compared to the built in java structure were the same? "+same);

        }
        if ( k == 5 ) {
            System.out.println(" SortedLinkedList::add(T) , SortedLinkedList::remove(T):boolean , T is Long");
            int n = jin.nextInt();
            SortedLinkedList<Long> list = new SortedLinkedList<Long>();
            ArrayList<Long> all = new ArrayList<Long>();
            all.add(684165189745L);
            for ( int i = 0 ; i < n ; ++i ) {
                double rand = Math.random();
                if ( rand < 0.7 ) { //addelement
                    Long srand = (long) (Math.random()*45668948941984L);
                    if ( Math.random() < 0.1 ) {
                        srand = all.get((int)(Math.random()*all.size()));
                    }
                    list.add(srand);
                }
                if ( rand >= 0.7 ) {
                    Long srand = (long) (Math.random()*45668948941984L);
                    if ( Math.random() < 0.1 ) {
                        srand = all.get((int)(Math.random()*all.size()));
                    }
                    list.remove(srand);
                }
            }
            System.out.println("Your program was really fast. You are a great developer!");
        }
    }

    private static String randomString() {
        byte buf[] = new byte[(int)(Math.random()*10)+1];
        new Random().nextBytes(buf);
        return new String(buf);
    }

}

