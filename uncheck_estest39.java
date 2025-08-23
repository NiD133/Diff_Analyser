package org.apache.commons.io.function;

import org.junit.Test;

/**
 * Tests for {@link Uncheck}.
 */
public class UncheckTest {

    /**
     * Tests that calling {@link Uncheck#getAsInt(IOIntSupplier)} with a null supplier
     * throws a {@link NullPointerException}. The underlying implementation is expected
     * to attempt to invoke a method on the null reference, causing the exception.
     */
    @Test(expected = NullPointerException.class)
    public void getAsIntShouldThrowNullPointerExceptionForNullSupplier() {
        // Call the method under test with a null argument, which is expected to fail.
        Uncheck.getAsInt(null);
    }
}