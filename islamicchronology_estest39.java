package org.joda.time.chrono;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Unit tests for the {@link IslamicChronology} class.
 */
public class IslamicChronologyTest {

    /**
     * Verifies that an IslamicChronology.LeapYearPatternType object is not
     * considered equal to an IslamicChronology object, as they are different types.
     * This tests the robustness of the LeapYearPatternType.equals() method.
     */
    @Test
    public void leapYearPatternType_shouldNotBeEqualTo_chronologyInstance() {
        // Arrange
        IslamicChronology chronology = IslamicChronology.getInstance();
        IslamicChronology.LeapYearPatternType leapYearPattern = IslamicChronology.LEAP_YEAR_16_BASED;

        // Act
        // The .equals() method is called on the LeapYearPatternType object.
        boolean result = leapYearPattern.equals(chronology);

        // Assert
        assertFalse("A LeapYearPatternType should not be equal to an IslamicChronology instance.", result);
    }
}