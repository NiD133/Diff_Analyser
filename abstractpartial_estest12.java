package org.joda.time.base;

import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.YearMonth;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * A test suite for the {@link AbstractPartial} class.
 * This test focuses on the contract of the getField(int) method,
 * using YearMonth as a concrete implementation for verification.
 */
public class AbstractPartialTest {

    @Test
    public void getField_withValidIndex_shouldReturnCorrespondingDateTimeField() {
        // Arrange: Create a concrete implementation of AbstractPartial with a fixed value.
        // Using a fixed date (June 2023) makes the test deterministic.
        YearMonth yearMonth = new YearMonth(2023, 6);

        // Act & Assert for the first field (index 0)
        DateTimeField yearField = yearMonth.getField(0);

        assertNotNull("The DateTimeField for index 0 should not be null.", yearField);
        assertEquals("The field at index 0 should be of type 'year'.",
                     DateTimeFieldType.year(), yearField.getType());

        // Act & Assert for the second field (index 1)
        DateTimeField monthField = yearMonth.getField(1);

        assertNotNull("The DateTimeField for index 1 should not be null.", monthField);
        assertEquals("The field at index 1 should be of type 'monthOfYear'.",
                     DateTimeFieldType.monthOfYear(), monthField.getType());
    }
}