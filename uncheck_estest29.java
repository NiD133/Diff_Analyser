package org.apache.commons.io.function;

import static org.junit.Assert.assertThrows;

import org.junit.Test;

/**
 * Tests for {@link Uncheck}.
 */
public class UncheckTest {

    /**
     * Tests that calling {@link Uncheck#getAsBoolean(IOBooleanSupplier)} with a null
     * supplier throws a {@link NullPointerException}.
     */
    @Test
    public void getAsBoolean_withNullSupplier_throwsNullPointerException() {
        // The method under test is expected to throw a NullPointerException
        // when its argument is null.
        assertThrows(NullPointerException.class, () -> Uncheck.getAsBoolean(null));
    }
}