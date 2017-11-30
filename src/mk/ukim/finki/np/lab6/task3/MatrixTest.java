package mk.ukim.finki.np.lab6.task3;

import java.util.ArrayList;
import java.util.Scanner;

class Matrix<T> {
    private ArrayList<ArrayList<T>> matrix;
    private int m;
    private int n;

    public Matrix(int numRows, int numCols) {
        this.n = numRows;
        this.m = numCols;
        matrix = new ArrayList<>();
        initialFill(matrix, numRows, numCols);
    }

    public int getNumRows() {
        return this.n;
    }

    public int getNumColumns() {
        return m;
    }

    public T getElementAt(int row, int col) {
        return matrix.get(row).get(col);
    }

    public void setElementAt(int row, int col, T value) {
        matrix.get(row).set(col, value);
    }

    public void fill(T element) {
        for (int i = 0; i < n; i++) {
            ArrayList<T> col = matrix.get(i);
            for (int j = 0; j < m; j++) {
                col.set(j, element);
            }
            matrix.add(col);
        }
    }

    public void insertRow(int row) {
        if (row < 0 || row > n) throw new ArrayIndexOutOfBoundsException();

        matrix.add(row, new ArrayList<>());
    }

    public void deleteRow(int row) {
        if (row < 0 || row > n) throw new ArrayIndexOutOfBoundsException();

        matrix.remove(row);
    }

    public void insertColumn(int col) {

    }

    public void deleteColumn(int col) {

    }

    public void resize(int rows, int cols) {
        boolean bigger = false;
        if (this.n < rows || this.m < cols) {
            bigger = true;
        }

        System.out.println(bigger);
        if (bigger) {
            for (int i = 0; i < rows; i++) {
                ArrayList<T> listCol;
                if (i > this.n) {
                    listCol = new ArrayList<>();
                } else {
                    listCol = matrix.get(i);
                }
                for (int j = this.m; j < cols; j++) {
                    listCol.add(j, null);
                }
                matrix.add(listCol);
            }
        } else {

        }
    }

    private void initialFill(ArrayList<ArrayList<T>> matrix, int numRows, int numCols) {
        for (int i = 0; i < numRows; i++) {
            ArrayList<T> cols = new ArrayList<>(numCols);
            for (int j = 0; j < numCols; j++) {
                cols.add(null);
            }
            matrix.add(cols);
        }
    }
}

public class MatrixTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int t = jin.nextInt();
        if ( t == 0 ) {
            int r = jin.nextInt();
            int c = jin.nextInt();
            Matrix<Integer> matrix = new Matrix<Integer>(r,c);
            print(matrix);
        }
        if ( t == 1 ) {
            int r = jin.nextInt();
            int c = jin.nextInt();
            Matrix<Integer> matrix = new Matrix<Integer>(r,c);
            for ( int i = 0 ; i < r ; ++i ) {
                for ( int k = 0 ; k < c ; ++k ) {
                    matrix.setElementAt(i, k, jin.nextInt());
                }
            }
            print(matrix);
        }
        if ( t == 2 ) {
            int r = jin.nextInt();
            int c = jin.nextInt();
            Matrix<String> matrix = new Matrix<String>(r,c);
            for ( int i = 0 ; i < r ; ++i ) {
                for ( int k = 0 ; k < c ; ++k ) {
                    matrix.setElementAt(i, k, jin.next());
                }
            }
            print(matrix);
        }
        if ( t == 3 ) {
            int r = jin.nextInt();
            int c = jin.nextInt();
            Matrix<String> matrix = new Matrix<String>(r,c);
            for ( int i = 0 ; i < r ; ++i ) {
                for ( int k = 0 ; k < c ; ++k ) {
                    matrix.setElementAt(i, k, jin.next());
                }
            }
            print(matrix);
            matrix.deleteRow(jin.nextInt());
            matrix.deleteRow(jin.nextInt());
            print(matrix);
            int ir = jin.nextInt();
            matrix.insertRow(ir);
            for ( int k = 0 ; k < c ; ++k ) {
                matrix.setElementAt(ir, k, jin.next());
            }
            ir = jin.nextInt();
            matrix.insertRow(ir);
            for ( int k = 0 ; k < c ; ++k ) {
                matrix.setElementAt(ir, k, jin.next());
            }
            print(matrix);
            matrix.deleteColumn(jin.nextInt());
            matrix.deleteColumn(jin.nextInt());
            print(matrix);
            int ic = jin.nextInt();
            matrix.insertColumn(ir);
            for ( int i = 0 ; i < r ; ++i ) {
                matrix.setElementAt(i, ic, jin.next());
            }
            ic = jin.nextInt();
            matrix.insertColumn(ic);
            for ( int i = 0 ; i < r ; ++i ) {
                matrix.setElementAt(i, ic, jin.next());
            }
            print(matrix);
        }
        if ( t == 4 ) {
            int r = jin.nextInt();
            int c = jin.nextInt();
            Matrix<Integer> matrix = new Matrix<Integer>(r,c);
            for ( int i = 0 ; i < r ; ++i ) {
                for ( int k = 0 ; k < c ; ++k ) {
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

    public static void print ( Matrix<?> m ) {
        int r = m.getNumRows();int c = m.getNumColumns();
        System.out.println("  "+r+" x "+c);
        System.out.print("    ");
        for ( int k = 0 ; k < c ; ++k ) {
            System.out.print(k+"    ");
        }
        System.out.println();
        System.out.print("  ");
        for ( int k = 0 ; k < c ; ++k ) {
            System.out.print("-----");
        }
        System.out.println();
        for ( int i = 0 ; i < r ; ++i ) {
            System.out.print(i+"|");
            for ( int k = 0 ; k < c ; ++k ) {
                if ( k > 0 ) System.out.print(" ");
                System.out.print(m.getElementAt(i, k));
            }
            System.out.println();
        }
        System.out.println();
    }

}

