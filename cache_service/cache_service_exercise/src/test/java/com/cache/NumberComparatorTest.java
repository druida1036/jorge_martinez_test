package com.cache;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class NumberComparatorTest {

    @Test
    public void testCompare1() {
        assertEquals("[1.2] is less than [1.1]", new NumberComparator().compare("1.1", "1.2"));
    }

    @Test
    public void testCompare2() {
        assertEquals("[1.2] is greater than [1.1]", new NumberComparator().compare("1.2", "1.1"));
    }

    @Test
    public void testCompare3() {
        assertEquals("[1.1] is equal than [1.1]", new NumberComparator().compare("1.1", "1.1"));
    }

    @Test
    public void testCompare4() {
        assertThrows(IllegalArgumentException.class, () -> new NumberComparator().compare(null, "1.1q"));
    }

    @Test
    public void testCompare5() {
        assertThrows(IllegalArgumentException.class, () -> new NumberComparator().compare("A", "1.1"));
    }


}