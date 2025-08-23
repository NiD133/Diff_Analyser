package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.temporal.ChronoField;
import java.time.temporal.ValueRange;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link InternationalFixedChronology}.
 */
public class InternationalFixedChronologyTest {

    /**
     * Verifies that the range for the ERA field is correctly defined.
     * <p>
     * The International Fixed Chronology has only one era, 'CE', with the numeric value 1.
     * The range() method should therefore return a fixed range of [1, 1] for the ERA field.
     */
    @Test
    public void range_forEraField_returnsCorrectRange() {
        // Arrange: The International Fixed Chronology has a single era.
        InternationalFixedChronology chronology = InternationalFixedChronology.INSTANCE;
        ValueRange expectedRange = ValueRange.of(1, 1);

        // Act: Get the range for the ERA field.
        ValueRange actualRange = chronology.range(ChronoField.ERA);

        // Assert: The actual range must match the expected range of [1, 1].
        assertEquals("The range for the ERA field should be [1, 1].", expectedRange, actualRange);
    }
}