package org.joda.time.base;

import org.joda.time.YearMonth;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the abstract behavior of {@link AbstractPartial}.
 */
public class AbstractPartialTest {

    /**
     * Verifies that calling toString() with a null formatter
     * correctly falls back to the default toString() implementation, as per the contract.
     */
    @Test
    public void toStringWithNullFormatterShouldReturnDefaultStringRepresentation() {
        // Arrange: Create a concrete instance of AbstractPartial for testing.
        // Using a fixed value (e.g., May 2023) makes the test deterministic and repeatable.
        YearMonth yearMonth = new YearMonth(2023, 5);
        String expectedDefaultString = yearMonth.toString(); // e.g., "2023-05"

        // Act: Call the method under test with a null formatter.
        String actualString = yearMonth.toString((DateTimeFormatter) null);

        // Assert: The result should be identical to the output of the default toString() method.
        assertEquals(expectedDefaultString, actualString);
    }
}