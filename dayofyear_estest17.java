package org.threeten.extra;

import org.junit.Test;
import java.time.Year;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class DayOfYear_ESTestTest17 { // Note: Class name kept from original for context.

    /**
     * Tests that atYear() throws a NullPointerException when passed a null Year object.
     * The underlying implementation uses Objects.requireNonNull, which is confirmed by
     * checking the exception message.
     */
    @Test
    public void atYear_withNullYear_throwsNullPointerException() {
        // Arrange: Create a DayOfYear instance. Any valid day will do.
        DayOfYear dayOfYear = DayOfYear.of(150);

        // Act & Assert: Call the method with a null argument and verify the exception.
        try {
            dayOfYear.atYear((Year) null);
            fail("Expected a NullPointerException to be thrown, but no exception occurred.");
        } catch (NullPointerException e) {
            assertEquals("year", e.getMessage());
        }
    }
}