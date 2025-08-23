package org.joda.time;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Test cases for the isLessThan() method of the Weeks class.
 */
public class WeeksTest {

    @Test
    public void isLessThan_shouldReturnTrue_whenThisIsSmaller() {
        assertTrue("Two weeks should be less than three weeks", Weeks.TWO.isLessThan(Weeks.THREE));
    }

    @Test
    public void isLessThan_shouldReturnFalse_whenThisIsGreater() {
        assertFalse("Three weeks should not be less than two weeks", Weeks.THREE.isLessThan(Weeks.TWO));
    }

    @Test
    public void isLessThan_shouldReturnFalse_whenValuesAreEqual() {
        assertFalse("Three weeks should not be less than three weeks", Weeks.THREE.isLessThan(Weeks.THREE));
    }

    @Test
    public void isLessThan_shouldTreatNullAsZero() {
        // The isLessThan method's contract specifies that a null parameter is treated as Weeks.ZERO.
        
        // Case 1: A positive value (1) compared to null (0). 1 is not less than 0.
        assertFalse("A positive Weeks value should not be less than null", Weeks.ONE.isLessThan(null));
        
        // Case 2: A negative value (-1) compared to null (0). -1 is less than 0.
        assertTrue("A negative Weeks value should be less than null", Weeks.weeks(-1).isLessThan(null));
    }
}