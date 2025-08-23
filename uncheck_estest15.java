package org.apache.commons.io.function;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import org.junit.Test;

/**
 * Tests for {@link Uncheck#getAsBoolean(IOBooleanSupplier)}.
 */
public class UncheckTest {

    /**
     * Tests that Uncheck.getAsBoolean() correctly returns the value from the
     * underlying supplier when the supplier does not throw an exception.
     */
    @Test
    public void getAsBooleanShouldReturnValueFromSupplierWhenNoExceptionIsThrown() throws IOException {
        // Arrange: Create a mock IOBooleanSupplier that returns 'true'.
        final IOBooleanSupplier mockSupplier = mock(IOBooleanSupplier.class);
        when(mockSupplier.getAsBoolean()).thenReturn(true);

        // Act: Call the method under test with the mock supplier.
        final boolean result = Uncheck.getAsBoolean(mockSupplier);

        // Assert: Verify that the method returned the value from the supplier.
        assertTrue("The result should be true, as provided by the supplier.", result);
    }
}