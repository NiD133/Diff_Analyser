package org.threeten.extra.scale;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for {@link UtcInstant}.
 * This class focuses on edge cases related to numeric overflow.
 */
// The original class name and inheritance are preserved to match the context provided.
public class UtcInstant_ESTestTest29 extends UtcInstant_ESTest_scaffolding {

    /**
     * Verifies that converting a UtcInstant with the maximum possible Modified Julian Day
     * to a TaiInstant throws an ArithmeticException due to a long overflow.
     */
    @Test(timeout = 4000)
    public void toTaiInstant_whenModifiedJulianDayIsMax_throwsArithmeticException() {
        // Arrange: Create a UtcInstant at the largest possible date.
        // The internal conversion to TAI seconds involves multiplying this large day
        // value, which is expected to cause a numeric overflow.
        UtcInstant instantAtMaxMjd = UtcInstant.ofModifiedJulianDay(Long.MAX_VALUE, 0L);

        // Act & Assert
        try {
            instantAtMaxMjd.toTaiInstant();
            fail("Expected ArithmeticException was not thrown for numeric overflow.");
        } catch (ArithmeticException e) {
            // This is the expected outcome.
            // We can assert on the message to confirm the cause of the exception.
            assertEquals("long overflow", e.getMessage());
        }
    }
}