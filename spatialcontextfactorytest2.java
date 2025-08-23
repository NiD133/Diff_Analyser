package org.locationtech.spatial4j.context;

import org.locationtech.spatial4j.context.jts.JtsSpatialContextFactory;
import org.locationtech.spatial4j.distance.CartesianDistCalc;
import org.locationtech.spatial4j.distance.GeodesicSphereDistCalc;
import org.locationtech.spatial4j.shape.Rectangle;
import org.locationtech.spatial4j.shape.impl.RectangleImpl;
import org.junit.After;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link SpatialContextFactory} focusing on creating contexts from string arguments.
 */
public class SpatialContextFactoryTest {

    // A system property that can be used to specify the factory class.
    // The tearDown method ensures this property is cleared after each test.
    public static final String PROP = "SpatialContextFactory";

    @After
    public void tearDown() {
        System.getProperties().remove(PROP);
    }

    /**
     * Tests that the factory correctly creates a non-geodetic (planar) context
     * when "geo" is set to "false".
     */
    @Test
    public void factory_withGeoFalse_createsNonGeodeticContext() {
        // Arrange
        Map<String, String> args = buildArgs("geo", "false");

        // Act
        SpatialContext context = SpatialContextFactory.makeSpatialContext(args, getClass().getClassLoader());

        // Assert
        assertFalse("Context should be non-geodetic", context.isGeo());
        assertEquals("Default calculator for non-geo should be Cartesian",
                new CartesianDistCalc(), context.getDistCalc());
    }

    /**
     * Tests that the factory correctly configures a non-geodetic context with
     * a custom distance calculator and world bounds.
     */
    @Test
    public void factory_withCustomNonGeodeticSettings_createsCorrectContext() {
        // Arrange
        Map<String, String> args = buildArgs(
                "geo", "false",
                "distCalculator", "cartesian^2",
                "worldBounds", "ENVELOPE(-100, 75, 200, 0)" // xMin, xMax, yMax, yMin
        );

        // Act
        SpatialContext context = SpatialContextFactory.makeSpatialContext(args, getClass().getClassLoader());

        // Assert
        assertFalse("Context should be non-geodetic", context.isGeo());
        assertEquals("Calculator should be Cartesian squared",
                new CartesianDistCalc(true), context.getDistCalc());

        Rectangle expectedBounds = new RectangleImpl(-100, 75, 0, 200, context);
        assertEquals("World bounds should be parsed from ENVELOPE string",
                expectedBounds, context.getWorldBounds());
    }

    /**
     * Tests that the factory correctly configures a geodetic context with a
     * specific distance calculator.
     */
    @Test
    public void factory_withCustomGeodeticSettings_createsCorrectContext() {
        // Arrange
        Map<String, String> args = buildArgs(
                "geo", "true",
                "distCalculator", "lawOfCosines"
        );

        // Act
        SpatialContext context = SpatialContextFactory.makeSpatialContext(args, getClass().getClassLoader());

        // Assert
        assertTrue("Context should be geodetic", context.isGeo());
        assertEquals("Calculator should be LawOfCosines",
                new GeodesicSphereDistCalc.LawOfCosines(), context.getDistCalc());
    }

    /**
     * Helper method to construct a map of arguments from key-value pairs.
     *
     * @param keyValuePairs A sequence of key-value strings. Must be even in number.
     * @return A map containing the provided arguments.
     */
    private Map<String, String> buildArgs(String... keyValuePairs) {
        if (keyValuePairs.length % 2 != 0) {
            throw new IllegalArgumentException("Key-value pairs must be even.");
        }
        Map<String, String> args = new HashMap<>();
        for (int i = 0; i < keyValuePairs.length; i += 2) {
            args.put(keyValuePairs[i], keyValuePairs[i + 1]);
        }
        return args;
    }
}