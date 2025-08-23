package org.threeten.extra.chrono;

import org.junit.jupiter.api.Test;

import java.time.DateTimeException;
import java.time.chrono.Era;
import java.time.chrono.IsoEra;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for {@link Symmetry454Chronology}.
 */
class Symmetry454ChronologyTest {

    @Test
    void prolepticYear_whenYearOfEraIsBelowMinimum_throwsDateTimeException() {
        // Arrange
        Symmetry454Chronology chronology = Symmetry454Chronology.INSTANCE;
        Era bceEra = IsoEra.BCE;
        // From the source, the valid range for yearOfEra is -1,000,000 to 1,000,000.
        // We test with a value just outside this boundary for clarity.
        int yearBelowMinimum = -1_000_001;

        // Act & Assert
        DateTimeException exception = assertThrows(
            DateTimeException.class,
            () -> chronology.prolepticYear(bceEra, yearBelowMinimum)
        );

        // Verify the exception message is correct and informative
        String expectedMessage = "Invalid value for YearOfEra (valid values -1000000 - 1000000): " + yearBelowMinimum;
        assertEquals(expectedMessage, exception.getMessage());
    }
}