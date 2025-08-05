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
import static org.junit.Assert.assertTrue;

public class SpatialContextFactoryTest {

    private static final String SYSTEM_PROPERTY_KEY = "SpatialContextFactory";

    @After
    public void clearSystemProperties() {
        System.getProperties().remove(SYSTEM_PROPERTY_KEY);
    }

    /**
     * Helper method to create a SpatialContext using provided arguments.
     */
    private SpatialContext createSpatialContext(String... keyValuePairs) {
        Map<String, String> arguments = new HashMap<>();
        for (int i = 0; i < keyValuePairs.length; i += 2) {
            String key = keyValuePairs[i];
            String value = keyValuePairs[i + 1];
            arguments.put(key, value);
        }
        return SpatialContextFactory.makeSpatialContext(arguments, getClass().getClassLoader());
    }

    @Test
    public void testDefaultSpatialContext() {
        SpatialContext defaultContext = SpatialContext.GEO;
        SpatialContext createdContext = createSpatialContext();

        assertEquals(defaultContext.getClass(), createdContext.getClass());
        assertEquals(defaultContext.isGeo(), createdContext.isGeo());
        assertEquals(defaultContext.getDistCalc(), createdContext.getDistCalc());
        assertEquals(defaultContext.getWorldBounds(), createdContext.getWorldBounds());
    }

    @Test
    public void testCustomSpatialContext() {
        SpatialContext nonGeoContext = createSpatialContext("geo", "false");
        assertTrue(!nonGeoContext.isGeo());
        assertEquals(new CartesianDistCalc(), nonGeoContext.getDistCalc());

        SpatialContext customContext = createSpatialContext(
            "geo", "false",
            "distCalculator", "cartesian^2",
            "worldBounds", "ENVELOPE(-100, 75, 200, 0)"
        );
        assertEquals(new CartesianDistCalc(true), customContext.getDistCalc());
        assertEquals(new RectangleImpl(-100, 75, 0, 200, customContext), customContext.getWorldBounds());

        SpatialContext geoContext = createSpatialContext(
            "geo", "true",
            "distCalculator", "lawOfCosines"
        );
        assertTrue(geoContext.isGeo());
        assertEquals(new GeodesicSphereDistCalc.LawOfCosines(), geoContext.getDistCalc());
    }

    @Test
    public void testJtsSpatialContextFactory() {
        JtsSpatialContext jtsContext = (JtsSpatialContext) createSpatialContext(
            "spatialContextFactory", JtsSpatialContextFactory.class.getName(),
            "geo", "true",
            "normWrapLongitude", "true",
            "precisionScale", "2.0",
            "wktShapeParserClass", CustomWktShapeParser.class.getName(),
            "datelineRule", "ccwRect",
            "validationRule", "repairConvexHull",
            "autoIndex", "true"
        );

        assertTrue(jtsContext.isNormWrapLongitude());
        assertEquals(2.0, jtsContext.getGeometryFactory().getPrecisionModel().getScale(), 0.0);
        assertTrue(CustomWktShapeParser.wasCreated);
        assertEquals(DatelineRule.ccwRect, jtsContext.getDatelineRule());
        assertEquals(ValidationRule.repairConvexHull, jtsContext.getValidationRule());

        // Test geo=false with worldBounds
        jtsContext = (JtsSpatialContext) createSpatialContext(
            "spatialContextFactory", JtsSpatialContextFactory.class.getName(),
            "geo", "false",
            "worldBounds", "ENVELOPE(-500,500,300,-300)",
            "normWrapLongitude", "true",
            "precisionScale", "2.0",
            "wktShapeParserClass", CustomWktShapeParser.class.getName(),
            "datelineRule", "ccwRect",
            "validationRule", "repairConvexHull",
            "autoIndex", "true"
        );
        assertEquals(300, jtsContext.getWorldBounds().getMaxY(), 0.0);
    }

    @Test
    public void testFormatsConfiguration() {
        JtsSpatialContext contextWithCustomReader = (JtsSpatialContext) createSpatialContext(
            "spatialContextFactory", JtsSpatialContextFactory.class.getName(),
            "readers", CustomWktShapeParser.class.getName()
        );

        assertTrue(contextWithCustomReader.getFormats().getReader(ShapeIO.WKT) instanceof CustomWktShapeParser);
    }

    @Test
    public void testSystemPropertyLookup() {
        System.setProperty(SYSTEM_PROPERTY_KEY, CustomSpatialContextFactory.class.getName());
        assertTrue(!createSpatialContext().isGeo());
    }

    public static class CustomSpatialContextFactory extends SpatialContextFactory {
        @Override
        public SpatialContext newSpatialContext() {
            geo = false;
            return new SpatialContext(this);
        }
    }

    public static class CustomWktShapeParser extends WKTReader {
        static boolean wasCreated = false;

        public CustomWktShapeParser(JtsSpatialContext context, JtsSpatialContextFactory factory) {
            super(context, factory);
            wasCreated = true;
        }
    }
}