package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the isGreaterThan() method in the Years class.
 */
public class YearsTest {

    @Test
    public void isGreaterThan_shouldReturnTrue_whenThisIsLarger() {
        assertTrue("3 years should be greater than 2 years", Years.THREE.isGreaterThan(Years.TWO));
    }

    @Test
    public void isGreaterThan_shouldReturnFalse_whenValuesAreEqual() {
        assertFalse("3 years should not be greater than 3 years", Years.THREE.isGreaterThan(Years.THREE));
    }

    @Test
    public void isGreaterThan_shouldReturnFalse_whenThisIsSmaller() {
        assertFalse("2 years should not be greater than 3 years", Years.TWO.isGreaterThan(Years.THREE));
    }

    @Test
    public void isGreaterThan_shouldReturnTrue_whenComparingPositiveToNull() {
        // Per the method's contract, a null Years object is treated as zero years.
        assertTrue("1 year should be greater than null (0 years)", Years.ONE.isGreaterThan(null));
    }

    @Test
    public void isGreaterThan_shouldReturnFalse_whenComparingNegativeToNull() {
        // Per the method's contract, a null Years object is treated as zero years.
        assertFalse("-1 years should not be greater than null (0 years)", Years.years(-1).isGreaterThan(null));
    }
}