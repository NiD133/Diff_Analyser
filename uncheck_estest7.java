package org.apache.commons.io.function;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.util.function.Supplier;
import org.junit.Test;

/**
 * Tests for {@link Uncheck}.
 */
public class UncheckTest {

    /**
     * Tests that Uncheck.getAsLong() successfully returns the value from the
     * underlying IOLongSupplier when no IOException is thrown.
     */
    @Test
    public void getAsLongShouldReturnValueFromSupplierWhenNoExceptionIsThrown() throws IOException {
        // Arrange: Set up the test objects and expectations.
        final long expectedValue = -890L;
        final IOLongSupplier mockIoSupplier = mock(IOLongSupplier.class);

        // Configure the mock to return a specific value when its getAsLong() method is called.
        // The 'throws IOException' is necessary for the mock setup, as IOLongSupplier.getAsLong()
        // is declared to throw a checked IOException.
        doReturn(expectedValue).when(mockIoSupplier).getAsLong();

        // Act: Call the method under test.
        // This test case uses the overload with a message supplier, passing null
        // to ensure it's handled correctly in the success path.
        final long actualValue = Uncheck.getAsLong(mockIoSupplier, (Supplier<String>) null);

        // Assert: Verify the result is as expected.
        assertEquals(expectedValue, actualValue);
    }
}