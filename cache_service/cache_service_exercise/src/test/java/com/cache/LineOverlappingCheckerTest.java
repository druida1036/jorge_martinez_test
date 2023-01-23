package com.cache;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LineOverlappingCheckerTest {

    @Test
    public void testCheckLinesOverlapSample1() {
        assertTrue(new LineOverlappingChecker().checkOverlap(1, 5, 2, 6));
    }

    @Test
    public void testCheckLinesOverlapSample2() {
        assertFalse(new LineOverlappingChecker().checkOverlap(1, 5, 6, 8));
    }

    @Test
    public void testCheckLinesOverlapSample3() {
        assertTrue(new LineOverlappingChecker().checkOverlap(2, 6, 1, 5));
    }

    @Test
    public void testCheckLinesOverlapSample4() {
        assertFalse(new LineOverlappingChecker().checkOverlap(-1, -1, 0, 0));
    }

    @Test
    public void testCheckLinesOverlapSample5() {
        assertTrue(new LineOverlappingChecker().checkOverlap(-4, -1, -2, 0));
    }

    @Test
    public void testCheckLinesOverlapSample6() {
        assertTrue(new LineOverlappingChecker().checkOverlap(-4, 1, 0, 4));
    }

    @Test
    public void testCheckLinesOverlapSample7() {
        assertTrue(new LineOverlappingChecker().checkOverlap(-4, 1, -1, 4));
    }

    @Test
    public void testCheckLinesOverlapSample8() {
        assertTrue(new LineOverlappingChecker().checkOverlap(-4, -1, -3, -2));
    }
}