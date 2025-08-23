package org.apache.commons.io.function;

import static org.junit.Assert.assertEquals;

import java.util.function.Supplier;
import org.junit.Test;

/**
 * Tests for the {@link Uncheck} utility class, focusing on the getAsLong method.
 */
public class UncheckTest {

    /**
     * Tests that {@link Uncheck#getAsLong(IOLongSupplier, Supplier)} successfully
     * returns the value from the given supplier when it does not throw an IOException.
     */
    @Test
    public void testGetAsLongReturnsValueWhenSupplierSucceeds() {
        // Arrange: Define the expected value and create a supplier that returns it.
        final long expectedValue = 0L;
        final IOLongSupplier successfulSupplier = () -> expectedValue;

        // Act: Call the method under test.
        // The message supplier is null because it's not used in the success path.
        final long actualValue = Uncheck.getAsLong(successfulSupplier, null);

        // Assert: Verify that the returned value matches the expected value.
        assertEquals(expectedValue, actualValue);
    }
}