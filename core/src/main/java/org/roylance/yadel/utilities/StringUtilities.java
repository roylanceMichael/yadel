package org.roylance.yadel.utilities;

public class StringUtilities {
    public static double editDistance(final String first, final String second) {
        if (first == null || second == null) {
            return 1.0;
        }

        final int[][] matrix = new int[first.length() + 1][];

        for(int i = 0; i < first.length(); i++) {
            for (int j = 0; j < second.length(); j++) {
                if (matrix[i] == null) {
                    matrix[i] = new int[second.length() + 1];
                }

                if (j == 0 && i == 0) {
                    matrix[i][j] = 0;
                }
                else if (j == 0) {
                    matrix[i][j] = i;
                }
                else if (i == 0) {
                    matrix[i][j] = j;
                }
                else {
                    final int firstTemp = matrix[i-1][j] + 2;
                    final int secondTemp = matrix[i][j-1] + 2;
                    final int cost = first.charAt(i-1) == second.charAt(j-1) ? 0 : 4;

                    final int thirdTemp = matrix[i-1][j-1] + cost;

                    if (firstTemp <= secondTemp && firstTemp <= thirdTemp)
                    {
                        matrix[i][j] = firstTemp;
                    }
                    else if (secondTemp <= firstTemp && secondTemp <= thirdTemp)
                    {
                        matrix[i][j] = secondTemp;
                    }
                    else
                    {
                        matrix[i][j] = thirdTemp;
                    }
                }
            }
        }

        return matrix[first.length() - 1][second.length() - 1];
    }
}
