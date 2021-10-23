package com.imc.workshop.core.limits;

public class MathUtil {
    public static Matrix transpose(Matrix matrix) {
        // throw new UnsupportedOperationException("Function not yet implemented.");
        int numOfRows = matrix.getNumberOfRows();
        int numOfCols = matrix.getNumberOfColumns();
        var t = new double[numOfCols][numOfRows];
        for (int i = 0; i < numOfRows; i++) {
            for (int j = 0; j < numOfCols; j++) {
                t[j][i] = matrix.get(i, j);
            }
        }
        return new Matrix(t);
    }

    public static Matrix multiply(Matrix leftMatrix, Matrix rightMatrix) {
        // throw new UnsupportedOperationException("Function not yet implemented.");
        int row1 = leftMatrix.getNumberOfRows();
        // int col1 = leftMatrix.getNumberOfColumns();
        int row2 = rightMatrix.getNumberOfRows();
        int col2 = rightMatrix.getNumberOfColumns();
        var m = new double[row1][col2];
        for (int i = 0; i < row1; i++) {
            for (int j = 0; j < col2; j++) {
                for (int k = 0; k < row2; k++) {
                    m[i][j] += leftMatrix.get(i, k) * rightMatrix.get(k, j);
                }
            }
        }
        return new Matrix(m);
    }
}
