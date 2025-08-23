package org.apache.commons.lang3.reflect;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * This test case focuses on the behavior of the {@link ConstructorUtils#invokeConstructor(Class, Object...)}
 * method when provided with invalid arguments.
 */
public class ConstructorUtils_ESTestTest9 extends ConstructorUtils_ESTest_scaffolding {

    /**
     * Tests that {@code invokeConstructor} throws a {@link NullPointerException}
     * when the target class is null. The method should perform a null check on the
     * class argument before attempting any reflection.
     */
    @Test
    public void invokeConstructorShouldThrowNullPointerExceptionWhenClassIsNull() {
        // Arrange: Define arguments for the constructor call. The actual values are
        // irrelevant since the method should fail before using them.
        final Object[] arguments = new Object[7];

        // Act & Assert: Verify that calling invokeConstructor with a null class
        // results in a NullPointerException.
        final NullPointerException thrown = assertThrows(
                NullPointerException.class,
                () -> ConstructorUtils.invokeConstructor(null, arguments)
        );

        // Further Assert: Check the exception message to ensure it's the expected
        // one, which indicates the 'cls' parameter was the cause.
        assertEquals("cls", thrown.getMessage());
    }
}