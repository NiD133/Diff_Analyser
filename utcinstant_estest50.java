package org.threeten.extra.scale;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Contains tests for the {@link UtcInstant#equals(Object)} method.
 */
public class UtcInstantTest {

    /**
     * Tests that the equals() method returns false when a UtcInstant is
     * compared with an object of a completely different type.
     */
    @Test
    public void equals_returnsFalse_whenComparedWithObjectOfDifferentType() {
        // Arrange: Create a UtcInstant instance and an object of an unrelated type.
        UtcInstant utcInstant = UtcInstant.ofModifiedJulianDay(0L, 0L);
        Object otherObject = new Object();

        // Act: Compare the UtcInstant with the other object.
        boolean isEqual = utcInstant.equals(otherObject);

        // Assert: The result should be false, as per the equals contract.
        assertFalse("UtcInstant.equals() must return false when compared to an object of a different type.", isEqual);
    }
}