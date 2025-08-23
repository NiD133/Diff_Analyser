package org.threeten.extra.chrono;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the {@link Symmetry010Chronology#isLeapYear(long)} method.
 *
 * Note: The original test class appeared to be auto-generated. This version has been
 * refactored for human readability and maintainability.
 */
public class Symmetry010Chronology_ESTestTest7 extends Symmetry010Chronology_ESTest_scaffolding {

    /**
     * Tests that a known negative leap year is correctly identified.
     */
    @Test
    public void isLeapYear_shouldReturnTrue_forKnownNegativeLeapYear() {
        // Arrange
        // The class under test is a singleton, so we should use its public INSTANCE.
        Symmetry010Chronology chronology = Symmetry010Chronology.INSTANCE;
        long yearKnownToBeLeap = -1547L;

        // According to the Symmetry010Chronology documentation, the leap year formula is:
        // isLeap = (52 > ((52 * year + 146) % 293))
        // For year = -1547:
        // (52 * -1547 + 146) % 293 = (-80298) % 293 = -16
        // The condition (52 > -16) is true, so -1547 must be a leap year.

        // Act
        boolean isLeap = chronology.isLeapYear(yearKnownToBeLeap);

        // Assert
        assertTrue(
            "Year " + yearKnownToBeLeap + " should be correctly identified as a leap year.",
            isLeap
        );
    }
}