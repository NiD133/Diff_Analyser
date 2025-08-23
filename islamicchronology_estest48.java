package org.joda.time.chrono;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link IslamicChronology} class.
 */
public class IslamicChronologyTest {

    /**
     * Tests that the year difference between two identical millisecond instants is zero.
     */
    @Test
    public void getYearDifference_withSameInstant_returnsZero() {
        // Arrange
        IslamicChronology islamicChronology = IslamicChronology.getInstance();
        
        // The specific instant in time does not matter for this test,
        // only that the minuend and subtrahend are identical.
        long anInstant = -2966L;

        // Act
        long yearDifference = islamicChronology.getYearDifference(anInstant, anInstant);

        // Assert
        assertEquals("The year difference between an instant and itself must be 0.", 0L, yearDifference);
    }
}