package cs221;

/**
 * Created by ibush on 10/25/15.
 */
public class Matrix {

    public static int[][] subtract(int[][] m1, int[][] m2) throws Exception {
        if ((m1.length != m2.length) || (m1[0].length != m2[0].length)) {
            throw new Exception("Matrices must be same dimensions");
        }
        int[][] result = new int[m1.length][m1[0].length];
        for(int i = 0; i < m1.length; i++ ) {
            for(int j = 0; j < m1[i].length; j++) {
                result[i][j] = m1[i][j] - m2[i][j];
            }
        }
        return result;
    }

    public static int[][] add(int[][] m1, int[][] m2) throws Exception {
        if ((m1.length != m2.length) || (m1[0].length != m2[0].length)) {
            throw new Exception("Matrices must be same dimensions");
        }
        int[][] result = new int[m1.length][m1[0].length];
        for(int i = 0; i < m1.length; i++ ) {
            for(int j = 0; j < m1[i].length; j++) {
                result[i][j] = m1[i][j] + m2[i][j];
            }
        }
        return result;
    }

    public static int[][] multiply(int[][] m1, int[][] m2) throws Exception {
        int m1Rows = m1.length;
        int m1Cols = m1[0].length;
        int m2Rows = m2.length;
        int m2Cols = m2[0].length;

        if (m1Cols != m2Rows) {
            throw new Exception("Matrices must have same inner dimensions");
        }
        int[][] result = new int[m1Rows][m2Cols];

        for (int i = 0; i < m1Rows; i++) {
            for (int j = 0; j < m2Cols; j++) {
                for (int k = 0; k < m1Cols; k++) {
                    result[i][j] += m1[i][k] * m2[k][j];
                }
            }
        }
        return result;
    }

}
