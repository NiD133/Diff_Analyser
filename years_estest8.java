package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Years} class.
 */
public class YearsTest {

    /**
     * Tests that the negated() method is pure and does not modify the original Years instance,
     * confirming the class's immutability.
     */
    @Test
    public void negatedDoesNotModifyOriginalInstance() {
        // Arrange: Create a Years instance with a specific value.
        final int initialValue = -1612;
        Years originalYears = Years.years(initialValue);

        // Act: Call the negated() method. The returned new instance is intentionally ignored
        // to verify that this operation does not have side effects on the original object.
        originalYears.negated();

        // Assert: Verify that the original Years object remains unchanged.
        assertEquals("The original Years object should not be mutated by the negated() method.",
                initialValue, originalYears.getYears());
    }
}