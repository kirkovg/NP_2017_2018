package mk.ukim.finki.np.lab2.task1;

import java.io.*;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;


class InsufficientElementsException extends Exception {
    public InsufficientElementsException() {
        super("Insufficient number of elements");
    }
}

class InvalidRowNumberException extends Exception {
    public InvalidRowNumberException() {
        super("Invalid row number");
    }
}

class InvalidColumnNumberException extends Exception {
    public InvalidColumnNumberException() {
        super("Invalid column number");
    }
}

class DoubleMatrix {
    private int m;
    private int n;
    private double[][] a;


    public DoubleMatrix(double[] a, int m, int n) throws InsufficientElementsException {
        if (a.length < m * n) throw new InsufficientElementsException();

        if (a.length > m * n) {
            double[] newArray = new double[m * n];
            int k = 0;
            for (int i = a.length - m * n; i < a.length; i++) {
                newArray[k] = a[i];
                k++;
            }

            this.a = new double[m][n];
            k = 0;
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    this.a[i][j] = newArray[k++];
                }
            }
        } else {
            this.a = new double[m][n];
            int k = 0;
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    this.a[i][j] = a[k++];
                }
            }
        }

        this.m = m;
        this.n = n;
    }

    public String getDimensions() {
        return String.format("[%d x %d]", this.m, this.n);
    }

    public int rows() {
        return this.m;
    }

    public int columns() {
        return this.n;
    }

    public double maxElementAtRow(int row) throws InvalidRowNumberException {
        if (row <= 0 || row > m) {
            throw new InvalidRowNumberException();
        }

        double max = -1000000d;
        for (int i = 0; i < this.n; i++) {
            if (this.a[row - 1][i] > max) {
                max = this.a[row - 1][i];
            }
        }
        return max;
    }

    public double maxElementAtColumn(int column) throws InvalidColumnNumberException {
        if (column <= 0 || column > n) {
            throw new InvalidColumnNumberException();
        }

        double max = -1000000d;
        for (int i = 0; i < this.m; i++) {
            if (this.a[i][column - 1] > max) {
                max = this.a[i][column - 1];
            }
        }
        return max;
    }

    public double sum() {
        return Arrays.stream(this.a)
                .mapToDouble(row -> Arrays.stream(row).sum())
                .sum();
    }

    public double[] toSortedArray() {
        double[] arr = new double[this.m * this.n];
        int k = 0;
        for (int i = 0; i < this.m; i++) {
            for (int j = 0; j < this.n; j++) {
                arr[k] = this.a[i][j];
                k++;
            }
        }

        for (int i = 0; i < arr.length; i++) {
            for (int j = 1; j < (arr.length - i); j++) {
                if (arr[j - 1] < arr[j]) {
                    double temp = arr[j - 1];
                    arr[j - 1] = arr[j];
                    arr[j] = temp;
                }

            }
        }
        return arr;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.m; i++) {
            for (int j = 0; j < this.n; j++) {
                if (j == n - 1) {
                    sb.append(String.format("%.2f", this.a[i][j]));
                } else {
                    sb.append(String.format("%.2f\t", this.a[i][j]));
                }
            }
            if (i != m - 1) {
                sb.append("\n");
            }
        }

        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DoubleMatrix that = (DoubleMatrix) o;

        if (m != that.m) return false;
        if (n != that.n) return false;
        return Arrays.deepEquals(a, that.a);
    }

    @Override
    public int hashCode() {
        int result = m;
        result = 31 * result + n;
        result = 31 * result + Arrays.deepHashCode(a);
        return result;
    }
}

class MatrixReader {
    public static DoubleMatrix read(InputStream inputStream) throws IOException, InsufficientElementsException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String[] howMuch = bufferedReader.readLine().split("\\s+");
        int m = parseInt(howMuch[0]);
        int n = parseInt(howMuch[1]);
        double[] array = new double[m * n];
        int k = 0;

        for (int i = 0; i < m; i++) {
            String[] row = bufferedReader.readLine().split("\\s+");
            for (int j = 0; j < row.length; j++) {
                array[k] = parseDouble(row[j]);
                k++;
            }
        }

        return new DoubleMatrix(array, m, n);
    }


}

