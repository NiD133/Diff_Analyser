package org.apache.commons.io.function;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import org.junit.Test;

/**
 * Tests for {@link Uncheck}.
 */
public class UncheckTest {

    /**
     * Tests that {@link Uncheck#getAsLong(IOLongSupplier)} correctly returns the value
     * from the supplier when the underlying operation succeeds without an exception.
     */
    @Test
    public void testGetAsLongReturnsValueOnSuccess() throws IOException {
        // Arrange: Create a mock IOLongSupplier that returns a specific value.
        final IOLongSupplier mockSupplier = mock(IOLongSupplier.class);
        final long expectedValue = 0L;
        
        // Stub the getAsLong() method to return our expected value.
        // We add 'throws IOException' to the test method signature to allow this clean syntax.
        when(mockSupplier.getAsLong()).thenReturn(expectedValue);

        // Act: Call the method under test.
        final long actualValue = Uncheck.getAsLong(mockSupplier);

        // Assert: Verify that the returned value is the one we expect.
        assertEquals(expectedValue, actualValue);
    }
}