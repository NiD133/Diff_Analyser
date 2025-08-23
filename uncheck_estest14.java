package org.apache.commons.io.function;

import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for the {@link Uncheck} utility class.
 * This focuses on the getAsInt() method.
 */
public class UncheckTest {

    /**
     * Tests that Uncheck.getAsInt() correctly returns the integer value
     * provided by the IOIntSupplier when no exception is thrown.
     */
    @Test
    public void getAsInt_shouldReturnValue_whenSupplierSucceeds() throws IOException {
        // Arrange: Define the expected value and mock the supplier.
        final int expectedValue = -1830;
        final IOIntSupplier mockSupplier = mock(IOIntSupplier.class);

        // Configure the mock to return the expected value.
        // The 'throws IOException' is required by the compiler because the
        // getAsInt() method in the IOIntSupplier interface is declared to throw it.
        when(mockSupplier.getAsInt()).thenReturn(expectedValue);

        // Act: Call the method under test.
        final int actualValue = Uncheck.getAsInt(mockSupplier);

        // Assert: Verify that the returned value matches the expected value.
        assertEquals(expectedValue, actualValue);
    }
}