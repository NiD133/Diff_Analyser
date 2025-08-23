package org.joda.time.base;

import org.joda.time.YearMonth;
import org.junit.Test;

/**
 * Unit tests for the {@link AbstractPartial} class.
 */
public class AbstractPartialTest {

    /**
     * Verifies that calling getField() with a negative index throws an IndexOutOfBoundsException.
     * This behavior is defined in the contract of AbstractPartial and is tested here using
     * YearMonth as a concrete implementation.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void getField_withNegativeIndex_shouldThrowIndexOutOfBoundsException() {
        // Arrange: Create a concrete instance of a partial to test the abstract behavior.
        YearMonth partial = YearMonth.now();
        int invalidIndex = -1;

        // Act: Attempt to access a field with an invalid, negative index.
        // This call is expected to throw the exception verified by the @Test annotation.
        partial.getField(invalidIndex);
    }
}