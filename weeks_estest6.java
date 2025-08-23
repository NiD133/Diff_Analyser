package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link Weeks} class.
 */
public class WeeksTest {

    /**
     * Tests that parsing a null string returns a period of zero weeks,
     * as specified by the method's contract.
     */
    @Test
    public void parseWeeks_withNullInput_returnsZeroWeeks() {
        // Arrange: The Javadoc for parseWeeks() specifies that a null input
        // should be treated as a zero-week period.

        // Act: Call the method with a null input.
        Weeks result = Weeks.parseWeeks(null);

        // Assert: The result should be the constant for zero weeks.
        assertEquals(Weeks.ZERO, result);
    }
}