package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.chrono.Era;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the eras of the JulianChronology.
 */
public class JulianChronologyEraTest {

    @Test
    public void eras_shouldReturnBothBCAndAD() {
        // Arrange
        JulianChronology julianChronology = JulianChronology.INSTANCE;
        List<Era> expectedEras = Arrays.asList(JulianEra.BC, JulianEra.AD);

        // Act
        List<Era> actualEras = julianChronology.eras();

        // Assert
        assertEquals("Julian chronology must contain both BC and AD eras.", expectedEras, actualEras);
    }
}