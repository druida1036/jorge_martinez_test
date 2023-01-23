package com.cache;

public class LineOverlappingChecker {

    public boolean checkOverlap(int x1, int x2, int x3, int x4) {
        if (x3 < 0 && x1 <= x3 && x3 <= x2) {
            return true;
        }
        if (x1 <= x3 && x3 <= x2) {
            return true;
        } else {
            return x1 > x3 && x2 > x4;
        }
    }
}
