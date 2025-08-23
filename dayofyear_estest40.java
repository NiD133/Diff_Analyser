package org.threeten.extra;

import org.junit.Test;
import java.time.LocalDate;
import java.time.chrono.ThaiBuddhistDate;
import java.time.temporal.TemporalField;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Contains tests for the {@link DayOfYear} class.
 */
public class DayOfYearTest {

    /**
     * Tests that isSupported() returns false for a null TemporalField.
     * <p>
     * This test also implicitly verifies that a {@code DayOfYear} can be correctly created
     * from a date in a non-ISO calendar system, such as {@code ThaiBuddhistDate}.
     */
    @Test
    public void isSupported_shouldReturnFalse_whenFieldIsNull() {
        // Arrange: Create a DayOfYear from a specific ThaiBuddhistDate.
        // We use February 15th, which is the 46th day of a non-leap year.
        LocalDate isoDate = LocalDate.of(2023, 2, 15);
        ThaiBuddhistDate thaiDate = ThaiBuddhistDate.from(isoDate);
        DayOfYear dayOfYear = DayOfYear.from(thaiDate);

        // Sanity check to ensure the DayOfYear was created as expected.
        assertEquals("PRECONDITION: DayOfYear from ThaiBuddhistDate should be 46", 46, dayOfYear.getValue());

        // Act: Call the method under test with a null argument.
        boolean isSupported = dayOfYear.isSupported((TemporalField) null);

        // Assert: The method should return false for null input, as per its contract.
        assertFalse("isSupported(null) must return false.", isSupported);
    }
}