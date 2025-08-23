package org.locationtech.spatial4j.context;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Test suite for {@link SpatialContextFactory}.
 * Note: The original test class extended an EvoSuite scaffolding class.
 * This has been removed for clarity, but may be necessary if it provides required setup.
 */
public class SpatialContextFactoryTest {

    /**
     * Tests that makeSpatialContext throws a RuntimeException when the provided
     * 'shapeFactoryClass' is invalid because it lacks the required constructor.
     *
     * The factory expects a class with a constructor that accepts a SpatialContext,
     * so providing a class without one should result in an instantiation error.
     */
    @Test
    public void makeSpatialContext_withShapeFactoryClassMissingRequiredConstructor_throwsRuntimeException() {
        // Arrange: Configure a shape factory class that is known to be invalid.
        // 'BufferedLineString' is a shape implementation, not a factory, and lacks
        // the constructor that SpatialContextFactory tries to invoke.
        Map<String, String> config = new HashMap<>();
        config.put("shapeFactoryClass", "org.locationtech.spatial4j.shape.impl.BufferedLineString");

        ClassLoader classLoader = ClassLoader.getSystemClassLoader();

        // Act & Assert
        try {
            SpatialContextFactory.makeSpatialContext(config, classLoader);
            fail("Expected a RuntimeException because the shape factory class is invalid.");
        } catch (RuntimeException e) {
            // Assert that the exception message clearly indicates the constructor problem.
            String expectedMessageContent = "needs a constructor that takes";
            assertTrue(
                    "Exception message should explain the missing constructor. Actual message: " + e.getMessage(),
                    e.getMessage().contains(expectedMessageContent)
            );
        }
    }
}