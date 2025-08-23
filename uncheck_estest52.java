package org.apache.commons.io.function;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.concurrent.ForkJoinTask;

import org.junit.Test;

/**
 * Contains tests for the {@link Uncheck} utility class.
 */
public class UncheckTest {

    /**
     * Tests that {@link Uncheck#get(IOSupplier)} correctly returns null when the
     * provided supplier returns null, without throwing an exception.
     *
     * @throws IOException Required for the mock setup, but not expected to be thrown in this test.
     */
    @Test
    public void get_shouldReturnNull_whenSupplierReturnsNull() throws IOException {
        // Arrange: Create a mock IOSupplier that is configured to return null.
        // The @SuppressWarnings is necessary because of type erasure with generic mocks.
        @SuppressWarnings("unchecked")
        final IOSupplier<ForkJoinTask<String>> mockSupplier = mock(IOSupplier.class);
        when(mockSupplier.get()).thenReturn(null);

        // Act: Call the method under test with the mock supplier.
        final ForkJoinTask<String> result = Uncheck.get(mockSupplier);

        // Assert: Verify that the result from Uncheck.get() is null, as provided by the mock.
        assertNull("Expected Uncheck.get() to return the null value from the supplier.", result);
    }
}