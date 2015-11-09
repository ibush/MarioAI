package cs221;

import java.awt.*;

/**
 * Created by ibush on 10/25/15.
 */
public class Matrix {

    public static float[][] subtract(float[][] m1, float[][] m2) {
        float[][] result = new float[m1.length][m1[0].length];
        for(int i = 0; i < m1.length; i++ ) {
            for(int j = 0; j < m1[i].length; j++) {
                result[i][j] = m1[i][j] - m2[i][j];
            }
        }
        return result;
    }

    public float[][] add(float[][] m1, float[][] m2) {
        float[][] result = new float[m1.length][m1[0].length];
        for(int i = 0; i < m1.length; i++ ) {
            for(int j = 0; j < m1[i].length; j++) {
                result[i][j] = m1[i][j] + m2[i][j];
            }
        }
        return result;
    }

    public static float[][] multiply(float[][] m1, float[][] m2) {
        int m1Rows = m1.length;
        int m1Cols = m1[0].length;
        int m2Rows = m2.length;
        int m2Cols = m2[0].length;

        float[][] result = new float[m1Rows][m2Cols];

        for (int i = 0; i < m1Rows; i++) {
            for (int j = 0; j < m2Cols; j++) {
                for (int k = 0; k < m1Cols; k++) {
                    result[i][j] += m1[i][k] * m2[k][j];
                }
            }
        }
        return result;
    }

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
        System.out.println("NOT YET IMPLEMENTED");
        return new double[1][1];
    }

    // return a (m x n) matrix of ones
    public static double[][] ones(int m, int n){
        System.out.println("NOT YET IMPLEMENTED");
        return new double[1][1];
    }

    // return a (m x n) matrix of zeros
    public static double[][] zeros(int m, int n){
        System.out.println("NOT YET IMPLEMENTED");
        return new double[1][1];
    }

}
