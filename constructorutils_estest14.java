package org.apache.commons.lang3.reflect;

import org.junit.Test;

import java.lang.NoSuchMethodException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for the {@link ConstructorUtils} class.
 * This class replaces the auto-generated test to improve clarity and maintainability.
 */
public class ConstructorUtilsTest {

    /**
     * Tests that {@link ConstructorUtils#invokeExactConstructor(Class, Object[], Class[])}
     * throws a {@link NoSuchMethodException} when called with a constructor signature
     * that does not exist on the target class.
     */
    @Test
    public void invokeExactConstructorShouldThrowExceptionForNonExistentConstructor() {
        // Arrange: Define a constructor signature that is guaranteed not to exist for the Integer class.
        // The Integer class has no public constructor that accepts nine parameters.
        // We use arrays of nulls to represent this non-existent signature.
        final Class<Integer> targetClass = Integer.class;
        final Class<?>[] nonExistentParameterTypes = new Class<?>[9];
        final Object[] args = new Object[9];

        // Act & Assert
        try {
            ConstructorUtils.invokeExactConstructor(targetClass, args, nonExistentParameterTypes);
            fail("Expected a NoSuchMethodException because no matching constructor exists.");
        } catch (final NoSuchMethodException e) {
            // This is the expected outcome.
            // We can also verify the exception message for more robust testing.
            final String expectedMessage = "No such accessible constructor on object: " + targetClass.getName();
            assertEquals(expectedMessage, e.getMessage());
        } catch (final Exception e) {
            // Fail the test if any other unexpected exception is thrown.
            fail("Caught an unexpected exception: " + e);
        }
    }
}