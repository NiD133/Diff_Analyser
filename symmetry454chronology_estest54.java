package org.threeten.extra.chrono;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link Symmetry454Chronology} class.
 */
public class Symmetry454ChronologyTest {

    /**
     * Verifies that the chronology's ID is correctly defined as "Sym454".
     * This ID is crucial for looking up the chronology via {@code Chronology.of(String)}.
     */
    @Test
    public void getId_shouldReturnCorrectIdentifier() {
        // Arrange
        Symmetry454Chronology chronology = Symmetry454Chronology.INSTANCE;
        String expectedId = "Sym454";

        // Act
        String actualId = chronology.getId();

        // Assert
        assertEquals("The chronology ID should match the expected value.", expectedId, actualId);
    }
}