package org.apache.commons.io.function;

import java.util.function.Supplier;
import org.junit.Test;

/**
 * Tests for the {@link Uncheck} utility class.
 */
public class UncheckTest {

    /**
     * Tests that Uncheck.getAsInt() throws a NullPointerException when the
     * IOIntSupplier argument is null.
     */
    @Test(expected = NullPointerException.class)
    public void testGetAsIntWithNullSupplierShouldThrowNullPointerException() {
        // This call is expected to fail because the supplier argument is null.
        Uncheck.getAsInt((IOIntSupplier) null, (Supplier<String>) null);
    }
}