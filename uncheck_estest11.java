package org.apache.commons.io.function;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.function.Supplier;
import org.junit.Test;

/**
 * Tests for the {@link Uncheck} utility class.
 */
public class UncheckTest {

    /**
     * Tests that Uncheck.getAsInt() successfully returns the integer from the
     * underlying IOIntSupplier when no IOException is thrown.
     */
    @Test
    public void testGetAsIntReturnsValueOnSuccess() throws IOException {
        // Arrange
        final int expectedValue = 2726;
        final IOIntSupplier mockSupplier = mock(IOIntSupplier.class);

        // Configure the mock to return a specific value.
        // The 'throws IOException' is necessary for the mock setup.
        when(mockSupplier.getAsInt()).thenReturn(expectedValue);

        // Act
        // Call the method under test. The second argument is a message supplier,
        // which is intentionally null for this test case.
        final int actualValue = Uncheck.getAsInt(mockSupplier, (Supplier<String>) null);

        // Assert
        assertEquals("The returned value should match the one from the supplier.",
                expectedValue, actualValue);
    }
}