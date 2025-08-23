package org.joda.time.base;

import org.joda.time.DateTimeFieldType;
import org.joda.time.MonthDay;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test case for the {@link AbstractPartial#getFieldType(int)} method.
 * This test uses {@link MonthDay} as a concrete implementation of {@link AbstractPartial}.
 */
public class AbstractPartial_ESTestTest11 extends AbstractPartial_ESTest_scaffolding {

    /**
     * Tests that getFieldType(int) returns the correct field type for a given index.
     * For a MonthDay partial, index 0 corresponds to 'monthOfYear' and index 1 corresponds to 'dayOfMonth'.
     */
    @Test
    public void getFieldType_returnsDayOfMonthForIndex1() {
        // Arrange: A MonthDay is a partial with two fields. We create a sample instance.
        MonthDay monthDay = new MonthDay(6, 23); // June 23

        // Act: Retrieve the field type at the second position (index 1).
        DateTimeFieldType actualFieldType = monthDay.getFieldType(1);

        // Assert: The retrieved field type should be 'dayOfMonth'.
        DateTimeFieldType expectedFieldType = DateTimeFieldType.dayOfMonth();
        assertEquals(expectedFieldType, actualFieldType);
    }
}