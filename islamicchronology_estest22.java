package org.joda.time.chrono;

import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.junit.Test;

import static org.junit.Assert.assertNull;

/**
 * This test class focuses on the behavior of the IslamicChronology.
 * The original test was auto-generated and has been refactored for clarity.
 */
public class IslamicChronology_ESTestTest22 extends IslamicChronology_ESTest_scaffolding {

    /**
     * Tests that getLeapYearPatternType() returns null if the chronology is
     * instantiated with a null leap year pattern via its package-private constructor.
     */
    @Test
    public void getLeapYearPatternType_shouldReturnNull_whenConstructedWithNullPattern() {
        // Arrange
        // This test directly invokes the package-private constructor to test an internal state.
        // The public factory methods (e.g., getInstance()) always provide a default leap year pattern.
        Chronology baseChronology = GJChronology.getInstance(DateTimeZone.UTC);
        Object dummyParam = "test-param"; // The 'param' object is for internal use (serialization).
        IslamicChronology.LeapYearPatternType nullPattern = null;

        IslamicChronology chronology = new IslamicChronology(baseChronology, dummyParam, nullPattern);

        // Act
        IslamicChronology.LeapYearPatternType actualPattern = chronology.getLeapYearPatternType();

        // Assert
        assertNull("The leap year pattern should be null when the chronology is constructed with a null pattern.", actualPattern);
    }
}