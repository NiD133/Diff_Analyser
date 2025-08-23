package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test cases for the {@link Minutes#isGreaterThan(Minutes)} method.
 */
public class MinutesTest {

    @Test
    public void isGreaterThan_shouldReturnTrue_whenComparingToSmallerValue() {
        assertTrue("3 minutes should be greater than 2 minutes", Minutes.THREE.isGreaterThan(Minutes.TWO));
    }

    @Test
    public void isGreaterThan_shouldReturnFalse_whenComparingToLargerValue() {
        assertFalse("2 minutes should not be greater than 3 minutes", Minutes.TWO.isGreaterThan(Minutes.THREE));
    }

    @Test
    public void isGreaterThan_shouldReturnFalse_whenComparingToSameValue() {
        assertFalse("3 minutes should not be greater than 3 minutes", Minutes.THREE.isGreaterThan(Minutes.THREE));
    }

    @Test
    public void isGreaterThan_shouldReturnTrue_whenComparingPositiveToNull() {
        // A null Minutes object is treated as zero.
        assertTrue("1 minute should be greater than null (0 minutes)", Minutes.ONE.isGreaterThan(null));
    }

    @Test
    public void isGreaterThan_shouldReturnFalse_whenComparingNegativeToNull() {
        // A null Minutes object is treated as zero.
        Minutes negativeMinutes = Minutes.minutes(-1);
        assertFalse("-1 minutes should not be greater than null (0 minutes)", negativeMinutes.isGreaterThan(null));
    }
}