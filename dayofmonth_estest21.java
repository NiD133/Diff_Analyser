package org.threeten.extra;

import org.junit.Test;
import java.time.temporal.Temporal;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for {@link DayOfMonth#adjustInto(Temporal)}.
 */
public class DayOfMonthTest {

    /**
     * Tests that adjustInto throws a NullPointerException when the input temporal is null.
     */
    @Test
    public void adjustInto_whenTemporalIsNull_throwsNullPointerException() {
        // Arrange: Create a DayOfMonth instance. The specific value doesn't matter for this test.
        DayOfMonth dayOfMonth = DayOfMonth.of(15);

        // Act & Assert: Call adjustInto with null and verify the exception.
        try {
            dayOfMonth.adjustInto(null);
            fail("Expected a NullPointerException to be thrown, but no exception was thrown.");
        } catch (NullPointerException e) {
            // The method is expected to use Objects.requireNonNull(temporal, "temporal"),
            // so we can check for the specific message.
            assertEquals("temporal", e.getMessage());
        }
    }
}