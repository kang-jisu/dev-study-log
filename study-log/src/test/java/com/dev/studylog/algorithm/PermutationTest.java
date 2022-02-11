package com.dev.studylog.algorithm;

import org.junit.jupiter.api.Test;

public class PermutationTest {


    /**
     * visit배열을 통해서 arr에 값 넣어주면서 출력
     */
    static boolean visit[] = new boolean[8];
    static int[] arr;
    static StringBuilder sb = new StringBuilder("");
    static int N;

    public static void permutation(int cnt) {
        if (cnt == N) {
            for (int i = 0; i < N; i++) {
                sb.append(arr[i] + " ");
            }
            sb.append("\n");
            return;
        }
        for (int i = 0; i < N; i++) {
            if (visit[i]) continue;
            visit[i] = true;
            arr[cnt] = i + 1;
            permutation(cnt + 1);
            visit[i] = false;
        }
    }


    @Test
    public void permuteTest() {
        N = 3;
        arr = new int[N];
        permutation(0);
        System.out.println(sb.toString());
    }
}