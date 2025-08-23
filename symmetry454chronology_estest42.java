package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.temporal.ChronoField;
import java.time.temporal.ValueRange;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the range of fields in the Symmetry454Chronology.
 * This class replaces the auto-generated test class Symmetry454Chronology_ESTestTest42.
 */
public class Symmetry454ChronologyTest {

    /**
     * Tests that the range() method returns the correct value range for the ERA field.
     * The Symmetry454 chronology supports two eras (BCE and CE), represented by the values 0 and 1.
     */
    @Test
    public void range_forEraField_returnsCorrectRange() {
        // Arrange
        Symmetry454Chronology chronology = Symmetry454Chronology.INSTANCE;
        ValueRange expectedEraRange = ValueRange.of(0, 1);

        // Act
        ValueRange actualEraRange = chronology.range(ChronoField.ERA);

        // Assert
        assertEquals("The range for the ERA field should be 0 to 1", expectedEraRange, actualEraRange);
    }
}