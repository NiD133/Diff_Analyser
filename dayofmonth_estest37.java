package org.threeten.extra;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Unit tests for the {@link DayOfMonth} class.
 */
public class DayOfMonthTest {

    /**
     * Tests that isSupported() returns false when the provided TemporalField is null.
     * <p>
     * The Javadoc for the isSupported method specifies that a null input should
     * result in false. This test verifies that contract.
     */
    @Test
    public void isSupported_shouldReturnFalse_whenFieldIsNull() {
        // Arrange: Create an arbitrary DayOfMonth instance. The specific day does not matter.
        DayOfMonth dayOfMonth = DayOfMonth.of(15);

        // Act: Call the method under test with a null argument.
        boolean isSupported = dayOfMonth.isSupported(null);

        // Assert: Verify that the result is false, as per the method's contract.
        assertFalse("isSupported(null) should return false.", isSupported);
    }
}