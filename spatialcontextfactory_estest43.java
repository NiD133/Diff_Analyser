package org.locationtech.spatial4j.context;

import org.junit.Test;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.assertFalse;

/**
 * Unit tests for {@link SpatialContextFactory}.
 */
public class SpatialContextFactoryTest {

    /**
     * Tests that creating a non-geodetic (i.e., planar) SpatialContext results in
     * the 'normWrapLongitude' property being false by default.
     */
    @Test
    public void makeSpatialContext_withGeoSetToFalse_shouldDefaultToNormWrapLongitudeFalse() {
        // Arrange: Create configuration arguments for a non-geodetic context.
        // The 'geo' property, when set to "false", configures a planar context.
        Map<String, String> config = new HashMap<>();
        config.put("geo", "false");

        ClassLoader classLoader = ClassLoader.getSystemClassLoader();

        // Act: Create the SpatialContext using the factory with the specified configuration.
        SpatialContext spatialContext = SpatialContextFactory.makeSpatialContext(config, classLoader);

        // Assert: Verify that normWrapLongitude is false, which is the expected default
        // for non-geodetic contexts when not explicitly overridden.
        assertFalse(
            "normWrapLongitude should be false by default for a non-geodetic context",
            spatialContext.isNormWrapLongitude()
        );
    }
}