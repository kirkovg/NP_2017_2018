package mk.ukim.finki.np.lab6.task3;

import java.util.Scanner;

class Matrix<T> {
    private T[][] matrix;
    private int m;
    private int n;

    public Matrix(int numRows, int numCols) {
        this.n = numRows;
        this.m = numCols;
        matrix = (T[][]) new Object[numRows][numCols];
    }

    public int getNumRows() {
        return this.n;
    }

    public int getNumColumns() {
        return m;
    }

    public T getElementAt(int row, int col) {
        return matrix[row][col];
    }

    public void setElementAt(int row, int col, T value) {
        matrix[row][col] = value;
    }

    public void fill(T element) {
        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.m; j++) {
                matrix[i][j] = element;
            }
        }
    }

    public void insertRow(int row) {
        if (row < 0 || row > this.n) throw new ArrayIndexOutOfBoundsException();
        T[][] newMatrix = (T[][]) new Object[this.n + 1][this.m];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < this.m; j++) {
                newMatrix[i][j] = this.matrix[i][j];
            }
        }
        for (int i = row; i < this.n; i++) {
            for (int j = 0; j < this.m; j++) {
                newMatrix[i + 1][j] = matrix[i][j];
            }
        }
        this.matrix = newMatrix;
        this.n++;
    }

    public void deleteRow(int row) {
        if (row < 0 || row > this.n) throw new ArrayIndexOutOfBoundsException();
        T[][] newMatrix = (T[][]) new Object[this.n - 1][this.m];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < this.m; j++) {
                newMatrix[i][j] = this.matrix[i][j];
            }
        }
        for (int i = row; i < this.n - 1; i++) {
            for (int j = 0; j < this.m; j++) {
                newMatrix[i][j] = this.matrix[i + 1][j];
            }
        }
        this.matrix = newMatrix;
        this.n--;
    }

    public void insertColumn(int col) {
        if (col < 0 || col > this.m) throw new ArrayIndexOutOfBoundsException();
        T[][] newMatrix = (T[][]) new Object[this.n][this.m + 1];
        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < col; j++) {
                newMatrix[i][j] = this.matrix[i][j];
            }
        }
        for (int i = 0; i < this.n; i++) {
            for (int j = col; j < this.m; j++) {
                newMatrix[i][j + 1] = matrix[i][j];
            }
        }
        this.matrix = newMatrix;
        this.m++;
    }

    public void deleteColumn(int col) {
        if (col < 0 || col > this.m) throw new ArrayIndexOutOfBoundsException();
        T[][] newMatrix = (T[][]) new Object[this.n][this.m - 1];
        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < col; j++) {
                newMatrix[i][j] = this.matrix[i][j];
            }
        }
        for (int i = 0; i < this.n; i++) {
            for (int j = col; j < this.m - 1; j++) {
                newMatrix[i][j] = this.matrix[i][j + 1];
            }
        }
        this.matrix = newMatrix;
        this.m--;
    }

    public void resize(int rows, int cols) {
        T[][] newMatrix = (T[][]) new Object[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (i >= n || j >= m) {
                    newMatrix[i][j] = null;
                } else {
                    newMatrix[i][j] = matrix[i][j];
                }
            }
        }
        this.n = rows;
        this.m = cols;
        this.matrix = newMatrix;
    }
}

public class MatrixTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int t = jin.nextInt();
        if (t == 0) {
            int r = jin.nextInt();
            int c = jin.nextInt();
            Matrix<Integer> matrix = new Matrix<Integer>(r, c);
            print(matrix);
        }
        if (t == 1) {
            int r = jin.nextInt();
            int c = jin.nextInt();
            Matrix<Integer> matrix = new Matrix<Integer>(r, c);
            for (int i = 0; i < r; ++i) {
                for (int k = 0; k < c; ++k) {
                    matrix.setElementAt(i, k, jin.nextInt());
                }
            }
            print(matrix);
        }
        if (t == 2) {
            int r = jin.nextInt();
            int c = jin.nextInt();
            Matrix<String> matrix = new Matrix<String>(r, c);
            for (int i = 0; i < r; ++i) {
                for (int k = 0; k < c; ++k) {
                    matrix.setElementAt(i, k, jin.next());
                }
            }
            print(matrix);
        }
        if (t == 3) {
            int r = jin.nextInt();
            int c = jin.nextInt();
            Matrix<String> matrix = new Matrix<String>(r, c);
            for (int i = 0; i < r; ++i) {
                for (int k = 0; k < c; ++k) {
                    matrix.setElementAt(i, k, jin.next());
                }
            }
            print(matrix);
            matrix.deleteRow(jin.nextInt());
            matrix.deleteRow(jin.nextInt());
            print(matrix);
            int ir = jin.nextInt();
            matrix.insertRow(ir);
            for (int k = 0; k < c; ++k) {
                matrix.setElementAt(ir, k, jin.next());
            }
            ir = jin.nextInt();
            matrix.insertRow(ir);
            for (int k = 0; k < c; ++k) {
                matrix.setElementAt(ir, k, jin.next());
            }
            print(matrix);
            matrix.deleteColumn(jin.nextInt());
            matrix.deleteColumn(jin.nextInt());
            print(matrix);
            int ic = jin.nextInt();
            matrix.insertColumn(ir);
            for (int i = 0; i < r; ++i) {
                matrix.setElementAt(i, ic, jin.next());
            }
            ic = jin.nextInt();
            matrix.insertColumn(ic);
            for (int i = 0; i < r; ++i) {
                matrix.setElementAt(i, ic, jin.next());
            }
            print(matrix);
        }
        if (t == 4) {
            int r = jin.nextInt();
            int c = jin.nextInt();
            Matrix<Integer> matrix = new Matrix<Integer>(r, c);
            for (int i = 0; i < r; ++i) {
                for (int k = 0; k < c; ++k) {
                    matrix.setElementAt(i, k, jin.nextInt());
                }
            }
            print(matrix);
            int nr = jin.nextInt();
            int nc = jin.nextInt();
            matrix.resize(nr, nc);
            print(matrix);
            matrix.fill(jin.nextInt());
            print(matrix);
        }
    }

    public static void print(Matrix<?> m) {
        int r = m.getNumRows();
        int c = m.getNumColumns();
        System.out.println("  " + r + " x " + c);
        System.out.print("    ");
        for (int k = 0; k < c; ++k) {
            System.out.print(k + "    ");
        }
        System.out.println();
        System.out.print("  ");
        for (int k = 0; k < c; ++k) {
            System.out.print("-----");
        }
        System.out.println();
        for (int i = 0; i < r; ++i) {
            System.out.print(i + "|");
            for (int k = 0; k < c; ++k) {
                if (k > 0) System.out.print(" ");
                System.out.print(m.getElementAt(i, k));
            }
            System.out.println();
        }
        System.out.println();
    }

}

