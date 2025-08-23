package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the Years class, focusing on comparison logic.
 */
public class YearsTest {

    @Test
    public void isLessThan_shouldReturnTrue_whenComparingToLargerValue() {
        // A smaller Years instance should be less than a larger one.
        assertTrue("2 years should be less than 3 years", Years.TWO.isLessThan(Years.THREE));
    }

    @Test
    public void isLessThan_shouldReturnFalse_whenComparingToSmallerValue() {
        // A larger Years instance should not be less than a smaller one.
        assertFalse("3 years should not be less than 2 years", Years.THREE.isLessThan(Years.TWO));
    }

    @Test
    public void isLessThan_shouldReturnFalse_whenComparingToEqualValue() {
        // An instance should not be less than another instance with the same value.
        assertFalse("3 years should not be less than 3 years", Years.THREE.isLessThan(Years.THREE));
    }

    @Test
    public void isLessThan_shouldTreatNullAsZero() {
        // The isLessThan method treats a null argument as a Years instance of zero.
        // This is documented in the method's Javadoc.

        // Scenario 1: A positive value (1) is not less than null (0).
        assertFalse("Years(1) should not be less than null (0)", Years.ONE.isLessThan(null));

        // Scenario 2: A negative value (-1) is less than null (0).
        assertTrue("Years(-1) should be less than null (0)", Years.years(-1).isLessThan(null));
    }
}