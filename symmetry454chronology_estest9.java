package org.threeten.extra.chrono;

import static org.junit.Assert.assertEquals;

import java.time.chrono.IsoEra;
import org.junit.Test;

/**
 * Tests for the {@link Symmetry454Chronology} class.
 */
public class Symmetry454ChronologyTest {

    /**
     * Tests that calling eraOf(0) on the Symmetry454Chronology
     * returns the 'Before Common Era' (BCE).
     */
    @Test
    public void testEraOf_withValue0_returnsBce() {
        // Arrange
        Symmetry454Chronology chronology = Symmetry454Chronology.INSTANCE;
        IsoEra expectedEra = IsoEra.BCE;

        // Act
        IsoEra actualEra = chronology.eraOf(0);

        // Assert
        assertEquals("eraOf(0) should return IsoEra.BCE", expectedEra, actualEra);
    }
}