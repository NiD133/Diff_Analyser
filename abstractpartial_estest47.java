package org.joda.time.base;

import org.joda.time.YearMonth;
import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertFalse;

/**
 * Unit tests for the {@link AbstractPartial} class, focusing on the equals() method.
 */
public class AbstractPartialTest {

    /**
     * Tests that the equals() method returns false when comparing a partial
     * with an object of an incompatible type.
     *
     * The implementation in AbstractPartial should first check if the compared object
     * is an instance of ReadablePartial before proceeding with value comparisons.
     */
    @Test
    public void equals_whenComparedWithDifferentObjectType_shouldReturnFalse() {
        // Arrange: Create an instance of a ReadablePartial implementation (YearMonth)
        // and an object of a completely different, unrelated type (Locale).
        YearMonth partial = YearMonth.now();
        Object otherObject = Locale.GERMANY;

        // Act: Call the equals method to compare the two objects.
        boolean result = partial.equals(otherObject);

        // Assert: The result must be false, as a partial cannot be equal to an
        // object that is not a ReadablePartial.
        assertFalse("A partial should not be equal to an object of a different type.", result);
    }
}