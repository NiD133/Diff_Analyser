package org.threeten.extra.chrono;

import org.junit.Test;

import static org.junit.Assert.assertNull;

/**
 * This test class contains tests for the Symmetry010Chronology class.
 * The original test was auto-generated and has been refactored for clarity.
 */
public class Symmetry010Chronology_ESTestTest56 extends Symmetry010Chronology_ESTest_scaffolding {

    /**
     * Verifies that getCalendarType() returns null as specified by its contract.
     * <p>
     * The Javadoc for {@link Symmetry010Chronology#getCalendarType()} states that
     * the Unicode LDML specification does not define an identifier for this calendar system,
     * so the method is expected to return null.
     */
    @Test
    public void getCalendarType_shouldReturnNull() {
        // Arrange: The Symmetry010Chronology is a singleton, so we use its public instance.
        Symmetry010Chronology chronology = Symmetry010Chronology.INSTANCE;

        // Act: Get the calendar type from the chronology.
        String calendarType = chronology.getCalendarType();

        // Assert: The returned calendar type should be null.
        assertNull("The calendar type should be null as it is not defined in LDML.", calendarType);
    }
}