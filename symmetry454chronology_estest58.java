package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.chrono.IsoEra;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Symmetry454Chronology} class.
 */
public class Symmetry454ChronologyTest {

    /**
     * Tests that {@link Symmetry454Chronology#dateNow()} returns a date
     * within the Common Era (CE), assuming the system clock is set to a
     * modern date.
     */
    @Test
    public void dateNow_shouldReturnDateInCommonEra() {
        // Arrange
        Symmetry454Chronology chronology = Symmetry454Chronology.INSTANCE;

        // Act
        Symmetry454Date currentDate = chronology.dateNow();

        // Assert
        assertEquals("The era of the current date should be Common Era (CE)", IsoEra.CE, currentDate.getEra());
    }
}