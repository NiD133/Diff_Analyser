package org.joda.time;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Unit tests for the {@link Years} class.
 */
public class YearsTest {

    @Test
    public void constants_shouldHoldCorrectValues() {
        assertEquals("Years.ZERO should have a value of 0", 0, Years.ZERO.getYears());
        assertEquals("Years.ONE should have a value of 1", 1, Years.ONE.getYears());
        assertEquals("Years.TWO should have a value of 2", 2, Years.TWO.getYears());
        assertEquals("Years.THREE should have a value of 3", 3, Years.THREE.getYears());
        assertEquals("Years.MAX_VALUE should be Integer.MAX_VALUE", Integer.MAX_VALUE, Years.MAX_VALUE.getYears());
        assertEquals("Years.MIN_VALUE should be Integer.MIN_VALUE", Integer.MIN_VALUE, Years.MIN_VALUE.getYears());
    }
}