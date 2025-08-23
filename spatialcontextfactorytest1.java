package org.locationtech.spatial4j.context;

import org.junit.After;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link SpatialContextFactory}.
 */
public class SpatialContextFactoryTest {

    // The system property key used by the factory to find a custom factory class.
    private static final String CONTEXT_FACTORY_SYSTEM_PROPERTY = "SpatialContextFactory";

    /**
     * Ensures that the system property for the factory is cleared after each test,
     * preventing side effects between tests.
     */
    @After
    public void tearDown() {
        System.getProperties().remove(CONTEXT_FACTORY_SYSTEM_PROPERTY);
    }

    /**
     * Tests that calling {@link SpatialContextFactory#makeSpatialContext(Map, ClassLoader)}
     * with no arguments returns a default {@link SpatialContext#GEO} instance.
     */
    @Test
    public void makeSpatialContext_withEmptyArgs_shouldReturnDefaultGeoContext() {
        // Arrange
        // The expected context when no arguments are provided is the default GEO context.
        SpatialContext expectedContext = SpatialContext.GEO;

        // Act
        // Create a context with an empty map of arguments.
        SpatialContext actualContext = createContext();

        // Assert
        // The created context should be equivalent to the default GEO context.
        assertEquals("Context class should match the default",
                expectedContext.getClass(), actualContext.getClass());
        assertEquals("isGeo flag should match the default",
                expectedContext.isGeo(), actualContext.isGeo());
        assertEquals("Distance calculator should match the default",
                expectedContext.getDistCalc(), actualContext.getDistCalc());
        assertEquals("World bounds should match the default",
                expectedContext.getWorldBounds(), actualContext.getWorldBounds());
    }

    /**
     * Helper method to invoke the factory with a variable number of string arguments,
     * simulating the configuration process.
     *
     * @param keyValuePairs A sequence of key-value strings. Must be an even number.
     * @return A new {@link SpatialContext} configured with the given arguments.
     */
    private SpatialContext createContext(String... keyValuePairs) {
        if (keyValuePairs.length % 2 != 0) {
            throw new IllegalArgumentException("Arguments must be in key-value pairs.");
        }

        Map<String, String> args = new HashMap<>();
        for (int i = 0; i < keyValuePairs.length; i += 2) {
            String key = keyValuePairs[i];
            String val = keyValuePairs[i + 1];
            args.put(key, val);
        }
        return SpatialContextFactory.makeSpatialContext(args, getClass().getClassLoader());
    }
}