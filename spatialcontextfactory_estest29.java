package org.locationtech.spatial4j.context;

import org.junit.Test;
import org.locationtech.spatial4j.io.LegacyShapeWriter;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Tests for {@link SpatialContextFactory}.
 */
public class SpatialContextFactoryTest {

    /**
     * Tests that makeSpatialContext throws a RuntimeException when the provided
     * 'shapeFactoryClass' does not have a constructor that accepts a SpatialContext
     * and a SpatialContextFactory.
     */
    @Test
    public void makeSpatialContext_throwsException_forShapeFactoryWithInvalidConstructor() {
        // Arrange: Configure a ShapeFactory class that is known to lack the required constructor.
        // LegacyShapeWriter is a ShapeWriter, not a ShapeFactory, and serves as a good example
        // of a class that will fail the reflective constructor lookup.
        Map<String, String> config = new HashMap<>();
        config.put("shapeFactoryClass", LegacyShapeWriter.class.getName());

        ClassLoader classLoader = ClassLoader.getSystemClassLoader();

        // Act & Assert
        try {
            SpatialContextFactory.makeSpatialContext(config, classLoader);
            fail("Expected a RuntimeException because the specified shape factory class has an invalid constructor.");
        } catch (RuntimeException e) {
            // Verify that the exception message clearly explains the problem.
            String actualMessage = e.getMessage();
            assertTrue(
                "Exception message should mention the problematic class name.",
                actualMessage.contains(LegacyShapeWriter.class.getName())
            );
            assertTrue(
                "Exception message should indicate a missing constructor.",
                actualMessage.contains("needs a constructor that takes")
            );
        }
    }
}