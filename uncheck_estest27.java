package org.apache.commons.io.function;

import org.junit.Test;

/**
 * Tests for the {@link Uncheck} utility class.
 */
public class UncheckTest {

    /**
     * Verifies that calling {@link Uncheck#getAsLong(IOLongSupplier)} with a null
     * supplier throws a {@link NullPointerException}.
     */
    @Test(expected = NullPointerException.class)
    public void getAsLong_withNullSupplier_throwsNullPointerException() {
        Uncheck.getAsLong(null);
    }
}