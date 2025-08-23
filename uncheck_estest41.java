package org.apache.commons.io.function;

import org.junit.Test;

import java.io.IOException;
import java.io.UncheckedIOException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for the {@link Uncheck#getAsBoolean(IOBooleanSupplier)} method.
 */
public class UncheckGetAsBooleanTest {

    /**
     * Tests that getAsBoolean returns true when the underlying supplier returns true.
     */
    @Test
    public void testGetAsBooleanShouldReturnTrueWhenSupplierReturnsTrue() throws IOException {
        // Arrange: Create a mock IOBooleanSupplier that returns true.
        final IOBooleanSupplier mockSupplier = mock(IOBooleanSupplier.class);
        when(mockSupplier.getAsBoolean()).thenReturn(true);

        // Act: Call the method under test.
        final boolean result = Uncheck.getAsBoolean(mockSupplier);

        // Assert: The result should be true.
        assertTrue(result);
    }

    /**
     * Tests that getAsBoolean returns false when the underlying supplier returns false.
     * This is the refactored version of the original test case.
     */
    @Test
    public void testGetAsBooleanShouldReturnFalseWhenSupplierReturnsFalse() throws IOException {
        // Arrange: Create a mock IOBooleanSupplier that returns false.
        final IOBooleanSupplier mockSupplier = mock(IOBooleanSupplier.class);
        when(mockSupplier.getAsBoolean()).thenReturn(false);

        // Act: Call the method under test.
        final boolean result = Uncheck.getAsBoolean(mockSupplier);

        // Assert: The result should be false.
        assertFalse(result);
    }

    /**
     * Tests that getAsBoolean wraps an IOException from the supplier in an UncheckedIOException.
     */
    @Test
    public void testGetAsBooleanShouldThrowUncheckedIOExceptionWhenSupplierThrowsIOException() throws IOException {
        // Arrange: Create a mock IOBooleanSupplier that throws an IOException.
        final IOBooleanSupplier mockSupplier = mock(IOBooleanSupplier.class);
        final IOException ioException = new IOException("Test I/O failure");
        when(mockSupplier.getAsBoolean()).thenThrow(ioException);

        // Act & Assert: Verify that calling the method results in an UncheckedIOException.
        assertThrows(UncheckedIOException.class, () -> Uncheck.getAsBoolean(mockSupplier));
    }
}