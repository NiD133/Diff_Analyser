package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Years} class.
 */
public class YearsTest {

    /**
     * Tests that the factory method `yearsIn()` returns zero years when
     * the input interval is null.
     */
    @Test
    public void yearsIn_givenNullInterval_shouldReturnZeroYears() {
        // Act: Call the factory method with a null interval.
        Years result = Years.yearsIn(null);

        // Assert: The result should be the constant for zero years.
        assertEquals(Years.ZERO, result);
    }
}