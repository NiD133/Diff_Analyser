package org.apache.commons.io.function;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link Uncheck} utility class.
 */
class UncheckTest {

    /**
     * Tests that {@link Uncheck#get(IOSupplier)} throws a NullPointerException
     * when the provided supplier is null.
     */
    @Test
    void getWithNullSupplierShouldThrowNullPointerException() {
        // Define a null supplier with a specific type to ensure the correct
        // Uncheck.get() method overload is tested.
        final IOSupplier<String> nullSupplier = null;

        // Assert that a NullPointerException is thrown when Uncheck.get() is called
        assertThrows(NullPointerException.class, () -> Uncheck.get(nullSupplier));
    }
}