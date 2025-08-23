package org.joda.time;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Unit tests for the isGreaterThan() method in the Seconds class.
 */
public class SecondsTest {

    @Test
    public void isGreaterThan_shouldReturnTrue_whenComparingToSmallerValue() {
        assertTrue("Three seconds should be greater than two seconds", Seconds.THREE.isGreaterThan(Seconds.TWO));
    }

    @Test
    public void isGreaterThan_shouldReturnFalse_whenComparingToEqualValue() {
        assertFalse("Three seconds should not be greater than three seconds", Seconds.THREE.isGreaterThan(Seconds.THREE));
    }

    @Test
    public void isGreaterThan_shouldReturnFalse_whenComparingToLargerValue() {
        assertFalse("Two seconds should not be greater than three seconds", Seconds.TWO.isGreaterThan(Seconds.THREE));
    }

    @Test
    public void isGreaterThan_shouldReturnTrue_whenComparingPositiveValueToNull() {
        // Per Javadoc, a null parameter is treated as a zero-second duration.
        assertTrue("One second should be greater than null (zero seconds)", Seconds.ONE.isGreaterThan(null));
    }

    @Test
    public void isGreaterThan_shouldReturnFalse_whenComparingNegativeValueToNull() {
        // Per Javadoc, a null parameter is treated as a zero-second duration.
        Seconds negativeOne = Seconds.seconds(-1);
        assertFalse("Negative one second should not be greater than null (zero seconds)", negativeOne.isGreaterThan(null));
    }
}