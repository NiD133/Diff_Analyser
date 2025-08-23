package org.threeten.extra.chrono;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Symmetry010Chronology} class.
 */
public class Symmetry010ChronologyTest {

    @Test
    public void getId_shouldReturnCorrectIdentifier() {
        // Arrange
        Symmetry010Chronology chronology = Symmetry010Chronology.INSTANCE;
        String expectedId = "Sym010";

        // Act
        String actualId = chronology.getId();

        // Assert
        assertEquals(expectedId, actualId);
    }
}