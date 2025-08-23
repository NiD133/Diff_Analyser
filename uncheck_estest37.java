package org.apache.commons.io.function;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import org.junit.Test;

/**
 * Tests for the {@link Uncheck} utility class.
 */
public class UncheckTest {

    /**
     * Tests that {@link Uncheck#getAsLong(IOLongSupplier)} returns the value from the
     * supplier when the supplier executes successfully without throwing an IOException.
     */
    @Test
    public void getAsLong_shouldReturnValueFromSupplier_whenNoExceptionIsThrown() throws IOException {
        // Arrange: Set up the test conditions and inputs.
        final long expectedValue = -2307L;
        
        // Create a mock IOLongSupplier.
        final IOLongSupplier mockSupplier = mock(IOLongSupplier.class);
        
        // Configure the mock to return a specific value when its getAsLong() method is called.
        when(mockSupplier.getAsLong()).thenReturn(expectedValue);

        // Act: Call the method under test.
        final long actualValue = Uncheck.getAsLong(mockSupplier);

        // Assert: Verify the result is as expected.
        assertEquals(expectedValue, actualValue);
    }
}