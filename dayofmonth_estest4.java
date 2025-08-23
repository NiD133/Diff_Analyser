package org.threeten.extra;

import org.junit.Test;
import java.time.temporal.TemporalQuery;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests for the query() method in the DayOfMonth class.
 */
public class DayOfMonthQueryTest {

    @Test
    public void query_shouldDelegateToTheProvidedQueryObjectAndReturnItsResult() {
        // Arrange: Create a DayOfMonth instance and a mock TemporalQuery.
        DayOfMonth dayOfMonth = DayOfMonth.of(21);

        // Use a specific type for the query result (e.g., String) for better type safety.
        @SuppressWarnings("unchecked")
        TemporalQuery<String> mockQuery = (TemporalQuery<String>) mock(TemporalQuery.class);

        String expectedResult = "TestQueryResult";
        // Configure the mock: when queryFrom is called with our DayOfMonth instance,
        // it should return our predefined result.
        when(mockQuery.queryFrom(dayOfMonth)).thenReturn(expectedResult);

        // Act: Call the method under test.
        String actualResult = dayOfMonth.query(mockQuery);

        // Assert: Verify the behavior.
        // 1. The method should return the result from the mock query.
        assertEquals("The query method should return the value from the TemporalQuery object.",
                expectedResult, actualResult);

        // 2. The method should have called the queryFrom method on the mock exactly once.
        verify(mockQuery).queryFrom(dayOfMonth);
    }
}