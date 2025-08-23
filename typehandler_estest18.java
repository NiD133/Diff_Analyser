package org.apache.commons.cli;

import org.junit.Test;

/**
 * Tests for the {@link TypeHandler} class.
 * This test case specifically verifies the behavior of the deprecated {@code createFiles} method.
 */
public class TypeHandler_ESTestTest18 {

    /**
     * Verifies that the deprecated createFiles method consistently throws an
     * UnsupportedOperationException, as its functionality was never implemented.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void createFilesShouldThrowUnsupportedOperationException() {
        // The createFiles method is deprecated and its Javadoc states it is not implemented.
        // This test confirms that it throws the expected exception regardless of the input.
        TypeHandler.createFiles("any-string-value");
    }
}