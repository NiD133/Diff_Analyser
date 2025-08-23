package org.joda.time.base;

import org.joda.time.DateTimeFieldType;
import org.joda.time.YearMonth;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link AbstractPartial} class, focusing on the indexOf method.
 */
public class AbstractPartialTest {

    @Test
    public void indexOf_shouldReturnCorrectIndexForSupportedField() {
        // Arrange
        // YearMonth is a concrete implementation of AbstractPartial.
        // Its fields are defined as [year, monthOfYear].
        YearMonth partial = new YearMonth();
        DateTimeFieldType monthOfYearType = DateTimeFieldType.monthOfYear();
        int expectedIndex = 1; // year is at index 0, monthOfYear is at index 1

        // Act
        int actualIndex = partial.indexOf(monthOfYearType);

        // Assert
        assertEquals("The index of monthOfYear should be 1", expectedIndex, actualIndex);
    }
}