package org.threeten.extra.chrono;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link Symmetry454Chronology} class.
 *
 * This class demonstrates improvements to an auto-generated test case, focusing on
 * making it more understandable and maintainable.
 */
public class Symmetry454ChronologyTest {

    /**
     * Tests that creating a date from a TemporalAccessor that is already a
     * {@code Symmetry454Date} returns an equal instance.
     *
     * This verifies that the {@code date(TemporalAccessor)} method correctly handles
     * identity conversions within its own chronology.
     */
    @Test
    public void dateFromTemporal_whenInputIsAlreadySymmetry454Date_returnsEqualDate() {
        // Arrange
        Symmetry454Chronology chronology = Symmetry454Chronology.INSTANCE;
        // Use a fixed, arbitrary date to ensure the test is deterministic and repeatable.
        // The original test used Symmetry454Date.now(), which is non-deterministic.
        Symmetry454Date originalDate = chronology.date(2023, 3, 15);

        // Act
        // Call the method under test, which converts a TemporalAccessor to a Symmetry454Date.
        Symmetry454Date convertedDate = chronology.date(originalDate);

        // Assert
        // The resulting date should be identical to the original input date.
        // This is a more comprehensive check than the original test's assertion,
        // which only verified the era.
        assertEquals(originalDate, convertedDate);
    }
}