package org.joda.time.base;

import org.joda.time.DateTimeFieldType;
import org.joda.time.YearMonth;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * This test class focuses on the behavior of the AbstractPartial class.
 */
public class AbstractPartial_ESTestTest30 extends AbstractPartial_ESTest_scaffolding {

    /**
     * Tests that indexOf() returns -1 when the specified field type is not supported by the partial.
     */
    @Test
    public void indexOf_shouldReturnMinusOne_forUnsupportedFieldType() {
        // Arrange
        // A YearMonth is a concrete implementation of AbstractPartial that supports
        // only 'year' and 'monthOfYear' fields.
        YearMonth yearMonth = new YearMonth(2023, 4); // April 2023

        // The 'era' field is a valid DateTimeFieldType but is not supported by YearMonth.
        DateTimeFieldType unsupportedFieldType = DateTimeFieldType.era();

        // Act
        // Attempt to find the index of the unsupported field type.
        int actualIndex = yearMonth.indexOf(unsupportedFieldType);

        // Assert
        // The expected result is -1, as defined by the indexOf() contract for unsupported fields.
        assertEquals(-1, actualIndex);
    }
}