package org.joda.time.base;

import org.joda.time.DateTimeFieldType;
import org.joda.time.MonthDay;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link AbstractPartial} class.
 * <p>
 * This test suite uses {@link MonthDay}, a concrete implementation of AbstractPartial,
 * to verify the behavior of shared methods.
 */
public class AbstractPartialTest {

    /**
     * Verifies that the get() method correctly retrieves the value
     * for a supported field type.
     */
    @Test
    public void getShouldReturnCorrectValueForSupportedFieldType() {
        // Arrange: Create a partial for January 1st.
        // MonthDay is a concrete subclass of AbstractPartial.
        MonthDay partial = new MonthDay(1, 1); // January 1st
        DateTimeFieldType fieldToQuery = DateTimeFieldType.dayOfMonth();
        int expectedDay = 1;

        // Act: Retrieve the value of the day-of-month field.
        int actualDay = partial.get(fieldToQuery);

        // Assert: The retrieved value should match the expected value.
        assertEquals(expectedDay, actualDay);
    }
}