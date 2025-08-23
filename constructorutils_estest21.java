package org.apache.commons.lang3.reflect;

import org.junit.Test;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;

/**
 * This test verifies the behavior of the ConstructorUtils class.
 * The original test was auto-generated and has been rewritten for clarity.
 */
public class ConstructorUtilsTest {

    /**
     * Tests that {@link ConstructorUtils#invokeConstructor(Class, Object[], Class[])}
     * throws a {@link NoSuchMethodException} when attempting to instantiate an interface.
     *
     * <p>This is expected because interfaces do not have constructors. The test uses
     * {@link Annotation} as a standard example of an interface.</p>
     */
    @Test(expected = NoSuchMethodException.class)
    public void invokeConstructorShouldThrowNoSuchMethodExceptionWhenClassIsAnInterface()
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        // Arrange: Define the class to instantiate as an interface.
        final Class<Annotation> interfaceToInstantiate = Annotation.class;

        // Act: Attempt to invoke a constructor on the interface. Since an interface has
        // no constructors, this call is expected to fail. The specific arguments and
        // parameter types are irrelevant, so empty arrays are used for simplicity.
        ConstructorUtils.invokeConstructor(interfaceToInstantiate, new Object[0], new Class<?>[0]);

        // Assert: The test is successful if a NoSuchMethodException is thrown,
        // as specified by the @Test(expected=...) annotation.
    }
}