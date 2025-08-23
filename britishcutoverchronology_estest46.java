package org.threeten.extra.chrono;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link BritishCutoverChronology}.
 */
public class BritishCutoverChronologyTest {

    /**
     * Tests that getId() returns the correct, constant identifier for the chronology.
     * The ID should be "BritishCutover" as per the class documentation.
     */
    @Test
    public void getId_shouldReturnConstantIdentifier() {
        // Arrange
        BritishCutoverChronology chronology = BritishCutoverChronology.INSTANCE;
        String expectedId = "BritishCutover";

        // Act
        String actualId = chronology.getId();

        // Assert
        assertEquals("The chronology ID should be a constant value.", expectedId, actualId);
    }
}