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
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SpatialContextFactoryTest {
    private static final String PROP = "SpatialContextFactory";
    
    // Configuration constants
    private static final String GEO = "geo";
    private static final String DIST_CALCULATOR = "distCalculator";
    private static final String WORLD_BOUNDS = "worldBounds";
    private static final String SPATIAL_CONTEXT_FACTORY = "spatialContextFactory";
    private static final String NORM_WRAP_LONGITUDE = "normWrapLongitude";
    private static final String PRECISION_SCALE = "precisionScale";
    private static final String WKT_SHAPE_PARSER_CLASS = "wktShapeParserClass";
    private static final String DATELINE_RULE = "datelineRule";
    private static final String VALIDATION_RULE = "validationRule";
    private static final String AUTO_INDEX = "autoIndex";
    private static final String READERS = "readers";

    @After
    public void tearDown() {
        System.getProperties().remove(PROP);
        CustomWktShapeParser.once = false; // Reset parser flag
    }

    private SpatialContext createSpatialContext(String... args) {
        Map<String, String> config = new HashMap<>();
        for (int i = 0; i < args.length; i += 2) {
            config.put(args[i], args[i + 1]);
        }
        return SpatialContextFactory.makeSpatialContext(config, getClass().getClassLoader());
    }

    @Test
    public void createDefaultContext_shouldMatchGlobalInstance() {
        SpatialContext defaultContext = SpatialContext.GEO;
        SpatialContext createdContext = createSpatialContext();
        
        assertEquals(defaultContext.getClass(), createdContext.getClass());
        assertEquals(defaultContext.isGeo(), createdContext.isGeo());
        assertEquals(defaultContext.getDistCalc(), createdContext.getDistCalc());
        assertEquals(defaultContext.getWorldBounds(), createdContext.getWorldBounds());
    }

    @Test
    public void createNonGeoContext_shouldUseCartesianDistance() {
        SpatialContext context = createSpatialContext(GEO, "false");
        
        assertTrue("Should be non-geo context", !context.isGeo());
        assertEquals("Should use Cartesian distance", 
                     new CartesianDistCalc(), context.getDistCalc());
    }

    @Test
    public void createNonGeoContext_withCustomDistanceAndBounds_shouldApplySettings() {
        SpatialContext context = createSpatialContext(
            GEO, "false",
            DIST_CALCULATOR, "cartesian^2",
            WORLD_BOUNDS, "ENVELOPE(-100, 75, 200, 0)"
        );
        
        assertEquals("Should use squared Cartesian distance",
                     new CartesianDistCalc(true), context.getDistCalc());
        assertEquals("World bounds should match",
                     new RectangleImpl(-100, 75, 0, 200, context), 
                     context.getWorldBounds());
    }

    @Test
    public void createGeoContext_withLawOfCosines_shouldUseCorrectCalculator() {
        SpatialContext context = createSpatialContext(
            GEO, "true",
            DIST_CALCULATOR, "lawOfCosines"
        );
        
        assertTrue("Should be geo context", context.isGeo());
        assertEquals("Should use Law of Cosines calculator",
                     new GeodesicSphereDistCalc.LawOfCosines(),
                     context.getDistCalc());
    }

    @Test
    public void createJtsContext_withGeoTrue_shouldApplyAllSettings() {
        CustomWktShapeParser.once = false; // Reset before test
        
        JtsSpatialContext context = (JtsSpatialContext) createSpatialContext(
            SPATIAL_CONTEXT_FACTORY, JtsSpatialContextFactory.class.getName(),
            GEO, "true",
            NORM_WRAP_LONGITUDE, "true",
            PRECISION_SCALE, "2.0",
            WKT_SHAPE_PARSER_CLASS, CustomWktShapeParser.class.getName(),
            DATELINE_RULE, "ccwRect",
            VALIDATION_RULE, "repairConvexHull",
            AUTO_INDEX, "true"
        );
        
        assertTrue("Should wrap longitude", context.isNormWrapLongitude());
        assertEquals("Precision scale should match", 2.0, 
                     context.getGeometryFactory().getPrecisionModel().getScale(), 0.0);
        assertTrue("Custom parser should be initialized", CustomWktShapeParser.once);
        assertEquals("Dateline rule should match", 
                     DatelineRule.ccwRect, context.getDatelineRule());
        assertEquals("Validation rule should match",
                     ValidationRule.repairConvexHull, context.getValidationRule());
    }

    @Test
    public void createJtsContext_withGeoFalseAndBounds_shouldApplyWorldBounds() {
        JtsSpatialContext context = (JtsSpatialContext) createSpatialContext(
            SPATIAL_CONTEXT_FACTORY, JtsSpatialContextFactory.class.getName(),
            GEO, "false",
            WORLD_BOUNDS, "ENVELOPE(-500,500,300,-300)",
            NORM_WRAP_LONGITUDE, "true",
            PRECISION_SCALE, "2.0",
            WKT_SHAPE_PARSER_CLASS, CustomWktShapeParser.class.getName(),
            DATELINE_RULE, "ccwRect",
            VALIDATION_RULE, "repairConvexHull",
            AUTO_INDEX, "true"
        );
        
        assertEquals("Max Y should match custom bounds", 
                     300, context.getWorldBounds().getMaxY(), 0.0);
    }

    @Test
    public void configureFormats_withCustomReader_shouldRegisterReader() {
        JtsSpatialContext context = (JtsSpatialContext) createSpatialContext(
            SPATIAL_CONTEXT_FACTORY, JtsSpatialContextFactory.class.getName(),
            READERS, CustomWktShapeParser.class.getName()
        );
        
        assertTrue("Should use custom WKT reader",
                   context.getFormats().getReader(ShapeIO.WKT) instanceof CustomWktShapeParser);
    }

    @Test
    public void createContext_withSystemPropertyFactory_shouldUseCustomFactory() {
        System.setProperty(PROP, DSCF.class.getName());
        assertTrue("Should use non-geo context from system property", 
                   !createSpatialContext().isGeo());
    }

    // Helper classes ----------------------------------------------------------
    
    public static class DSCF extends SpatialContextFactory {
        @Override
        public SpatialContext newSpatialContext() {
            geo = false; // Custom behavior
            return new SpatialContext(this);
        }
    }

    public static class CustomWktShapeParser extends WKTReader {
        static boolean once = false; // Tracks initialization
        
        public CustomWktShapeParser(JtsSpatialContext ctx, JtsSpatialContextFactory factory) {
            super(ctx, factory);
            once = true;
        }
    }
}