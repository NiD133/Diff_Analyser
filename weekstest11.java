package org.joda.time;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Unit tests for the {@link Weeks#isGreaterThan(Weeks)} method.
 */
public class WeeksIsGreaterThanTest {

    @Test
    public void isGreaterThan_shouldReturnTrue_whenThisInstanceIsLarger() {
        assertTrue("3 weeks should be greater than 2 weeks", Weeks.THREE.isGreaterThan(Weeks.TWO));
    }

    @Test
    public void isGreaterThan_shouldReturnFalse_whenThisInstanceIsSmaller() {
        assertFalse("2 weeks should not be greater than 3 weeks", Weeks.TWO.isGreaterThan(Weeks.THREE));
    }

    @Test
    public void isGreaterThan_shouldReturnFalse_whenInstancesAreEqual() {
        assertFalse("3 weeks should not be greater than 3 weeks", Weeks.THREE.isGreaterThan(Weeks.THREE));
    }

    @Test
    public void isGreaterThan_shouldTreatNullAsZeroAndReturnTrueForPositive() {
        // The isGreaterThan method treats a null input as a Weeks object with a value of zero.
        assertTrue("1 week should be greater than null (0 weeks)", Weeks.ONE.isGreaterThan(null));
    }

    @Test
    public void isGreaterThan_shouldTreatNullAsZeroAndReturnFalseForNegative() {
        // The isGreaterThan method treats a null input as a Weeks object with a value of zero.
        assertFalse("-1 weeks should not be greater than null (0 weeks)", Weeks.weeks(-1).isGreaterThan(null));
    }
}