package org.joda.time.base;

import org.joda.time.ReadablePartial;
import org.joda.time.YearMonth;
import org.junit.Test;

/**
 * This test class focuses on the behavior of the {@link AbstractPartial#compareTo(ReadablePartial)} method.
 */
public class AbstractPartialTest {

    /**
     * Verifies that compareTo(null) throws a NullPointerException as per the method's contract.
     */
    @Test(expected = NullPointerException.class)
    public void compareTo_whenComparingWithNull_shouldThrowNullPointerException() {
        // Arrange: Create an instance of a concrete subclass of AbstractPartial.
        // YearMonth is used here as a representative example.
        ReadablePartial partial = YearMonth.now();

        // Act: Attempt to compare the partial to null.
        // Assert: The @Test(expected) annotation asserts that a NullPointerException is thrown.
        partial.compareTo(null);
    }
}