package org.locationtech.spatial4j.context;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for {@link SpatialContextFactory}.
 * This test case focuses on the initialization behavior with invalid arguments.
 */
public class SpatialContextFactoryTest {

    @Test
    public void init_withInvalidShapeFactoryClassName_shouldThrowRuntimeException() {
        // Arrange
        // Create configuration arguments for the SpatialContextFactory.
        // We provide a value for 'shapeFactoryClass' that is not a valid,
        // loadable, fully-qualified class name.
        Map<String, String> configArgs = new HashMap<>();
        configArgs.put("shapeFactoryClass", "shapeFactoryClass"); // This string is not a real class name.

        SpatialContextFactory factory = new SpatialContextFactory();
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();

        // Act & Assert
        try {
            // The init method attempts to load the class by its name, which should fail.
            factory.init(configArgs, classLoader);
            fail("Expected a RuntimeException because the shapeFactoryClass is invalid.");
        } catch (RuntimeException e) {
            // Verify that the exception has the expected message, confirming the cause of the failure.
            String expectedMessage = "Invalid value 'shapeFactoryClass' on field shapeFactoryClass of type class java.lang.Class";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}