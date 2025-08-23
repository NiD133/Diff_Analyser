package org.apache.commons.io.function;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.function.Supplier;
import java.util.stream.LongStream;

import org.junit.Test;

/**
 * Tests for the {@link Uncheck} utility class.
 */
public class UncheckTest {

    /**
     * Tests that {@link Uncheck#get(IOSupplier, Supplier)} returns null
     * when the provided supplier successfully returns null.
     */
    @Test
    public void testGet_whenSupplierReturnsNull_shouldReturnNull() throws IOException {
        // Arrange: Create a mock IOSupplier that returns null without throwing an exception.
        @SuppressWarnings("unchecked")
        final IOSupplier<LongStream> nullReturningSupplier = mock(IOSupplier.class);
        when(nullReturningSupplier.get()).thenReturn(null);

        // Act: Call the method under test. The message supplier is irrelevant here.
        final LongStream result = Uncheck.get(nullReturningSupplier, (Supplier<String>) null);

        // Assert: The result should be the null value passed through from the supplier.
        assertNull("Expected Uncheck.get() to return null when the supplier provides a null value.", result);
    }
}