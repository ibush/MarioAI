package cs221.test;

import cs221.Matrix;

/**
 * Created by ibush on 10/25/15.
 */
public class TestMatrix {

    public static void main(String[] args)
    {
        System.out.println("Subtraction");
        float[][] m1 = {{1,2}, {2,3}, {4,0}};
        float[][] m2 = {{0,1}, {0,1}, {0,1}};
        try {

            float[][] result = Matrix.subtract(m1, m2);
            for(int i = 0; i < result.length; i++ ) {
                for (int j = 0; j < result[i].length; j++) {
                    System.out.println(result[i][j]);
                }
            }
        } catch (Exception e) {
            System.out.println("Matrix sizes don't match");
        }

        System.out.println("Multiplication");
        float[][] m1Mult = {{1,2}, {2,3}, {4,0}};
        float[][] m2Mult = {{1}, {2}};
        try {

            float[][] result = Matrix.multiply(m1Mult, m2Mult);
            for(int i = 0; i < result.length; i++ ) {
                for (int j = 0; j < result[i].length; j++) {
                    System.out.println(result[i][j]);
                }
            }
        } catch (Exception e) {
            System.out.println("Matrix sizes don't work for multiplication");
        }

        System.out.println("Dot Product");
        float[] v1 = {1, 3, 4};
        float[] v2 = {1, 2, 1};
        try {
            float result = Matrix.dotProduct(v1, v2);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println("Matrix sizes don't work for multiplication");
        }
    }
}
