package org.locationtech.spatial4j.context;

import org.locationtech.spatial4j.context.jts.DatelineRule;
import org.locationtech.spatial4j.context.jts.JtsSpatialContext;
import org.locationtech.spatial4j.context.jts.JtsSpatialContextFactory;
import org.locationtech.spatial4j.context.jts.ValidationRule;
import org.locationtech.spatial4j.distance.CartesianDistCalc;
import org.locationtech.spatial4j.distance.GeodesicSphereDistCalc;
import org.locationtech.spatial4j.io.WKTReader;
import org.locationtech.spatial4j.shape.Rectangle;
import org.locationtech.spatial4j.shape.impl.RectangleImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests for configuring {@link JtsSpatialContext} via the {@link SpatialContextFactory}.
 */
public class JtsSpatialContextFactoryConfigTest {

    private static final String CONTEXT_FACTORY_SYS_PROP_NAME = "SpatialContextFactory";

    /**
     * A test-specific WKTReader to verify that the factory can load and instantiate custom classes.
     */
    public static class CustomWktShapeParser extends WKTReader {
        // This flag confirms that this custom class was instantiated by the factory.
        static boolean wasInstantiated = false;

        public CustomWktShapeParser(JtsSpatialContext ctx, JtsSpatialContextFactory factory) {
            super(ctx, factory);
            wasInstantiated = true;
        }
    }

    @Before
    public void setUp() {
        // Reset state before each test to ensure test isolation.
        CustomWktShapeParser.wasInstantiated = false;
    }

    @After
    public void tearDown() {
        // The factory can fall back to a system property. Clean it up to avoid side-effects.
        System.getProperties().remove(CONTEXT_FACTORY_SYS_PROP_NAME);
    }

    @Test
    public void factoryShouldCorrectlyConfigureAllPropertiesForGeoContext() {
        // GIVEN a map of configuration arguments for a geodetic JTS context
        Map<String, String> args = new HashMap<>();
        args.put("spatialContextFactory", JtsSpatialContextFactory.class.getName());
        args.put("geo", "true");
        args.put("normWrapLongitude", "true");
        args.put("precisionScale", "2.0");
        args.put("wktShapeParserClass", CustomWktShapeParser.class.getName());
        args.put("datelineRule", "ccwRect");
        args.put("validationRule", "repairConvexHull");
        args.put("autoIndex", "true");

        // WHEN a JtsSpatialContext is created from the arguments
        JtsSpatialContext ctx = (JtsSpatialContext) createContext(args);

        // THEN all properties should be configured correctly
        assertTrue("Context should be geodetic", ctx.isGeo());
        assertTrue("normWrapLongitude should be true", ctx.isNormWrapLongitude());
        assertTrue("Distance calculator should be for a geodetic context", ctx.getDistCalc() instanceof GeodesicSphereDistCalc);
        assertEquals("Precision scale should be 2.0", 2.0, ctx.getGeometryFactory().getPrecisionModel().getScale(), 0.0);
        assertTrue("CustomWktShapeParser should have been instantiated", CustomWktShapeParser.wasInstantiated);
        assertEquals("DatelineRule should be ccwRect", DatelineRule.ccwRect, ctx.getDatelineRule());
        assertEquals("ValidationRule should be repairConvexHull", ValidationRule.repairConvexHull, ctx.getValidationRule());
    }

    @Test
    public void factoryShouldConfigureNonGeoContextWithCustomWorldBounds() {
        // This test verifies a fix for issue #72, ensuring a non-geo context with
        // custom world bounds is configured correctly.

        // GIVEN a map of configuration arguments for a non-geodetic JTS context
        Map<String, String> args = new HashMap<>();
        args.put("spatialContextFactory", JtsSpatialContextFactory.class.getName());
        args.put("geo", "false");
        args.put("worldBounds", "ENVELOPE(-500, 500, 300, -300)"); // WKT ENVELOPE is (xMin, xMax, yMax, yMin)

        // WHEN a JtsSpatialContext is created from the arguments
        JtsSpatialContext ctx = (JtsSpatialContext) createContext(args);

        // THEN the world bounds and other non-geo properties should be configured correctly
        assertFalse("Context should not be geodetic", ctx.isGeo());
        assertTrue("Distance calculator should be Cartesian", ctx.getDistCalc() instanceof CartesianDistCalc);

        Rectangle expectedBounds = new RectangleImpl(-500, 500, -300, 300, ctx);
        assertEquals("Custom world bounds should be set correctly", expectedBounds, ctx.getWorldBounds());
    }

    private SpatialContext createContext(Map<String, String> args) {
        return SpatialContextFactory.makeSpatialContext(args, getClass().getClassLoader());
    }
}