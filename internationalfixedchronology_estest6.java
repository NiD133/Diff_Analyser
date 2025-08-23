package org.threeten.extra.chrono;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit tests for the {@link InternationalFixedChronology} class, focusing on the
 * {@code getLeapYearsBefore} static method.
 */
public class InternationalFixedChronologyTest {

    /**
     * Tests that the number of leap years before year 0 is correctly calculated as 0.
     * <p>
     * The International Fixed Chronology is defined for years 1 and greater. This test
     * verifies the behavior for an input just below the supported range, ensuring it
     * handles this edge case gracefully.
     */
    @Test
    public void getLeapYearsBefore_forYearZero_returnsZero() {
        // Arrange: Define the input year and the expected result.
        long prolepticYear = 0L;
        long expectedLeapYearCount = 0L;

        // Act: Call the method under test.
        long actualLeapYearCount = InternationalFixedChronology.getLeapYearsBefore(prolepticYear);

        // Assert: Verify the result matches the expectation.
        assertEquals(
            "The number of leap years before year 0 should be 0.",
            expectedLeapYearCount,
            actualLeapYearCount
        );
    }
}