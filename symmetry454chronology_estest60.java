package org.threeten.extra.chrono;

import org.junit.Test;

import static org.junit.Assert.assertNull;

/**
 * Tests for the {@link Symmetry454Chronology} class.
 */
public class Symmetry454ChronologyTest {

    /**
     * Tests that {@link Symmetry454Chronology#getCalendarType()} returns null.
     * <p>
     * The documentation for this method specifies that it returns null because the Unicode
     * LDML (Locale Data Markup Language) specification does not define an identifier for
     * this calendar system. This test verifies this documented behavior.
     */
    @Test
    public void getCalendarType_shouldReturnNull() {
        // Arrange: Obtain the singleton instance of the chronology.
        Symmetry454Chronology chronology = Symmetry454Chronology.INSTANCE;

        // Act: Call the method under test.
        String calendarType = chronology.getCalendarType();

        // Assert: Verify that the result is null as per the contract.
        assertNull("The calendar type for Symmetry454Chronology should be null.", calendarType);
    }
}