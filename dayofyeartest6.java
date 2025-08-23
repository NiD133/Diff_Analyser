package org.threeten.extra;

import org.junit.Test;
import java.time.LocalDate;
import java.time.temporal.TemporalQuery;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * This test suite focuses on the {@link DayOfYear#query(TemporalQuery)} method,
 * ensuring it behaves as a proper delegation mechanism.
 */
public class DayOfYearQueryTest {

    /**
     * Tests that the query(TemporalQuery) method correctly delegates the query
     * operation to the provided TemporalQuery instance.
     *
     * <p>The test ensures that the result from {@code dayOfYear.query(mockQuery)} is the
     * exact object returned by the mock's {@code queryFrom} method, confirming that
     * DayOfYear does not alter the result.
     */
    @Test
    public void queryShouldDelegateToTheProvidedTemporalQuery() {
        // Arrange: Set up the test objects and define the mock's behavior.
        DayOfYear dayOfYear = DayOfYear.of(150); // The 150th day of the year.
        LocalDate expectedResult = LocalDate.of(2024, 5, 29); // An arbitrary, concrete date to act as the query result.

        // Create a mock TemporalQuery.
        // We configure it to return our expectedResult when its 'queryFrom' method
        // is called with the 'dayOfYear' instance.
        @SuppressWarnings("unchecked") // Necessary for mocking a generic type like TemporalQuery.
        TemporalQuery<LocalDate> mockQuery = mock(TemporalQuery.class);
        when(mockQuery.queryFrom(dayOfYear)).thenReturn(expectedResult);

        // Act: Call the method under test.
        LocalDate actualResult = dayOfYear.query(mockQuery);

        // Assert: Verify that the method returned the exact object from the mock.
        // This confirms that the 'query' method correctly delegated the call without
        // modification.
        assertSame(
                "The query method should return the exact result from the TemporalQuery's queryFrom method.",
                expectedResult,
                actualResult
        );
    }
}