package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.chrono.IsoEra;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Symmetry010Chronology} class.
 */
public class Symmetry010ChronologyTest {

    @Test
    public void dateNow_shouldReturnDateInCommonEra() {
        // Arrange
        // The Symmetry010Chronology is a singleton, so we use its static INSTANCE.
        Symmetry010Chronology chronology = Symmetry010Chronology.INSTANCE;

        // Act
        // Get the current date in this chronology from the system clock.
        Symmetry010Date currentDate = chronology.dateNow();

        // Assert
        // The current date is expected to be in the Common Era (CE).
        assertEquals("The era of the current date should be Common Era (CE)", IsoEra.CE, currentDate.getEra());
    }
}