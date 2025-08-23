package org.threeten.extra.chrono;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests for the {@link InternationalFixedChronology}.
 */
public class InternationalFixedChronologyTest {

    @Test
    public void getId_shouldReturnCorrectIdentifier() {
        // The ID of the International Fixed Chronology is defined to be "Ifc".
        // This test confirms that the singleton instance returns the correct ID.
        
        // Arrange
        InternationalFixedChronology chronology = InternationalFixedChronology.INSTANCE;
        String expectedId = "Ifc";

        // Act
        String actualId = chronology.getId();

        // Assert
        assertEquals("The chronology ID should be the constant 'Ifc'", expectedId, actualId);
    }
}