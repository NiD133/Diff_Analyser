package org.apache.commons.io.function;

import org.junit.Test;

import java.io.IOException;
import java.util.function.Supplier;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Tests for {@link Uncheck}.
 */
public class UncheckTest {

    /**
     * Tests that {@link Uncheck#getAsInt(IOIntSupplier, Supplier)} successfully returns the integer
     * from the underlying supplier when no IOException is thrown.
     */
    @Test
    public void testGetAsIntReturnsValueWhenSupplierSucceeds() throws IOException {
        // Arrange
        final int expectedValue = -1214;
        final IOIntSupplier mockIoIntSupplier = mock(IOIntSupplier.class);
        final Supplier<String> mockErrorMessageSupplier = mock(Supplier.class);

        // Configure the mock to return the expected value.
        // The 'throws IOException' is part of the IOIntSupplier#getAsInt signature.
        doReturn(expectedValue).when(mockIoIntSupplier).getAsInt();

        // Act
        final int actualValue = Uncheck.getAsInt(mockIoIntSupplier, mockErrorMessageSupplier);

        // Assert
        assertEquals(expectedValue, actualValue);

        // Verify that the IOIntSupplier was called.
        verify(mockIoIntSupplier).getAsInt();
        
        // Also verify that the error message supplier was never invoked,
        // as no exception occurred.
        verify(mockErrorMessageSupplier, never()).get();
    }
}