package org.locationtech.spatial4j.context;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for {@link SpatialContextFactory}.
 * This class contains a more readable version of the original generated test.
 */
public class SpatialContextFactoryTest {

    /**
     * Verifies that creating a SpatialContext with the "haversine" distance calculator
     * correctly configures a geographic context (isGeo=true) with default settings.
     * The haversine formula is used for calculations on a sphere, implying a geographic context.
     */
    @Test
    public void makeSpatialContext_withHaversineCalculator_shouldCreateGeographicContext() {
        // Arrange: Define configuration arguments to specify the "haversine" distance calculator.
        Map<String, String> args = new HashMap<>();
        args.put("distCalculator", "haversine");
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();

        // Act: Create the SpatialContext using the factory with the specified arguments.
        SpatialContext spatialContext = SpatialContextFactory.makeSpatialContext(args, classLoader);

        // Assert: Check that the resulting context is geographic and has the expected default properties.
        assertTrue("The context should be geographic when using the haversine calculator.",
                spatialContext.isGeo());
        assertFalse("The 'normWrapLongitude' property should be false by default.",
                spatialContext.isNormWrapLongitude());
    }
}