package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

// The test class name and inheritance are kept from the original for context.
public class Weeks_ESTestTest18 extends Weeks_ESTest_scaffolding {

    /**
     * Tests that the negated() method returns a new Weeks instance with the
     * opposite value and does not modify the original instance, thus confirming
     * the class's immutability.
     */
    @Test
    public void negatedReturnsNewInstanceWithOppositeValueAndDoesNotChangeOriginal() {
        // Arrange: Create a Weeks instance with a negative value.
        final int initialValue = -2490;
        Weeks originalWeeks = Weeks.weeks(initialValue);

        // Act: Call the method under test.
        Weeks negatedWeeks = originalWeeks.negated();

        // Assert: Verify both the original and the new instance.
        
        // 1. Confirm immutability: The original object must not be changed.
        assertEquals("The original Weeks object should not be modified.",
                     initialValue, originalWeeks.getWeeks());

        // 2. Confirm correctness: The new object should have the correctly negated value.
        assertEquals("The new Weeks object should have the negated value.",
                     -initialValue, negatedWeeks.getWeeks());
    }
}