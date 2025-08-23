package org.apache.commons.io.function;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Tests for the {@link Uncheck} utility class, focusing on the getAsInt method.
 */
public class UncheckTest {

    /**
     * Tests that {@link Uncheck#getAsInt(IOIntSupplier)} returns the value from the
     * supplier when the supplier executes successfully without throwing an IOException.
     */
    @Test
    public void getAsInt_shouldReturnValueFromSupplier_whenSupplierSucceeds() {
        // Arrange: Define the expected value and create a supplier that returns it.
        // Using a lambda is clearer and more concise than using a mock for this simple case.
        final int expectedValue = 0;
        final IOIntSupplier successfulSupplier = () -> expectedValue;

        // Act: Call the method under test with the successful supplier.
        final int actualValue = Uncheck.getAsInt(successfulSupplier);

        // Assert: Verify that the returned value is the one provided by the supplier.
        assertEquals(expectedValue, actualValue);
    }
}