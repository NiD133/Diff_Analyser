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
     * Tests that {@link Uncheck#getAsLong(IOLongSupplier, Supplier)} correctly returns the value
     * from the underlying supplier when it executes without throwing an IOException.
     */
    @Test
    public void testGetAsLongReturnsValueFromSupplier() throws IOException {
        // Arrange
        final long expectedValue = 1139L;
        final IOLongSupplier mockIoLongSupplier = mock(IOLongSupplier.class);
        doReturn(expectedValue).when(mockIoLongSupplier).getAsLong();

        // The message supplier is null for this test case, as we are testing the
        // scenario where no exception is thrown.
        final Supplier<String> nullMessageSupplier = null;

        // Act
        final long actualValue = Uncheck.getAsLong(mockIoLongSupplier, nullMessageSupplier);

        // Assert
        assertEquals(expectedValue, actualValue);
    }
}