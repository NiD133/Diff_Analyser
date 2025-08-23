package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.DateTimeException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Tests for {@link InternationalFixedChronology}.
 */
public class InternationalFixedChronologyTest {

    @Test
    public void dateEpochDay_whenEpochDayIsBelowMinimum_throwsDateTimeException() {
        // Arrange
        InternationalFixedChronology chronology = InternationalFixedChronology.INSTANCE;
        // The valid range for epoch day is [-719528, 364522971].
        // We test with a value just below the minimum boundary.
        long minValidEpochDay = -719528L;
        long invalidEpochDay = minValidEpochDay - 1;

        // Act & Assert
        DateTimeException thrown = assertThrows(
            DateTimeException.class,
            () -> chronology.dateEpochDay(invalidEpochDay)
        );

        // Verify that the exception message is informative and correct.
        String expectedMessage = "Invalid value for EpochDay (valid values " +
                                 minValidEpochDay + " - 364522971): " + invalidEpochDay;
        assertEquals(expectedMessage, thrown.getMessage());
    }
}