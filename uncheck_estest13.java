package org.apache.commons.io.function;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link Uncheck} utility class.
 */
public class UncheckTest {

    /**
     * Tests that Uncheck.getAsInt() successfully returns the integer value
     * from a given IOIntSupplier when no exception is thrown.
     */
    @Test
    public void testGetAsIntReturnsValueFromSupplier() {
        // Arrange
        final int expectedValue = 1165;
        final IOIntSupplier supplier = () -> expectedValue;

        // Act
        final int actualValue = Uncheck.getAsInt(supplier);

        // Assert
        assertEquals(expectedValue, actualValue);
    }
}