package org.joda.time.base;

import org.joda.time.YearMonth;
import org.junit.Test;

/**
 * Unit tests for the {@link AbstractPartial} class, focusing on index-based access.
 */
public class AbstractPartialTest {

    /**
     * Tests that calling getFieldType(int) with an index that is out of bounds
     * throws an ArrayIndexOutOfBoundsException.
     *
     * A YearMonth is used as a concrete implementation of AbstractPartial. It has two
     * fields (year and month), so valid indices are 0 and 1.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void getFieldType_withInvalidIndex_shouldThrowArrayIndexOutOfBoundsException() {
        // Arrange: Create a YearMonth instance. It has fields at index 0 (year) and 1 (month).
        YearMonth yearMonth = new YearMonth();
        int invalidIndex = 2; // Any index other than 0 or 1 is invalid.

        // Act: Attempt to access a field type at an index that does not exist.
        // The @Test(expected=...) annotation handles the assertion that an exception is thrown.
        yearMonth.getFieldType(invalidIndex);
    }
}