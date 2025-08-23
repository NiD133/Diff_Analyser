package org.threeten.extra;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

/**
 * This test suite contains tests for the DayOfYear class, focusing on its value-based behavior,
 * such as the implementation of the equals() and hashCode() methods.
 */
public class DayOfYearTest {

    /**
     * Verifies that the equals() method returns false when comparing two different DayOfYear instances.
     *
     * <p>This improved test uses explicit, hardcoded values to create DayOfYear instances.
     * This approach makes the test's intent clear and its outcome deterministic, enhancing
     * readability and maintainability compared to relying on system clocks or complex mock objects.
     */
    @Test
    public void testEqualsForDifferentInstances() {
        // Arrange: Create two distinct DayOfYear instances with clear, different values.
        DayOfYear dayOfYear150 = DayOfYear.of(150);
        DayOfYear dayOfYear200 = DayOfYear.of(200);

        // Act: Perform the equality check.
        boolean areEqual = dayOfYear150.equals(dayOfYear200);

        // Assert:
        // 1. Verify that the two different instances are not considered equal.
        assertFalse("Two DayOfYear instances with different values should not be equal.", areEqual);

        // 2. Explicitly verify the symmetric property of the equals method.
        assertFalse("The equals() method must be symmetric.", dayOfYear200.equals(dayOfYear150));

        // 3. For completeness, verify that their hash codes are also different.
        // While not strictly required by the equals/hashCode contract for non-equal objects,
        // it is expected for well-behaved value types.
        assertNotEquals("Hash codes for different instances should be different.",
                dayOfYear150.hashCode(), dayOfYear200.hashCode());
    }
}