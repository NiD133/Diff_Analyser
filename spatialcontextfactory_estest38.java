package org.locationtech.spatial4j.context;

import org.junit.Test;
import org.locationtech.spatial4j.distance.GeodesicSphereDistCalc;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Test for {@link SpatialContextFactory}.
 */
public class SpatialContextFactoryTest {

    @Test
    public void makeSpatialContext_shouldUseDefaultSettings_whenPropertiesAreUnspecified() {
        // Arrange: Create configuration arguments specifying only the distance calculator.
        // Key properties like 'geo' and 'normWrapLongitude' are deliberately omitted
        // to test that the factory applies their default values correctly.
        Map<String, String> args = new HashMap<>();
        args.put("distCalculator", "vincentysphere");

        ClassLoader classLoader = ClassLoader.getSystemClassLoader();

        // Act: Create the SpatialContext using the factory.
        SpatialContext spatialContext = SpatialContextFactory.makeSpatialContext(args, classLoader);

        // Assert: Verify that the context was created with the correct default settings
        // for the unspecified properties.
        assertTrue("Context should be geographic (geo=true) by default", spatialContext.isGeo());
        assertFalse("Longitude wrapping should be disabled by default", spatialContext.isNormWrapLongitude());

        // Also, verify that the explicitly configured property was set correctly.
        assertNotNull("The distance calculator should not be null", spatialContext.getDistCalc());
        assertTrue("The distance calculator should be an instance of VincentySphere",
                spatialContext.getDistCalc() instanceof GeodesicSphereDistCalc.Vincenty);
    }
}