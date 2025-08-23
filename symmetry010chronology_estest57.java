package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.DateTimeException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests for {@link Symmetry010Chronology}.
 */
public class Symmetry010ChronologyTest {

    @Test
    public void eraOf_withInvalidValue_throwsDateTimeException() {
        // Arrange
        Symmetry010Chronology chronology = Symmetry010Chronology.INSTANCE;
        // The Symmetry010Chronology uses IsoEra, where valid values are 0 (BCE) and 1 (CE).
        // We use '2' as a simple, representative invalid value.
        int invalidEraValue = 2;

        // Act & Assert
        try {
            chronology.eraOf(invalidEraValue);
            fail("Expected DateTimeException was not thrown for invalid era value: " + invalidEraValue);
        } catch (DateTimeException e) {
            // Verify that the exception and its message are correct
            assertEquals("Invalid era: " + invalidEraValue, e.getMessage());
        }
    }
}