package com.dev.studylog.codinginterview.ch4;

/**
 * 이미지를 표현하는 NXN행렬이 있다. 이미지의 각 픽셀은 4바이트로 표현된다.
 * 이때 이미지를 90도 회전시키는 메서드를 작성하라.
 * 행렬을 추가로 사용하지 않고서도 할 수 있겠는가?
 */
public class RotateMatrics {

    public static void print(int[][] matrix) {
        for(int i=0; i<matrix.length; i++){
            for(int j=0; j<matrix.length; j++){
                System.out.print(matrix[i][j]+" ");
            }
            System.out.println("");
        }
    }
    public static void rotate(int[][] matrix) {
        int n = matrix.length;
        /*
        제일 바깥부터 층별로 회전하고, 만약  n이 홀수라면 제일 가운데는 회전되지 않고 그대로 남아있는다.
         */
        for(int layer=0; layer<n/2; layer++){
            int first = layer;
            int last = n - layer - 1;
            for(int i=first; i<last; i++){
                int offset = i - first;
                int top = matrix[first][i];

                //왼 - > 위
                matrix[first][i] = matrix[last-offset][first];
                // 아래 -> 왼
                matrix[last-offset][first] = matrix[last][last-offset];
                // 오른 - > 아래
                matrix[last][last-offset] = matrix[i][last];

                // 위 - > 오른
                matrix[i][last] = top;
            }
        }
    }

    public static void main(String[] args) {
        int[][] matrix = new int[][]{{1,2,3,4},{5,6,7,8},{9,10,11,12},{13,14,15,16}};
        print(matrix);
        rotate(matrix);
        print(matrix);
    }
}
