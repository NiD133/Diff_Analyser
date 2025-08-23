package org.apache.commons.io.function;

import java.util.function.Supplier;
import org.junit.Test;

/**
 * Tests for {@link Uncheck}.
 */
public class UncheckTest {

    /**
     * Tests that {@link Uncheck#get(IOSupplier, Supplier)} throws a
     * NullPointerException when the IOSupplier argument is null.
     */
    @Test(expected = NullPointerException.class)
    public void testGetWithNullIOSupplierThrowsNullPointerException() {
        // The call should fail because the first argument (the supplier) is null.
        Uncheck.get((IOSupplier<String>) null, (Supplier<String>) null);
    }
}