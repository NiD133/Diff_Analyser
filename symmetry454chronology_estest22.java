package org.threeten.extra.chrono;

import org.junit.jupiter.api.Test;
import java.time.DateTimeException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for the {@link Symmetry454Chronology} class.
 */
class Symmetry454ChronologyTest {

    /**
     * Tests that calling {@code eraOf(int)} with an invalid integer value
     * throws a {@code DateTimeException}.
     * <p>
     * The Symmetry454Chronology uses the same eras as the ISO calendar system (IsoEra),
     * which only defines eras for the values 0 (BCE) and 1 (CE). Any other value is invalid.
     */
    @Test
    void eraOf_shouldThrowException_forInvalidEraValue() {
        // Arrange: Get an instance of the chronology and define an invalid era value.
        Symmetry454Chronology chronology = Symmetry454Chronology.INSTANCE;
        int invalidEraValue = -334;
        String expectedErrorMessage = "Invalid era: " + invalidEraValue;

        // Act & Assert: Verify that calling eraOf() with the invalid value throws the correct exception.
        DateTimeException thrown = assertThrows(
                DateTimeException.class,
                () -> chronology.eraOf(invalidEraValue),
                "Symmetry454Chronology.eraOf() should throw for an invalid era value."
        );

        // Further Assert: Check that the exception message is as expected.
        assertEquals(expectedErrorMessage, thrown.getMessage());
    }
}