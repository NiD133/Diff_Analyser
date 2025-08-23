package org.apache.commons.lang3.reflect;

import org.junit.Test;
import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * This test class contains tests for {@link ConstructorUtils}.
 * The original name {@code ConstructorUtils_ESTestTest22} suggests it was
 * auto-generated, and would typically be renamed to {@code ConstructorUtilsTest}.
 */
public class ConstructorUtils_ESTestTest22 extends ConstructorUtils_ESTest_scaffolding {

    /**
     * Tests that {@code invokeConstructor} throws an {@link InvocationTargetException}
     * when the underlying constructor itself throws an exception.
     *
     * <p>This test calls {@code invokeConstructor} for the {@code Integer} class with a single
     * {@code null} argument. The utility is expected to match the {@code Integer(String)}
     * constructor. However, invoking {@code new Integer(null)} throws a
     * {@code NumberFormatException}, which the reflection API wraps in an
     * {@code InvocationTargetException}.</p>
     */
    @Test
    public void invokeConstructorWithNullArgumentShouldThrowInvocationTargetException() {
        // Arrange: Define the target class and the null argument.
        // The original argument creation was overly complex and has been simplified.
        final Object[] args = { null };
        final Class<Integer> classToInstantiate = Integer.class;

        // Act & Assert: Verify that the expected exception is thrown.
        // Using assertThrows is clearer than a try-catch-fail block.
        final InvocationTargetException thrown = assertThrows(
            InvocationTargetException.class,
            () -> ConstructorUtils.invokeConstructor(classToInstantiate, args)
        );

        // Further Assert: Check that the cause of the exception is correct.
        // This makes the test more specific and robust.
        assertTrue(
            "The cause of the InvocationTargetException should be a NumberFormatException.",
            thrown.getCause() instanceof NumberFormatException
        );
    }
}