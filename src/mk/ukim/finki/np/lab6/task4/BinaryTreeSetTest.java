package mk.ukim.finki.np.lab6.task4;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeSet;

class Node<T> {
    T element;
    Node<T> prev;
    Node<T> succ;

    public Node(T element) {
        this.element = element;
        prev = null;
        succ = null;
    }
}

class BinaryTreeSet<T extends Comparable<T>> {

    private Node<T> root;

    public BinaryTreeSet() {
        root = null;
    }

    public void addElement(T element) {
        root = add(element, root);
    }

    public boolean contains(T element) {
        if (find(element) != null) {
            return true;
        }
        return false;
    }

    public boolean removeElement(T element) {
        if (contains(element)) {
            root = remove(element, root);
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        if (root == null) {
            return "EMPTY";
        }
        return inorder(root, new StringBuilder());
    }

    private Node<T> add(T element, Node<T> node) {
        if (node == null) {
            return new Node<>(element);
        }
        if (element.compareTo(node.element) == 0) {
            node.element = element;
        } else {
            if (element.compareTo(node.element) < 0) {
                node.prev = add(element, node.prev);
            } else {
                node.succ = add(element, node.succ);
            }
        }
        return node;
    }

    private Node<T> find(T element) {
        return find(element, root);
    }

    private String inorder(Node<T> node, StringBuilder sb) {
        if (node != null) {
            inorder(node.prev, sb);
            sb.append(node.element).append(" ");
            inorder(node.succ, sb);
        }
        return sb.toString().trim();
    }

    private T getRightmost(Node<T> node) {
        assert (node != null);
        Node<T> succ = node.succ;
        if (succ == null) {
            return node.element;
        } else {
            return getRightmost(succ);
        }
    }


    private Node<T> remove(T element, Node<T> node) {
        if (node == null) {
            return null;
        }
        if (element.compareTo(node.element) == 0) {
            if (node.prev == null) {
                return node.succ;
            } else if (node.succ == null) {
                return node.prev;
            } else {
                node.element = getRightmost(node.prev);
                node.prev = remove(node.element, node.prev);
            }
        } else {
            if (element.compareTo(node.element) < 0) {
                node.prev = remove(element, node.prev);
            } else {
                node.succ = remove(element, node.succ);
            }
        }
        return node;
    }


    private Node<T> find(T element, Node<T> node) {
        if (node == null)
            return null;

        if (element.compareTo(node.element) < 0) {
            return find(element, node.prev);
        } else if (element.compareTo(node.element) > 0) {
            return find(element, node.succ);
        } else {
            return node;
        }
    }
}

public class BinaryTreeSetTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int t = jin.nextInt();
        if (t == 0) {
            BinaryTreeSet<Integer> ts = new BinaryTreeSet<Integer>();
            while (jin.hasNextInt()) {
                ts.addElement(jin.nextInt());
            }
            System.out.println(ts);
        }
        if (t == 1) {
            BinaryTreeSet<String> ts = new BinaryTreeSet<String>();
            while (true) {
                String next = jin.next();
                if (next.equals("stop")) break;
                ts.addElement(next);
            }
            System.out.println(ts);
        }
        if (t == 2) {
            BinaryTreeSet<Long> ts = new BinaryTreeSet<Long>();
            while (jin.hasNextLong()) {
                ts.addElement(jin.nextLong());
            }
            jin.next();
            System.out.println(ts);
            while (jin.hasNextLong()) {
                System.out.println(ts.contains(jin.nextLong()));
            }
            System.out.println(ts);
        }
        if (t == 3) {
            BinaryTreeSet<String> ts = new BinaryTreeSet<String>();
            int counter = 0;
            while (true) {
                if (counter % 20 == 0) System.out.println(ts);
                ++counter;
                String next = jin.next();
                if (next.equals("stop")) break;
                if (next.equals("add")) {
                    ts.addElement(jin.next());
                }
                if (next.equals("remove")) {
                    ts.removeElement(jin.next());
                }
                if (next.equals("query")) {
                    System.out.println(ts.contains(jin.next()));
                }
            }
            System.out.println(ts);
        }
        if (t == 4) {
            BinaryTreeSet<Long> ts = new BinaryTreeSet<Long>();
            TreeSet<Long> control_set = new TreeSet<Long>();
            ArrayList<Long> all = new ArrayList<Long>();
            all.add(5L);
            int n = jin.nextInt();
            boolean exact = true;
            for (int i = 0; exact && i < n; ++i) {
                if (Math.random() < 0.4) {
                    if (Math.random() < 0.6) {
                        long to_add = (long) (Math.random() * 98746516548964156L);
                        ts.addElement(to_add);
                        control_set.add(to_add);
                        all.add(to_add);
                    } else {
                        int add_idx = (int) (Math.random() * all.size());
                        long to_add = all.get(add_idx);
                        ts.addElement(to_add);
                        control_set.add(to_add);
                    }
                } else {
                    if (Math.random() < 0.4) {
                        if (Math.random() < 0.1) {
                            long to_remove = (long) (Math.random() * 98746516548964156L);
                            ts.removeElement(to_remove);
                            control_set.remove(to_remove);
                        } else {
                            int remove_idx = (int) (Math.random() * all.size());
                            long to_remove = all.get(remove_idx);
                            ts.removeElement(to_remove);
                            control_set.remove(to_remove);
                        }
                    } else {
                        if (Math.random() < 0.3) {
                            long to_query = (long) (Math.random() * 98746516548964156L);
                            exact &= ts.contains(to_query) == control_set.contains(to_query);
                        } else {
                            int query_idx = (int) (Math.random() * all.size());
                            long to_query = all.get(query_idx);
                            exact &= ts.contains(to_query) == control_set.contains(to_query);
                        }
                    }
                }
            }
            System.out.println(exact);
        }
    }

}

