package org.threeten.extra.chrono;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.time.chrono.Era;
import java.time.chrono.JapaneseEra;
import org.junit.Test;

/**
 * Test for {@link Symmetry454Chronology} focusing on invalid era handling.
 */
public class Symmetry454ChronologyInvalidEraTest {

    /**
     * Verifies that dateYearDay() throws a ClassCastException when provided with an era
     * from a different calendar system. The Symmetry454Chronology is designed to work
     * only with the ISO calendar's eras (CE and BCE).
     */
    @Test
    public void dateYearDay_withNonIsoEra_throwsClassCastException() {
        // Arrange: Set up the chronology and an era that is incompatible with it.
        Symmetry454Chronology chronology = Symmetry454Chronology.INSTANCE;
        Era invalidEra = JapaneseEra.SHOWA;
        int yearOfEra = 10; // An arbitrary, valid year for the test.
        int dayOfYear = 100; // An arbitrary, valid day of the year.

        // Act & Assert: Attempting to create a date should fail with a ClassCastException.
        try {
            chronology.dateYearDay(invalidEra, yearOfEra, dayOfYear);
            fail("Expected a ClassCastException to be thrown for an incompatible era type.");
        } catch (ClassCastException e) {
            // This is the expected outcome.
            // For a more robust test, we can verify that the exception message
            // correctly identifies the invalid era that was used.
            String expectedMessageContent = invalidEra.toString();
            assertTrue(
                "The exception message should contain the name of the invalid era.",
                e.getMessage().contains(expectedMessageContent)
            );
        }
    }
}