package org.joda.time.chrono;

import org.joda.time.DateTimeZone;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link CopticChronology}.
 */
public class CopticChronologyTest {

    /**
     * This constant is a pre-calculated value used for performance optimization
     * within the CopticChronology implementation. It represents half of the
     * approximate number of milliseconds between the Coptic calendar's
     * hypothetical year 0 and the standard Unix epoch (1970-01-01T00:00:00Z).
     *
     * This test ensures that this internal constant remains unchanged,
     * as it's crucial for correct date calculations.
     */
    private static final long EXPECTED_APPROX_MILLIS_AT_EPOCH_DIVIDED_BY_TWO = 26607895200000L;

    @Test
    public void getApproxMillisAtEpochDividedByTwo_returnsCorrectConstantValue() {
        // Arrange: Get a standard, timezone-independent instance of the chronology.
        CopticChronology copticChronology = CopticChronology.getInstanceUTC();

        // Act: Call the method under test.
        long actualValue = copticChronology.getApproxMillisAtEpochDividedByTwo();

        // Assert: Verify that the returned value matches the expected, pre-calculated constant.
        assertEquals(EXPECTED_APPROX_MILLIS_AT_EPOCH_DIVIDED_BY_TWO, actualValue);
    }
}