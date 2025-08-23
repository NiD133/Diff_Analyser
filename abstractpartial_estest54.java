package org.joda.time.base;

import org.joda.time.DateTimeFieldType;
import org.joda.time.LocalDate;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

/**
 * This test class contains tests for the AbstractPartial class,
 * verified through its concrete subclass, LocalDate.
 */
public class AbstractPartial_ESTestTest54 extends AbstractPartial_ESTest_scaffolding {

    /**
     * Verifies that getFieldTypes() on a LocalDate instance returns the correct
     * three fields (year, month, day) in the correct order.
     * <p>
     * This test implicitly checks the contract of AbstractPartial as implemented
     * by one of its primary subclasses.
     */
    @Test
    public void getFieldTypes_forLocalDate_returnsYearMonthAndDayFields() {
        // Arrange
        // A LocalDate is a partial date consisting of year, month, and day fields.
        LocalDate localDate = new LocalDate();
        DateTimeFieldType[] expectedFieldTypes = {
            DateTimeFieldType.year(),
            DateTimeFieldType.monthOfYear(),
            DateTimeFieldType.dayOfMonth()
        };

        // Act
        // Call the method under test.
        DateTimeFieldType[] actualFieldTypes = localDate.getFieldTypes();

        // Assert
        // Verify that the returned array of field types matches the expectation.
        assertArrayEquals(
            "LocalDate should have three fields: year, monthOfYear, and dayOfMonth.",
            expectedFieldTypes,
            actualFieldTypes
        );
    }
}