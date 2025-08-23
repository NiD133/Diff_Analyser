package org.apache.commons.io.function;

import org.junit.Test;

/**
 * Tests for the {@link Uncheck} utility class.
 */
public class UncheckTest {

    /**
     * Tests that {@link Uncheck#apply(IOQuadFunction, Object, Object, Object, Object)}
     * throws a {@link NullPointerException} when the function argument is null.
     */
    @Test(expected = NullPointerException.class)
    public void applyWithNullQuadFunctionShouldThrowNullPointerException() {
        // The Uncheck.apply method is expected to delegate to the provided function.
        // If the function itself is null, a NullPointerException should be thrown,
        // consistent with standard Java behavior.
        Uncheck.apply((IOQuadFunction<String, String, String, String, String>) null, "arg1", "arg2", "arg3", "arg4");
    }
}