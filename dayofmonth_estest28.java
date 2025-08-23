package org.threeten.extra;

import org.junit.Test;
import java.time.ZoneOffset;
import static org.junit.Assert.assertFalse;

/**
 * Tests for the {@link DayOfMonth#equals(Object)} method.
 */
public class DayOfMonth_ESTestTest28 extends DayOfMonth_ESTest_scaffolding {

    /**
     * Tests that the equals() method returns false when comparing a DayOfMonth
     * instance with an object of a completely different type.
     */
    @Test
    public void equals_returnsFalseWhenComparedWithObjectOfDifferentType() {
        // Arrange
        DayOfMonth dayOfMonth = DayOfMonth.of(15);
        Object differentTypeObject = ZoneOffset.MAX;

        // Act
        boolean isEqual = dayOfMonth.equals(differentTypeObject);

        // Assert
        assertFalse("DayOfMonth should not be equal to an object of a different type.", isEqual);
    }
}