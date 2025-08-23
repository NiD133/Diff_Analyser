package org.threeten.extra;

import org.junit.Test;
import java.time.temporal.ChronoField;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link DayOfYear#get(TemporalField)}.
 */
public class DayOfYearTest {

    @Test
    public void get_withDayOfYearField_returnsCorrectValue() {
        // Arrange
        int expectedDay = 150;
        DayOfYear dayOfYear = DayOfYear.of(expectedDay);

        // Act
        int actualDay = dayOfYear.get(ChronoField.DAY_OF_YEAR);

        // Assert
        assertEquals(expectedDay, actualDay);
    }
}