package org.apache.commons.lang3.reflect;

import org.junit.Test;
import static org.junit.Assert.fail;

/**
 * Unit tests for {@link org.apache.commons.lang3.reflect.ConstructorUtils}.
 */
public class ConstructorUtilsTest {

    /**
     * Tests that {@link ConstructorUtils#invokeConstructor(Class, Object[], Class[])}
     * throws an IllegalArgumentException when the number of provided arguments does not
     * match the number of specified parameter types.
     */
    @Test
    public void invokeConstructorWithMismatchedArgumentCountThrowsIllegalArgumentException() {
        // Arrange: We will attempt to invoke the Integer(String) constructor
        // but provide no arguments. This mismatch should cause an exception.
        final Class<Integer> targetClass = Integer.class;
        final Class<?>[] parameterTypes = {String.class};
        final Object[] args = {}; // An empty array, signifying zero arguments.

        // Act & Assert: The call should fail with an IllegalArgumentException.
        try {
            ConstructorUtils.invokeConstructor(targetClass, args, parameterTypes);
            fail("Expected an IllegalArgumentException to be thrown due to argument count mismatch.");
        } catch (final IllegalArgumentException expected) {
            // The expected exception was thrown. The test passes.
        } catch (final Exception e) {
            fail("Expected IllegalArgumentException, but caught " + e.getClass().getName());
        }
    }
}