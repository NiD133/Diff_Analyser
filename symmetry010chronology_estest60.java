package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.chrono.Era;
import java.time.chrono.IsoEra;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the {@link Symmetry010Chronology}.
 */
public class Symmetry010ChronologyTest {

    @Test
    public void eras_shouldReturnListOfTwoEras() {
        // Arrange
        Symmetry010Chronology chronology = Symmetry010Chronology.INSTANCE;

        // Act
        List<Era> actualEras = chronology.eras();

        // Assert
        // The documentation states that the Symmetry010Chronology uses the same eras as the ISO calendar system.
        assertEquals("There should be exactly two eras", 2, actualEras.size());
        assertTrue("The list of eras should contain BCE", actualEras.contains(IsoEra.BCE));
        assertTrue("The list of eras should contain CE", actualEras.contains(IsoEra.CE));
    }
}