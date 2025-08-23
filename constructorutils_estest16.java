package org.apache.commons.lang3.reflect;

import org.junit.Test;

import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class ConstructorUtils_ESTestTest16 {

    /**
     * Tests that {@link ConstructorUtils#invokeConstructor(Class, Object[], Class[])}
     * wraps exceptions thrown by the underlying constructor in an {@link InvocationTargetException}.
     *
     * <p>This test specifically checks the scenario where a constructor is matched using a
     * {@code null} parameter type (which acts as a wildcard for any non-primitive type) and
     * is then invoked with a {@code null} argument, causing the constructor itself to throw
     * an exception.</p>
     */
    @Test
    public void invokeConstructorShouldWrapExceptionFromUnderlyingConstructor() {
        // Arrange
        // We want to invoke a constructor of the Integer class. The Integer(String) constructor
        // is known to throw a NumberFormatException when passed a null argument.
        final Class<Integer> classToInstantiate = Integer.class;
        final Object[] args = { null };

        // A 'null' in the parameterTypes array acts as a wildcard that matches any
        // non-primitive parameter. This will cause ConstructorUtils to find and match the
        // public Integer(String val) constructor.
        final Class<?>[] parameterTypes = { null };

        // Act & Assert
        // We expect the call to throw an InvocationTargetException, which is a wrapper
        // around the actual exception thrown by the constructor.
        InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
            ConstructorUtils.invokeConstructor(classToInstantiate, args, parameterTypes);
        });

        // Verify that the cause of the InvocationTargetException is the NumberFormatException
        // from the Integer(String) constructor, confirming the correct exception was wrapped.
        assertTrue(thrown.getCause() instanceof NumberFormatException);
    }
}