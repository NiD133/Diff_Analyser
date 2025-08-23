package org.joda.time.chrono;

import org.joda.time.DateTimeZone;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Contains tests for the {@link IslamicChronology} factory methods.
 */
public class IslamicChronologyTest {

    /**
     * Verifies that creating an IslamicChronology instance with a LeapYearPatternType
     * that has an invalid index throws an ArrayIndexOutOfBoundsException.
     *
     * <p>The factory method {@code IslamicChronology.getInstance} uses an internal cache,
     * which is an array of size 4. The index for this array is derived from the
     * {@code LeapYearPatternType} index, which is cast to a byte. This test ensures
     * that an integer index that results in a negative byte value correctly triggers
     * an exception.</p>
     */
    @Test
    public void getInstance_withInvalidLeapYearPatternIndex_throwsArrayIndexOutOfBounds() {
        // Arrange: Create a LeapYearPatternType with an index that becomes negative
        // when cast to a byte. The value 4497, when cast to a byte, becomes -111.
        final int indexThatBecomesNegativeWhenCast = 4497;
        final int arbitraryPatternValue = 0; // The pattern value is not relevant for this test.
        IslamicChronology.LeapYearPatternType invalidLeapYearPattern =
                new IslamicChronology.LeapYearPatternType(indexThatBecomesNegativeWhenCast, arbitraryPatternValue);

        // Act & Assert
        try {
            // Attempt to get an instance with the invalid leap year pattern.
            // This should fail when accessing the internal cache with index -111.
            IslamicChronology.getInstance((DateTimeZone) null, invalidLeapYearPattern);
            fail("Expected an ArrayIndexOutOfBoundsException to be thrown, but it wasn't.");
        } catch (ArrayIndexOutOfBoundsException e) {
            // Success: The expected exception was caught.
            // Verify that the exception message contains the invalid index, confirming the cause.
            assertEquals("The exception message should report the invalid index.", "-111", e.getMessage());
        }
    }
}