package mk.ukim.finki.np.midterms.first.task9;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

interface Scalable {
    void scale(float scaleFactor);
}

interface Stackable {
    float weight();
}

abstract class Shape implements Stackable, Scalable {
    protected String id;
    protected Color color;

    public Shape(String id, Color color) {
        this.id = id;
        this.color = color;
    }
}

class Circle extends Shape {
    private float radius;

    public Circle(String id, Color color, float radius) {
        super(id, color);
        this.radius = radius;
    }

    @Override
    public void scale(float scaleFactor) {
        this.radius *= scaleFactor;
    }

    @Override
    public float weight() {
        return (float) (Math.pow(radius, 2) * Math.PI);
    }

    @Override
    public String toString() {
        return String.format("C: %-5s%-10s%10.2f", id, color, weight());
    }
}

class Rectangle extends Shape {
    private float width;
    private float height;

    public Rectangle(String id, Color color, float width, float height) {
        super(id, color);
        this.width = width;
        this.height = height;
    }

    @Override
    public void scale(float scaleFactor) {
        this.width *= scaleFactor;
        this.height *= scaleFactor;
    }

    @Override
    public float weight() {
        return width * height;
    }

    @Override
    public String toString() {
        return String.format("R: %-5s%-10s%10.2f", id, color, weight());
    }
}

class Canvas {
    private List<Shape> shapes;
    private List<Float> weights;

    public Canvas() {
        this.shapes = new ArrayList<>();
        this.weights = new ArrayList<>();
    }

    public void add(String id, Color color, float radius) {
        Circle circleToAdd = new Circle(id, color, radius);
        addSorted(circleToAdd);
    }

    public void add(String id, Color color, float width, float height) {
        Rectangle rectangleToAdd = new Rectangle(id, color, width, height);
        addSorted(rectangleToAdd);
    }

    public void scale(String id, float scaleFactor) {
        Shape shape = shapes.stream()
                .filter(s -> s.id.equals(id))
                .findFirst().get();

        Float weight = weights.stream()
                .filter(w -> w == shape.weight())
                .findFirst().get();

        weights.remove(weight);
        shapes.remove(shape);

        shape.scale(scaleFactor);
        addSorted(shape);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        shapes.forEach(shape -> sb.append(shape.toString()).append("\n"));
        return sb.toString();
    }

    private void addSorted(Shape shape) {
        int i;
        for (i = 0; i < weights.size(); i++) {
            if (weights.get(i) < shape.weight()) {
                break;
            }
        }
        shapes.add(i, shape);
        weights.add(i, shape.weight());
    }
}

enum Color {
    RED, GREEN, BLUE
}

public class ShapesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Canvas canvas = new Canvas();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(" ");
            int type = Integer.parseInt(parts[0]);
            String id = parts[1];
            if (type == 1) {
                Color color = Color.valueOf(parts[2]);
                float radius = Float.parseFloat(parts[3]);
                canvas.add(id, color, radius);
            } else if (type == 2) {
                Color color = Color.valueOf(parts[2]);
                float width = Float.parseFloat(parts[3]);
                float height = Float.parseFloat(parts[4]);
                canvas.add(id, color, width, height);
            } else if (type == 3) {
                float scaleFactor = Float.parseFloat(parts[2]);
                System.out.println("ORIGNAL:");
                System.out.print(canvas);
                canvas.scale(id, scaleFactor);
                System.out.printf("AFTER SCALING: %s %.2f\n", id, scaleFactor);
                System.out.print(canvas);
            }

        }
    }
}
