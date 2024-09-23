package com.alipour.learning.patternsearch.kmp;

import java.util.Arrays;

public class KMPSearch {
    public static void search(String pattern, String txt) {
        System.out.println("--------------------------------------------");
        final int[] lps = buildLPS(pattern);

        System.out.println("LPS: " + Arrays.toString(lps));
        System.out.println("Found indexes: " + Arrays.toString(search(pattern, lps, txt)));
    }

    private static int[] search(String pattern, int[] lps, String txt) {
        final char[] ctx = txt.toCharArray();
        final char[] pat = pattern.toCharArray();

        for (int i = 0; i < ctx.length; ) {
            for (int j = 0; j < pat.length; j++) {
                if (ctx[i + j] != pat[j]) {
                    final int lp = lps[j - 1];
                    if (lp == 0) {
                        i = i + j + 1;
                        break;
                    } else {
                        if (ctx[i + j] != pat[lp]){

                            if (lp == 0) {
                                i = i + j + 1;
                                break;
                            }
                          if (pat[lp-1] != ctx[i + j]) {

                          }
                        }
                    }
                }
            }
            i += 1;
        }

    }

    private static int[] buildLPS(String pattern) {
        final char[] pat = pattern.toCharArray();
        final int[] LPS = new int[pat.length];
        int i = 0;
        LPS[0] = 0;
        for (int j = 1; j < pat.length; j++) {
            if (pat[i] == pat[j]) {
                LPS[j] = (i + 1);
                i += 1;
            }
        }
        return LPS;
    }
}
