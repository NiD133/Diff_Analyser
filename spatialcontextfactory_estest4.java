package org.locationtech.spatial4j.context;

import org.junit.Test;

import java.util.Collections;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for {@link SpatialContextFactory}.
 */
public class SpatialContextFactoryTest {

    /**
     * Verifies that a SpatialContextFactory instance is configured with its default
     * values when initialized with an empty arguments map.
     */
    @Test
    public void factoryShouldHaveDefaultSettingsWhenInitializedWithEmptyArgs() {
        // Arrange: Create a factory and an empty configuration map.
        SpatialContextFactory factory = new SpatialContextFactory();
        Map<String, String> emptyArgs = Collections.emptyMap();
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();

        // Act: Initialize the factory with the empty configuration.
        factory.init(emptyArgs, classLoader);
        factory.initWorldBounds();

        // Assert: Verify that the factory's properties retain their default values.
        assertTrue("The 'geo' property should default to true.", factory.geo);
        assertFalse("The 'normWrapLongitude' property should default to false.", factory.normWrapLongitude);
        assertFalse("The 'hasFormatConfig' property should default to false.", factory.hasFormatConfig);
    }
}