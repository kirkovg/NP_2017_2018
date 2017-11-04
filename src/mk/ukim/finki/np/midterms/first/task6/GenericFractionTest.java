package mk.ukim.finki.np.midterms.first.task6;

import java.util.Scanner;

class ZeroDenominatorException extends Exception {
    public ZeroDenominatorException() {
        super("Denominator cannot be zero");
    }
}


class GenericFraction<T extends Number, U extends Number> {

    private T numerator;
    private U denominator;

    public GenericFraction(T numerator, U denominator) throws ZeroDenominatorException {
        if (denominator.doubleValue() == 0) {
            throw new ZeroDenominatorException();
        }
        this.numerator = numerator;
        this.denominator = denominator;
    }

    public GenericFraction<Double, Double> add(GenericFraction<? extends Number, ? extends Number> gf) throws ZeroDenominatorException {
        if (this.denominator.doubleValue() == gf.denominator.doubleValue()) {
            return new GenericFraction<>(this.numerator.doubleValue() + gf.numerator.doubleValue(),
                    gf.denominator.doubleValue());
        } else {
            double commonDenominator = this.denominator.doubleValue() * gf.denominator.doubleValue();
            double x = this.numerator.doubleValue() * gf.denominator.doubleValue();
            double y = gf.numerator.doubleValue() * this.denominator.doubleValue();
            return new GenericFraction<>(x + y, commonDenominator);
        }
    }

    public double toDouble() {
        return numerator.doubleValue() / denominator.doubleValue();
    }

    @Override
    public String toString() {
        double gcd = findGCD(numerator.doubleValue(), denominator.doubleValue());
        if (gcd != -1) {
            return String.format("%.2f / %.2f", numerator.doubleValue() / gcd, denominator.doubleValue() / gcd);
        } else {
            return String.format("%.2f / %.2f", numerator.doubleValue(), denominator.doubleValue());
        }
    }

    /**
     * Finds the greatest common divisor of a fraction
     *
     * @param x the numerator
     * @param y the denominator
     * @return the gcd if found, else -1.
     */
    private double findGCD(double x, double y) {
        while (y > 0) {
            double temp = y;
            y = x % y;
            x = temp;
        }
        return x > 1 ? x : -1;
    }
}


public class GenericFractionTest {
    public static void main(String[] args) throws ZeroDenominatorException {
        Scanner scanner = new Scanner(System.in);
        double n1 = scanner.nextDouble();
        double d1 = scanner.nextDouble();
        float n2 = scanner.nextFloat();
        float d2 = scanner.nextFloat();
        int n3 = scanner.nextInt();
        int d3 = scanner.nextInt();
        try {
            GenericFraction<Double, Double> gfDouble = new GenericFraction<Double, Double>(n1, d1);
            GenericFraction<Float, Float> gfFloat = new GenericFraction<Float, Float>(n2, d2);
            GenericFraction<Integer, Integer> gfInt = new GenericFraction<Integer, Integer>(n3, d3);
            System.out.printf("%.2f\n", gfDouble.toDouble());
            System.out.println(gfDouble.add(gfFloat));
            System.out.println(gfInt.add(gfFloat));
            System.out.println(gfDouble.add(gfInt));
            gfInt = new GenericFraction<Integer, Integer>(n3, 0);
        } catch (ZeroDenominatorException e) {
            System.out.println(e.getMessage());
        }

        scanner.close();
    }

}