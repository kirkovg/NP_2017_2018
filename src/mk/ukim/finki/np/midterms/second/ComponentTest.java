package mk.ukim.finki.np.midterms.second;

import java.util.*;

public class ComponentTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();
        Window window = new Window(name);
        Component prev = null;
        while (true) {
            try {
                int what = scanner.nextInt();
                scanner.nextLine();
                if (what == 0) {
                    int position = scanner.nextInt();
                    window.addComponent(position, prev);
                } else if (what == 1) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev = component;
                } else if (what == 2) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev.addComponent(component);
                    prev = component;
                } else if (what == 3) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev.addComponent(component);
                } else if(what == 4) {
                    break;
                }

            } catch (InvalidPositionException e) {
                System.out.println(e.getMessage());
            }
            scanner.nextLine();
        }

        System.out.println("=== ORIGINAL WINDOW ===");
        System.out.println(window);
        int weight = scanner.nextInt();
        scanner.nextLine();
        String color = scanner.nextLine();
        window.changeColor(weight, color);
        System.out.println(String.format("=== CHANGED COLOR (%d, %s) ===", weight, color));
        System.out.println(window);
        int pos1 = scanner.nextInt();
        int pos2 = scanner.nextInt();
        System.out.println(String.format("=== SWITCHED COMPONENTS %d <-> %d ===", pos1, pos2));
        window.switchComponents(pos1, pos2);
        System.out.println(window);
    }
}

class Component implements Comparable<Component>{
    private String color;
    private int weight;
    private TreeSet<Component> childComponents;

    public Component(String color, int weight) {
        this.color = color;
        this.weight = weight;
        this.childComponents = new TreeSet<>();
    }

    public String getColor() {
        return color;
    }

    public int getWeight() {
        return weight;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public TreeSet<Component> getChildComponents() {
        return childComponents;
    }

    public void addComponent(Component cmp) {
        childComponents.add(cmp);
    }

    @Override
    public int compareTo(Component o) {
        int cmpWeight = Integer.compare(this.weight, o.weight);
        if (cmpWeight == 0) {
            return this.color.compareTo(o.color);
        }
        return cmpWeight;
    }

    @Override
    public String toString() {
        return String.format("%d:%s", weight, color);
    }
}

class Window {
    private String name;
    private TreeMap<Integer, Component> components;

    public Window(String name) {
        this.name = name;
        this.components = new TreeMap<>();
    }

    public void addComponent(int position, Component component) throws InvalidPositionException {
        if (components.containsKey(position))
            throw new InvalidPositionException(position);
        components.put(position, component);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("WINDOW ").append(name).append("\n");
        components.keySet().forEach(position -> {
            Component cmpt = components.get(position);
            sb.append(String.format("%d:", position));
            createStringResult(cmpt, sb, 0);
        });
        return sb.toString();
    }

    private void createStringResult(Component component, StringBuilder sb, int depth) {
        sb.append(String.format("%s%s\n", depthAdder(depth), component.toString()));
        component.getChildComponents().forEach(cmpt -> createStringResult(cmpt, sb, depth + 1));
    }


    public void changeColor(int weight, String color) {
        components.values().forEach(cmpt -> changeColorComponent(weight, color, cmpt));
    }

    private void changeColorComponent(int weight, String color, Component component) {
        if (component.getWeight() < weight) {
            component.setColor(color);
        }
        component.getChildComponents().forEach(cmpt -> changeColorComponent(weight, color, cmpt));
    }

    public void switchComponents(int pos1, int pos2) {
        Component cmpt1 = components.get(pos1);
        Component cmpt2 = components.get(pos2);

        components.put(pos1, cmpt2);
        components.put(pos2, cmpt1);
    }

    private String depthAdder(int d) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < d; ++i) {
            sb.append("---");
        }
        return sb.toString();
    }
}

class InvalidPositionException extends Exception {
    public InvalidPositionException(int position) {
        super(String.format("Invalid position %d, already taken!", position));
    }
}
