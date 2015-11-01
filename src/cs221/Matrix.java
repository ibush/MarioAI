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

    public static float[] subtract(float[] v1, float[] v2) {
        float[] result = new float[v1.length];
        for(int i = 0; i < v1.length; i++ ) {
            result[i] = v1[i] - v2[i];
        }
        return result;
    }

    public static float[] add(float[] v1, float[] v2) {
        float[] result = new float[v1.length];
        for(int i = 0; i < v1.length; i++ ) {
            result[i] = v1[i] + v2[i];
        }
        return result;
    }

    public static float[] scalarMult(float[] v1, float scale) {
        float[] result = new float[v1.length];
        for(int i = 0; i < v1.length; i++ ) {
            result[i] = scale * v1[i];
        }
        return result;
    }

    public static float dotProduct(float[] v1, float[] v2) {
        float result = 0;
        for(int i = 0; i < v1.length; i++) {
            result += v1[i] * v2[i];
        }
        return result;
    }

}
