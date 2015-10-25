package cs221;

/**
 * Created by ibush on 10/25/15.
 */
public class TestMatrix {

    public static void main(String[] args)
    {
        System.out.println("Subtraction");
        int[][] m1 = {{1,2}, {2,3}, {4,0}};
        int[][] m2 = {{0,1}, {0,1}, {0,1}};
        try {

            int[][] result = Matrix.subtract(m1, m2);
            for(int i = 0; i < result.length; i++ ) {
                for (int j = 0; j < result[i].length; j++) {
                    System.out.println(result[i][j]);
                }
            }
        } catch (Exception e) {
            System.out.println("Matrix sizes don't match");
        }

        System.out.println("Multiplication");
        int[][] m1Mult = {{1,2}, {2,3}, {4,0}};
        int[][] m2Mult = {{1}, {2}};
        try {

            int[][] result = Matrix.multiply(m1Mult, m2Mult);
            for(int i = 0; i < result.length; i++ ) {
                for (int j = 0; j < result[i].length; j++) {
                    System.out.println(result[i][j]);
                }
            }
        } catch (Exception e) {
            System.out.println("Matrix sizes don't work for multiplication");
        }
    }
}
