package org.apache.commons.lang3.reflect;

import org.junit.Test;

/**
 * Unit tests for {@link org.apache.commons.lang3.reflect.ConstructorUtils}.
 */
public class ConstructorUtilsTest {

    /**
     * Tests that {@link ConstructorUtils#invokeConstructor(Class, Object[], Class[])}
     * throws a NullPointerException if the target class is null.
     */
    @Test(expected = NullPointerException.class)
    public void testInvokeConstructorWithNullClassThrowsNPE() {
        // The method under test should throw a NullPointerException before it
        // processes the arguments or parameter types. Therefore, we can pass
        // empty arrays for them.
        ConstructorUtils.invokeConstructor(null, new Object[0], new Class<?>[0]);
    }
}