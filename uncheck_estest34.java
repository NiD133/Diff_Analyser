package org.apache.commons.io.function;

import static org.junit.Assert.assertThrows;

import org.junit.Test;

/**
 * Tests for the {@link Uncheck} utility class.
 */
public class UncheckTest {

    /**
     * Tests that {@link Uncheck#apply(IOBiFunction, Object, Object)} throws a
     * {@link NullPointerException} when the function argument is null.
     */
    @Test
    public void applyWithNullBiFunctionShouldThrowNullPointerException() {
        // The Uncheck.apply method is expected to delegate to the provided function.
        // If the function itself is null, a NullPointerException should be thrown
        // before any I/O operation can be attempted.
        assertThrows(NullPointerException.class, () -> {
            // The arguments "input1" and "input2" are placeholders; their values do not affect this test.
            Uncheck.apply((IOBiFunction<String, String, String>) null, "input1", "input2");
        });
    }
}