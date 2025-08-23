package org.joda.time;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Unit tests for the isLessThan() method in the {@link Minutes} class.
 */
public class MinutesIsLessThanTest {

    @Test
    public void isLessThan_shouldReturnTrue_whenThisMinutesIsSmaller() {
        // A smaller Minutes value should be less than a larger one.
        assertTrue("2 minutes should be less than 3 minutes", Minutes.TWO.isLessThan(Minutes.THREE));
    }

    @Test
    public void isLessThan_shouldReturnFalse_whenThisMinutesIsLarger() {
        // A larger Minutes value should not be less than a smaller one.
        assertFalse("3 minutes should not be less than 2 minutes", Minutes.THREE.isLessThan(Minutes.TWO));
    }

    @Test
    public void isLessThan_shouldReturnFalse_whenMinutesAreEqual() {
        // An equal Minutes value should not be considered less than.
        assertFalse("3 minutes should not be less than 3 minutes", Minutes.THREE.isLessThan(Minutes.THREE));
    }

    @Test
    public void isLessThan_shouldTreatNullAsZero() {
        // The isLessThan(null) comparison should treat the null argument as a Minutes object of zero.
        
        // -1 is less than 0
        assertTrue("Negative minutes should be less than null (zero)", Minutes.minutes(-1).isLessThan(null));
        
        // 1 is not less than 0
        assertFalse("Positive minutes should not be less than null (zero)", Minutes.ONE.isLessThan(null));
    }
}