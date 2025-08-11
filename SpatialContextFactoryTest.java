/*******************************************************************************
 * Copyright (c) 2015 Voyager Search and MITRE
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0 which
 * accompanies this distribution and is available at
 *    http://www.apache.org/licenses/LICENSE-2.0.txt
 ******************************************************************************/

package org.locationtech.spatial4j.context;

import org.locationtech.spatial4j.context.jts.DatelineRule;
import org.locationtech.spatial4j.context.jts.JtsSpatialContext;
import org.locationtech.spatial4j.context.jts.JtsSpatialContextFactory;
import org.locationtech.spatial4j.context.jts.ValidationRule;
import org.locationtech.spatial4j.distance.CartesianDistCalc;
import org.locationtech.spatial4j.distance.GeodesicSphereDistCalc;
import org.locationtech.spatial4j.io.ShapeIO;
import org.locationtech.spatial4j.io.WKTReader;
import org.locationtech.spatial4j.shape.impl.RectangleImpl;
import org.junit.After;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests for SpatialContextFactory configuration and creation of SpatialContext instances.
 */
public class SpatialContextFactoryTest {
    
    private static final String SYSTEM_PROPERTY_KEY = "SpatialContextFactory";

    @After
    public void cleanUpSystemProperties() {
        System.getProperties().remove(SYSTEM_PROPERTY_KEY);
    }
    
    /**
     * Creates a SpatialContext with the given configuration parameters.
     * Parameters should be provided as key-value pairs.
     */
    private SpatialContext createSpatialContextWithConfig(String... keyValuePairs) {
        Map<String, String> configArgs = buildConfigMap(keyValuePairs);
        return SpatialContextFactory.makeSpatialContext(configArgs, getClass().getClassLoader());
    }
    
    /**
     * Converts key-value pairs into a configuration map.
     */
    private Map<String, String> buildConfigMap(String... keyValuePairs) {
        Map<String, String> configMap = new HashMap<>();
        for (int i = 0; i < keyValuePairs.length; i += 2) {
            String key = keyValuePairs[i];
            String value = keyValuePairs[i + 1];
            configMap.put(key, value);
        }
        return configMap;
    }
    
    @Test
    public void shouldCreateDefaultGeoSpatialContext() {
        // Given: Default GEO spatial context
        SpatialContext expectedDefaultContext = SpatialContext.GEO;
        
        // When: Creating context with no configuration
        SpatialContext actualContext = createSpatialContextWithConfig();
        
        // Then: Should match default GEO context properties
        assertEquals("Context class should match", 
                    expectedDefaultContext.getClass(), actualContext.getClass());
        assertEquals("Should be geo-enabled", 
                    expectedDefaultContext.isGeo(), actualContext.isGeo());
        assertEquals("Distance calculator should match", 
                    expectedDefaultContext.getDistCalc(), actualContext.getDistCalc());
        assertEquals("World bounds should match", 
                    expectedDefaultContext.getWorldBounds(), actualContext.getWorldBounds());
    }
    
    @Test
    public void shouldCreateCartesianSpatialContext() {
        // When: Creating non-geo (Cartesian) spatial context
        SpatialContext cartesianContext = createSpatialContextWithConfig("geo", "false");
        
        // Then: Should be configured for Cartesian coordinates
        assertFalse("Should not be geo-enabled", cartesianContext.isGeo());
        assertEquals("Should use Cartesian distance calculator", 
                    new CartesianDistCalc(), cartesianContext.getDistCalc());
    }

    @Test
    public void shouldCreateCustomCartesianContextWithSquaredDistanceAndBounds() {
        // Given: Custom world bounds envelope (xMin=-100, xMax=75, yMax=200, yMin=0)
        RectangleImpl expectedBounds = new RectangleImpl(-100, 75, 0, 200, null);
        
        // When: Creating Cartesian context with squared distance calculation and custom bounds
        SpatialContext customContext = createSpatialContextWithConfig(
            "geo", "false",
            "distCalculator", "cartesian^2",
            "worldBounds", "ENVELOPE(-100, 75, 200, 0)" // xMin, xMax, yMax, yMin
        );
        
        // Then: Should use squared Cartesian distance and custom bounds
        assertEquals("Should use squared Cartesian distance calculator", 
                    new CartesianDistCalc(true), customContext.getDistCalc());
        assertEquals("Should have custom world bounds", 
                    expectedBounds, customContext.getWorldBounds());
    }

