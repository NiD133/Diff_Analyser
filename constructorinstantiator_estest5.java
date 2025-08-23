package org.mockito.internal.creation.instance;

import org.junit.Test;
import org.mockito.creation.instance.InstantiationException;
import org.mockito.creation.instance.Instantiator;

import static org.junit.Assert.fail;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for {@link ConstructorInstantiator}.
 */
public class ConstructorInstantiatorTest {

    /**
     * This test verifies that the ConstructorInstantiator throws an InstantiationException
     * when it is asked to create an instance of a class for which no constructor
     * matches the provided arguments.
     */
    @Test
    public void newInstance_shouldThrowException_whenNoConstructorMatchesArguments() {
        // Arrange: Create an instantiator with arguments that do not match any
        // constructor of the target class. The java.lang.Object class only has a
        // default, no-argument constructor.
        Object[] constructorArgs = new Object[]{"some-argument"};
        Instantiator instantiator = new ConstructorInstantiator(false, constructorArgs);
        Class<Object> classToInstantiate = Object.class;

        // Act & Assert: We expect an InstantiationException because no constructor
        // in Object.class accepts a single argument.
        try {
            instantiator.newInstance(classToInstantiate);
            fail("Expected an InstantiationException to be thrown, but the method completed successfully.");
        } catch (InstantiationException e) {
            // Success: The expected exception was caught.
            // For a more robust test, we can verify the exception message.
            String expectedMessagePart = "Unable to create instance of 'Object'";
            assertTrue(
                "Exception message should indicate which class could not be instantiated.",
                e.getMessage().contains(expectedMessagePart)
            );
        } catch (Exception e) {
            fail("Expected an InstantiationException, but a different exception was thrown: " + e.getClass().getName());
        }
    }
}