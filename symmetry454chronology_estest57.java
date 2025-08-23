package org.threeten.extra.chrono;

import static org.junit.Assert.assertEquals;

import java.time.chrono.Era;
import java.time.chrono.IsoEra;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

/**
 * Tests for the {@link Symmetry454Chronology} class.
 */
public class Symmetry454ChronologyTest {

    /**
     * Tests that the eras() method returns the correct list of eras,
     * which should be consistent with the ISO calendar system eras (BCE and CE).
     */
    @Test
    public void eras_shouldReturnIsoEras() {
        // Arrange
        Symmetry454Chronology chronology = Symmetry454Chronology.INSTANCE;
        List<Era> expectedEras = Arrays.asList(IsoEra.values());

        // Act
        List<Era> actualEras = chronology.eras();

        // Assert
        assertEquals(expectedEras, actualEras);
    }
}