public class DoubleMatrixTester {

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in).useLocale(Locale.US);

        int tests = scanner.nextInt();
        DoubleMatrix fm = null;

        double[] info = null;

        DecimalFormat format = new DecimalFormat("0.00");

        for (int t = 0; t < tests; t++) {

            String operation = scanner.next();

            switch (operation) {
                case "READ": {
                    int N = scanner.nextInt();
                    int R = scanner.nextInt();
                    int C = scanner.nextInt();

                    double[] f = new double[N];

                    for (int i = 0; i < f.length; i++)
                        f[i] = scanner.nextDouble();

                    try {
                        fm = new DoubleMatrix(f, R, C);
                        info = Arrays.copyOf(f, f.length);

                    } catch (InsufficientElementsException e) {
                        System.out.println("Exception caught: " + e.getMessage());
                    }

                    break;
                }

                case "INPUT_TEST": {
                    int R = scanner.nextInt();
                    int C = scanner.nextInt();

                    StringBuilder sb = new StringBuilder();

                    sb.append(R + " " + C + "\n");

                    scanner.nextLine();

                    for (int i = 0; i < R; i++)
                        sb.append(scanner.nextLine() + "\n");

                    fm = MatrixReader.read(new ByteArrayInputStream(sb
                            .toString().getBytes()));

                    info = new double[R * C];
                    Scanner tempScanner = new Scanner(new ByteArrayInputStream(sb
                            .toString().getBytes()));
                    tempScanner.nextDouble();
                    tempScanner.nextDouble();
                    for (int z = 0; z < R * C; z++) {
                        info[z] = tempScanner.nextDouble();
                    }

                    tempScanner.close();

                    break;
                }

                case "PRINT": {
                    System.out.println(fm.toString());
                    break;
                }

                case "DIMENSION": {
                    System.out.println("Dimensions: " + fm.getDimensions());
                    break;
                }

                case "COUNT_ROWS": {
                    System.out.println("Rows: " + fm.rows());
                    break;
                }

                case "COUNT_COLUMNS": {
                    System.out.println("Columns: " + fm.columns());
                    break;
                }

                case "MAX_IN_ROW": {
                    int row = scanner.nextInt();
                    try {
                        System.out.println("Max in row: "
                                + format.format(fm.maxElementAtRow(row)));
                    } catch (InvalidRowNumberException e) {
                        System.out.println("Exception caught: " + e.getMessage());
                    }
                    break;
                }

                case "MAX_IN_COLUMN": {
                    int col = scanner.nextInt();
                    try {
                        System.out.println("Max in column: "
                                + format.format(fm.maxElementAtColumn(col)));
                    } catch (InvalidColumnNumberException e) {
                        System.out.println("Exception caught: " + e.getMessage());
                    }
                    break;
                }

                case "SUM": {
                    System.out.println("Sum: " + format.format(fm.sum()));
                    break;
                }

                case "CHECK_EQUALS": {
                    int val = scanner.nextInt();

                    int maxOps = val % 7;

                    for (int z = 0; z < maxOps; z++) {
                        double work[] = Arrays.copyOf(info, info.length);

                        int e1 = (31 * z + 7 * val + 3 * maxOps) % info.length;
                        int e2 = (17 * z + 3 * val + 7 * maxOps) % info.length;

                        if (e1 > e2) {
                            double temp = work[e1];
                            work[e1] = work[e2];
                            work[e2] = temp;
                        }

                        DoubleMatrix f1 = fm;
                        DoubleMatrix f2 = new DoubleMatrix(work, fm.rows(),
                                fm.columns());
                        System.out
                                .println("Equals check 1: "
                                        + f1.equals(f2)
                                        + " "
                                        + f2.equals(f1)
                                        + " "
                                        + (f1.hashCode() == f2.hashCode()&&f1
                                        .equals(f2)));
                    }

                    if (maxOps % 2 == 0) {
                        DoubleMatrix f1 = fm;
                        DoubleMatrix f2 = new DoubleMatrix(new double[]{3.0, 5.0,
                                7.5}, 1, 1);

                        System.out
                                .println("Equals check 2: "
                                        + f1.equals(f2)
                                        + " "
                                        + f2.equals(f1)
                                        + " "
                                        + (f1.hashCode() == f2.hashCode()&&f1
                                        .equals(f2)));
                    }

                    break;
                }

                case "SORTED_ARRAY": {
                    double[] arr = fm.toSortedArray();

                    String arrayString = "[";

                    if (arr.length > 0)
                        arrayString += format.format(arr[0]) + "";

                    for (int i = 1; i < arr.length; i++)
                        arrayString += ", " + format.format(arr[i]);

                    arrayString += "]";

                    System.out.println("Sorted array: " + arrayString);
                    break;
                }

            }

        }

        scanner.close();
    }
}
