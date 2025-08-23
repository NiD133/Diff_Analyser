package org.apache.commons.lang3.reflect;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.lang.reflect.InvocationTargetException;

/**
 * Test suite for {@link ConstructorUtils}.
 */
public class ConstructorUtilsTest {

    /**
     * Tests that invokeConstructor can create an instance of a class (Object)
     * by calling its default, no-argument constructor.
     */
    @Test
    public void testInvokeConstructorWithNoArguments() throws NoSuchMethodException,
            IllegalAccessException, InvocationTargetException, InstantiationException {
        // Arrange: The class to instantiate is Object.class, which has a public no-arg constructor.
        final Class<Object> classToInstantiate = Object.class;

        // Act: Invoke the constructor with no arguments.
        final Object createdInstance = ConstructorUtils.invokeConstructor(classToInstantiate);

        // Assert: The method should successfully return a new, non-null instance of Object.
        assertNotNull("The created instance should not be null.", createdInstance);
        assertEquals("The created instance should be of type Object.",
                Object.class, createdInstance.getClass());
    }
}