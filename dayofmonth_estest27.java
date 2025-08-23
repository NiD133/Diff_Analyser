package org.threeten.extra;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * This test class contains an improved version of a test for the {@link DayOfMonth} class.
 * The original test was auto-generated, making its purpose unclear. The refactored test
 * below is focused, clearly named, and easy to comprehend.
 */
public class DayOfMonth_ESTestTest27 extends DayOfMonth_ESTest_scaffolding {

    /**
     * Tests the reflexive property of the DayOfMonth.equals() method.
     *
     * <p>According to the contract of {@link Object#equals(Object)}, for any non-null
     * reference value x, the expression {@code x.equals(x)} must return {@code true}.
     * This test verifies that contract for the {@code DayOfMonth} class.
     */
    @Test
    public void equals_shouldReturnTrue_whenComparedToTheSameInstance() {
        // Arrange: Create an instance of DayOfMonth.
        // The value 14 is chosen to align with the original test's implicit value,
        // but any valid day of the month would work.
        DayOfMonth dayOfMonth = DayOfMonth.of(14);

        // Act & Assert: Verify that the object is equal to itself.
        // This is a fundamental requirement for any implementation of the equals method.
        assertEquals("An instance of DayOfMonth must be equal to itself.", dayOfMonth, dayOfMonth);
    }
}