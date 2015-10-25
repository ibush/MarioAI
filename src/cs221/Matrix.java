package cs221;

import java.awt.*;

/**
 * Created by ibush on 10/25/15.
 */
public class Matrix {

    public static float[][] subtract(float[][] m1, float[][] m2) throws Exception {
        if ((m1.length != m2.length) || (m1[0].length != m2[0].length)) {
            throw new Exception("Matrices must be same dimensions");
        }
        float[][] result = new float[m1.length][m1[0].length];
        for(int i = 0; i < m1.length; i++ ) {
            for(int j = 0; j < m1[i].length; j++) {
                result[i][j] = m1[i][j] - m2[i][j];
            }
        }
        return result;
    }

    public static float[][] add(float[][] m1, float[][] m2) throws Exception {
        if ((m1.length != m2.length) || (m1[0].length != m2[0].length)) {
            throw new Exception("Matrices must be same dimensions");
        }
        float[][] result = new float[m1.length][m1[0].length];
        for(int i = 0; i < m1.length; i++ ) {
            for(int j = 0; j < m1[i].length; j++) {
                result[i][j] = m1[i][j] + m2[i][j];
            }
        }
        return result;
    }

    public static float[][] multiply(float[][] m1, float[][] m2) throws Exception {
        int m1Rows = m1.length;
        int m1Cols = m1[0].length;
        int m2Rows = m2.length;
        int m2Cols = m2[0].length;

        if (m1Cols != m2Rows) {
            throw new Exception("Matrices must have same inner dimensions");
        }
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

    public static float dotProduct(float[] v1, float[] v2) throws Exception {
        if(v1.length != v2.length) {
            throw new Exception("Vectors must be of same length");
        }
        float result = 0;
        for(int i = 0; i < v1.length; i++) {
            result += v1[i] * v2[i];
        }
        return result;
    }

}
