package com.alipour.learning.patternsearch.kmp;

public class KMPPattern {

    public static void main(String[] args) {
        String txt = "ABABDABACDABABCABAB";
        String pat = "ABABCABAB";
//        pat = "ABCDABD";
        KMPSearch.search(pat, txt);
    }
}
