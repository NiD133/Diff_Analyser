package org.locationtech.spatial4j.context;

import org.junit.Test;

import java.util.Collections;
import java.util.Map;

import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link SpatialContextFactory}.
 */
public class SpatialContextFactoryTest {

    @Test
    public void makeSpatialContext_withEmptyConfig_createsGeographicContextByDefault() {
        // Arrange
        // Use an empty map to signify that no specific configuration is provided.
        Map<String, String> emptyConfig = Collections.emptyMap();

        // Act
        // Create a SpatialContext using the factory with the empty configuration.
        SpatialContext context = SpatialContextFactory.makeSpatialContext(emptyConfig, null);

        // Assert
        // The factory should default to a geographic context (geo=true) when not specified.
        assertTrue("Context should be geographic by default", context.isGeo());
    }
}