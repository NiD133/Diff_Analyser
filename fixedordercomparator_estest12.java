package org.apache.commons.collections4.comparators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;
import org.junit.Test;

/**
 * Tests for {@link FixedOrderComparator}.
 * This test focuses on the constructor's handling of null input.
 */
public class FixedOrderComparator_ESTestTest12 { // Note: Class name kept for consistency with the original file.

    /**
     * Tests that the constructor throws a NullPointerException when initialized with a null list.
     * The constructor should reject null inputs to ensure valid state.
     */
    @Test
    public void constructorWithNullListShouldThrowNullPointerException() {
        try {
            // Act: Attempt to create a comparator with a null list of items.
            new FixedOrderComparator<>((List<String>) null);
            fail("Expected a NullPointerException to be thrown for a null list.");
        } catch (final NullPointerException e) {
            // Assert: Verify that the exception message clearly indicates the cause.
            // The source class uses Objects.requireNonNull(items, "items"), so "items" is the expected message.
            assertEquals("The exception message should identify the null parameter.", "items", e.getMessage());
        }
    }
}