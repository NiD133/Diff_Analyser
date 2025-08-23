package org.joda.time.base;

import org.joda.time.LocalDate;
import org.joda.time.ReadablePartial;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

/**
 * Test class for the toString(DateTimeFormatter) method in {@link AbstractPartial}.
 */
public class AbstractPartialToStringTest {

    @Test
    public void toStringWithFormatterShouldDelegateToFormatter() {
        // Arrange
        // Use a concrete implementation of AbstractPartial for the test.
        // A fixed date makes the test deterministic and easier to understand.
        ReadablePartial partial = new LocalDate(2023, 10, 27);
        DateTimeFormatter mockFormatter = mock(DateTimeFormatter.class);
        String expectedFormattedString = "2023-10-27-formatted";

        // Stub the formatter's print method to return a specific string for our partial.
        when(mockFormatter.print(partial)).thenReturn(expectedFormattedString);

        // Act
        // Call the method under test.
        String actualResult = partial.toString(mockFormatter);

        // Assert
        // Verify that the method returned the string from the formatter.
        assertEquals(expectedFormattedString, actualResult);

        // Also, verify that the formatter's print method was called correctly.
        verify(mockFormatter).print(partial);
    }
}