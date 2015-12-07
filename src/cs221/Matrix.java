package cs221;

import java.awt.*;
import java.util.Random;

public class Matrix {

    public static double[][] subtract(double[][] m1, double[][] m2) {
        assert m1.length == m2.length && m1[0].length == m2[0].length;
        double[][] result = new double[m1.length][m1[0].length];
        for(int i = 0; i < m1.length; i++ ) {
            for(int j = 0; j < m1[i].length; j++) {
                result[i][j] = m1[i][j] - m2[i][j];
            }
        }
        return result;
    }

    public static double[][] add(double[][] m1, double[][] m2) {
        assert m1.length == m2.length && m1[0].length == m2[0].length;
        double[][] result = new double[m1.length][m1[0].length];
        for(int i = 0; i < m1.length; i++ ) {
            for(int j = 0; j < m1[i].length; j++) {
                result[i][j] = m1[i][j] + m2[i][j];
            }
        }
        return result;
    }

    public static double[][] multiply(double[][] m1, double[][] m2) {
        assert m1.length == m2.length && m1[0].length == m2[0].length;
        int m1Rows = m1.length;
        int m1Cols = m1[0].length;
        int m2Rows = m2.length;
        int m2Cols = m2[0].length;

        double[][] result = new double[m1Rows][m2Cols];

        for (int i = 0; i < m1Rows; i++) {
            for (int j = 0; j < m2Cols; j++) {
                for (int k = 0; k < m1Cols; k++) {
                    result[i][j] += m1[i][k] * m2[k][j];
                }
            }
        }
        return result;
    }

    public static double[][] scalarMult(double[][] m, double scale) {
        double[][] result = new double[m.length][m[0].length];
        for(int i = 0; i < m.length; i++ ) {
            for(int j = 0; j < m[i].length; j++) {
                result[i][j] = scale * m[i][j];
            }
        }
        return result;
    }

    public static double[][] scalarAdd(double[][] m, double scalar){
        double[][] result = new double[m.length][m[0].length];
        for(int i = 0; i < m.length; i++ ) {
            for (int j = 0; j < m[i].length; j++) {
                result[i][j] = scalar + m[i][j];
            }
        }
        return result;
    }

    public static double[][] transpose(double[][] m) {
        double[][] result = new double[m[0].length][m.length];
        for(int i = 0; i < m.length; i++ ) {
            for (int j = 0; j < m[i].length; j++) {
                result[j][i] = m[i][j];
            }
        }
        return result;
    }


    // Vector Functions

    public static double[] subtract(double[] v1, double[] v2) {
        double[] result = new double[v1.length];
        for(int i = 0; i < v1.length; i++ ) {
            result[i] = v1[i] - v2[i];
        }
        return result;
    }

    public static double[] add(double[] v1, double[] v2) {
        double[] result = new double[v1.length];
        for(int i = 0; i < v1.length; i++ ) {
            result[i] = v1[i] + v2[i];
        }
        return result;
    }

    public static double[] scalarMult(double[] v1, double scale) {
        double[] result = new double[v1.length];
        for(int i = 0; i < v1.length; i++ ) {
            result[i] = scale * v1[i];
        }
        return result;
    }

    public static double dotProduct(double[] v1, double[] v2) {
        double result = 0;
        for(int i = 0; i < v1.length; i++) {
            result += v1[i] * v2[i];
        }
        return result;
    }

    // Extended methods for neural network computations


    // Returns a (m x n) matrix of random numbers in [0,1]
    public static double[][] rand(int m, int n){
        Random numGenerator = new Random();
        double[][] result = new double[m][n];
        for(int i = 0; i < m; i++ ) {
            for(int j = 0; j < n; j++) {
                result[i][j] = numGenerator.nextFloat();
            }
        }
        return result;
    }

    // Returns a (m x n) matrix of random numbers draw from Norm(mu=0,sigma=stddev)
    public static double[][] norm(int m, int n, double stddev){
        Random numGenerator = new Random();
        double[][] result = new double[m][n];
        for(int i = 0; i < m; i++ ) {
            for(int j = 0; j < n; j++) {
                result[i][j] = numGenerator.nextGaussian() * stddev;
            }
        }
        return result;
    }

    // return a (m x n) matrix of ones
    public static double[][] ones(int m, int n){
        double[][] result = new double[m][n];
        for(int i = 0; i < m; i++ ) {
            for(int j = 0; j < n; j++) {
                result[i][j] = 1;
            }
        }
        return result;
    }

    // return a (m x n) matrix of zeros
    public static double[][] zeros(int m, int n){
        double[][] result = new double[m][n];
        for(int i = 0; i < m; i++ ) {
            for(int j = 0; j < n; j++) {
                result[i][j] = 0;
            }
        }
        return result;
    }

    // Calculates Rectified Linear Unit function on matrix m.
    // Stores the partial derivative (1 if m[i][j] > 0) in dRelu
    public static double[][] relu(double[][] m, double[][] dRelu) {
        double[][] result = new double[m.length][m[0].length];
        for(int i = 0; i < m.length; i++ ) {
            for(int j = 0; j < m[i].length; j++) {
                if(m[i][j] > 0) {
                    result[i][j] = m[i][j];
                    dRelu[i][j] = 1;
                } else {
                    result[i][j] = dRelu[i][j] = 0;
                }
            }
        }
        return result;
    }


}
