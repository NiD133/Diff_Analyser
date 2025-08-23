package org.joda.time.base;

import org.joda.time.LocalDateTime;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * This test class evaluates the comparison methods of {@link AbstractPartial}.
 * This specific test focuses on the isAfter() method using {@link LocalDateTime}
 * as a concrete implementation.
 */
public class AbstractPartialTest {

    /**
     * Tests that isAfter() returns true when the partial instance is chronologically
     * later than the one it is being compared to.
     */
    @Test
    public void isAfter_shouldReturnTrue_whenPartialIsLater() {
        // Arrange: Create two LocalDateTime instances where one is clearly after the other.
        LocalDateTime referenceDateTime = new LocalDateTime(2023, 10, 27, 10, 0);
        LocalDateTime laterDateTime = referenceDateTime.plusWeeks(1);

        // Act & Assert: Verify that the later date is correctly identified as being "after" the reference date.
        assertTrue("A date-time should be considered 'after' an earlier one.",
                   laterDateTime.isAfter(referenceDateTime));
    }
}