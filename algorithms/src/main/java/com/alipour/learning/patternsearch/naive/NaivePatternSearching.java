package com.alipour.learning.patternsearch.naive;

public class NaivePatternSearching {

    public static void main(String[] args) {
        search("AABA", "AABAACAADAABAABAA");
        System.out.println("----------------------------------------------------------------");
        search("AABA", "AABAACAADAABAAABAA");
        System.out.println("----------------------------------------------------------------");
        search("AS", "SHDYEASDSDADLDKASDADFSRLISFSDFSASDFSDSSDFAFASSDAFALSFJ");
    }

    /**
     * Naive pattern searching is the simplest method among other pattern-searching algorithms.
     * It checks for all characters of the main string to the pattern.
     * This algorithm is helpful for smaller texts. It does not need any pre-processing phases.
     * We can find the substring by checking once for the string.
     * It also does not occupy extra space to perform the operation.
     * O(m*n)
     */

    public static void search(String pattern, String txt) {

        final char[] pat = pattern.toCharArray();
        final char[] context = txt.toCharArray();

        final int TL = context.length;
        final int PL = pat.length;

        for (int i = 0; i < (TL - PL); i++) {
            for (int j = 0; j < PL; j++) {
                if (i + j < TL) {
                    if (context[i + j] != pat[j]) {
                        break;
                    }
                    if (j == (PL - 1)) {
                        System.out.println("Found pattern in index: " + i);
                    }
                }
            }
        }
    }
}
