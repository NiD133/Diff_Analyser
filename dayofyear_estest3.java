package org.threeten.extra;

import org.junit.Test;
import java.time.temporal.TemporalQuery;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the query() method in {@link DayOfYear}.
 */
public class DayOfYearQueryTest {

    @Test
    public void query_delegatesToTemporalQueryAndReturnsResult() {
        // This test verifies that the query(TemporalQuery) method correctly
        // delegates the call to the query's queryFrom() method and returns its result.

        // Arrange
        DayOfYear dayOfYear = DayOfYear.of(46);
        String expectedResult = "TestQueryResult";

        // Mock the TemporalQuery functional interface
        @SuppressWarnings("unchecked")
        TemporalQuery<String> mockQuery = (TemporalQuery<String>) mock(TemporalQuery.class);
        when(mockQuery.queryFrom(dayOfYear)).thenReturn(expectedResult);

        // Act
        String actualResult = dayOfYear.query(mockQuery);

        // Assert
        // 1. Check that the result from the query is returned correctly.
        assertEquals(expectedResult, actualResult);

        // 2. Verify that the queryFrom method was called exactly once with the correct DayOfYear instance.
        verify(mockQuery, times(1)).queryFrom(dayOfYear);
    }
}