    @Test
    public void shouldCreateGeoContextWithLawOfCosinesDistanceCalculator() {
        // When: Creating geo context with Law of Cosines distance calculator
        SpatialContext geoContext = createSpatialContextWithConfig(
            "geo", "true",
            "distCalculator", "lawOfCosines"
        );
        
        // Then: Should be geo-enabled with Law of Cosines calculator
        assertTrue("Should be geo-enabled", geoContext.isGeo());
        assertEquals("Should use Law of Cosines distance calculator",
                    new GeodesicSphereDistCalc.LawOfCosines(), geoContext.getDistCalc());
    }

    @Test
    public void shouldCreateJtsSpatialContextWithAllCustomSettings() {
        // When: Creating JTS spatial context with comprehensive configuration
        JtsSpatialContext jtsContext = (JtsSpatialContext) createSpatialContextWithConfig(
            "spatialContextFactory", JtsSpatialContextFactory.class.getName(),
            "geo", "true",
            "normWrapLongitude", "true",
            "precisionScale", "2.0",
            "wktShapeParserClass", TestCustomWktShapeParser.class.getName(),
            "datelineRule", "ccwRect",
            "validationRule", "repairConvexHull",
            "autoIndex", "true"
        );
        
        // Then: Should have all custom settings applied
        assertTrue("Should normalize longitude wrapping", jtsContext.isNormWrapLongitude());
        assertEquals("Should have precision scale of 2.0", 
                    2.0, jtsContext.getGeometryFactory().getPrecisionModel().getScale(), 0.0);
        assertTrue("Custom WKT parser should have been instantiated", 
                  TestCustomWktShapeParser.wasInstantiated);
        assertEquals("Should use counter-clockwise rectangle dateline rule", 
                    DatelineRule.ccwRect, jtsContext.getDatelineRule());
        assertEquals("Should use repair convex hull validation rule", 
                    ValidationRule.repairConvexHull, jtsContext.getValidationRule());
    }

    @Test
    public void shouldCreateNonGeoJtsContextWithCustomWorldBounds() {
        // This test addresses issue #72: ensure geo=false works with worldbounds in JTS context
        
        // When: Creating non-geo JTS context with custom world bounds
        JtsSpatialContext nonGeoJtsContext = (JtsSpatialContext) createSpatialContextWithConfig(
            "spatialContextFactory", JtsSpatialContextFactory.class.getName(),
            "geo", "false",
            "worldBounds", "ENVELOPE(-500,500,300,-300)",
            "normWrapLongitude", "true",
            "precisionScale", "2.0",
            "wktShapeParserClass", TestCustomWktShapeParser.class.getName(),
            "datelineRule", "ccwRect",
            "validationRule", "repairConvexHull",
            "autoIndex", "true"
        );
        
        // Then: Should have custom world bounds with maxY=300
        assertEquals("Should have custom world bounds with maxY=300", 
                    300.0, nonGeoJtsContext.getWorldBounds().getMaxY(), 0.0);
    }
    
    @Test
    public void shouldConfigureCustomShapeReaderFormats() {
        // When: Creating JTS context with custom WKT reader
        JtsSpatialContext contextWithCustomReader = (JtsSpatialContext) createSpatialContextWithConfig(
            "spatialContextFactory", JtsSpatialContextFactory.class.getName(),
            "readers", TestCustomWktShapeParser.class.getName()
        );
        
        // Then: Should use custom WKT reader
        assertTrue("Should use custom WKT shape parser", 
                  contextWithCustomReader.getFormats().getReader(ShapeIO.WKT) 
                  instanceof TestCustomWktShapeParser);
    }
    
    @Test
    public void shouldLookupFactoryClassFromSystemProperty() {
        // Given: System property pointing to custom factory
        System.setProperty(SYSTEM_PROPERTY_KEY, TestCustomSpatialContextFactory.class.getName());
        
        // When: Creating context (should use system property)
        SpatialContext contextFromSystemProperty = createSpatialContextWithConfig();
        
        // Then: Should use custom factory that creates non-geo context
        assertFalse("Should create non-geo context from custom factory", 
                   contextFromSystemProperty.isGeo());
    }

    /**
     * Test implementation of SpatialContextFactory that creates non-geo contexts.
     */
    public static class TestCustomSpatialContextFactory extends SpatialContextFactory {
        @Override
        public SpatialContext newSpatialContext() {
            geo = false; // Always create non-geo contexts
            return new SpatialContext(this);
        }
    }

    /**
     * Test implementation of WKTReader for testing custom parser configuration.
     */
    public static class TestCustomWktShapeParser extends WKTReader {
        static boolean wasInstantiated = false;
        
        public TestCustomWktShapeParser(JtsSpatialContext ctx, JtsSpatialContextFactory factory) {
            super(ctx, factory);
            wasInstantiated = true;
        }
    }
}