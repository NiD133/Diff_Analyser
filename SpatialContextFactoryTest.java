/*******************************************************************************
 * Copyright (c) 2015 Voyager Search and MITRE
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0 which
 * accompanies this distribution and is available at
 *    http://www.apache.org/licenses/LICENSE-2.0.txt
 ******************************************************************************/

package org.locationtech.spatial4j.context;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.spatial4j.context.jts.DatelineRule;
import org.locationtech.spatial4j.context.jts.JtsSpatialContext;
import org.locationtech.spatial4j.context.jts.JtsSpatialContextFactory;
import org.locationtech.spatial4j.context.jts.ValidationRule;
import org.locationtech.spatial4j.distance.CartesianDistCalc;
import org.locationtech.spatial4j.distance.GeodesicSphereDistCalc;
import org.locationtech.spatial4j.io.ShapeIO;
import org.locationtech.spatial4j.io.WKTReader;
import org.locationtech.spatial4j.shape.impl.RectangleImpl;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class SpatialContextFactoryTest {

  private static final String FACTORY_SYSPROP = "SpatialContextFactory";

  @Before
  public void setUp() {
    // Reset the static flag to ensure test isolation.
    CustomWktShapeParser.wasCreated = false;
  }

  @After
  public void tearDown() {
    System.getProperties().remove(FACTORY_SYSPROP);
  }

  /**
   * Helper method to create a SpatialContext from a series of key-value pair arguments.
   */
  private SpatialContext createContextFromArgs(String... args) {
    Map<String, String> argMap = new HashMap<>();
    for (int i = 0; i < args.length; i += 2) {
      argMap.put(args[i], args[i + 1]);
    }
    return SpatialContextFactory.makeSpatialContext(argMap, getClass().getClassLoader());
  }

  @Test
  public void should_createDefaultGeoContext_when_noArgsAreProvided() {
    // When
    SpatialContext createdCtx = createContextFromArgs(); // default args

    // Then
    SpatialContext defaultCtx = SpatialContext.GEO;
    assertEquals(defaultCtx.getClass(), createdCtx.getClass());
    assertEquals(defaultCtx.isGeo(), createdCtx.isGeo());
    assertEquals(defaultCtx.getDistCalc(), createdCtx.getDistCalc());
    assertEquals(defaultCtx.getWorldBounds(), createdCtx.getWorldBounds());
  }

  @Test
  public void should_createNonGeodeticContext_when_geoIsFalse() {
    // When
    SpatialContext ctx = createContextFromArgs("geo", "false");

    // Then
    assertFalse(ctx.isGeo());
    assertEquals(new CartesianDistCalc(), ctx.getDistCalc());
  }

  @Test
  public void should_createContextWithCustomCalculatorAndBounds_when_configured() {
    // When
    SpatialContext ctx = createContextFromArgs(
        "geo", "false",
        "distCalculator", "cartesian^2",
        "worldBounds", "ENVELOPE(-100, 75, 200, 0)"); // xMin, xMax, yMax, yMin

    // Then
    assertFalse(ctx.isGeo());
    assertEquals(new CartesianDistCalc(true), ctx.getDistCalc());
    assertEquals(new RectangleImpl(-100, 75, 0, 200, ctx), ctx.getWorldBounds());
  }

  @Test
  public void should_createGeodeticContextWithCustomCalculator_when_configured() {
    // When
    SpatialContext ctx = createContextFromArgs(
        "geo", "true",
        "distCalculator", "lawOfCosines");

    // Then
    assertTrue(ctx.isGeo());
    assertEquals(new GeodesicSphereDistCalc.LawOfCosines(), ctx.getDistCalc());
  }

  @Test
  public void should_createJtsContextWithAllParameters_when_fullyConfigured() {
    // When
    JtsSpatialContext ctx = (JtsSpatialContext) createContextFromArgs(
        "spatialContextFactory", JtsSpatialContextFactory.class.getName(),
        "geo", "true",
        "normWrapLongitude", "true",
        "precisionScale", "2.0",
        "wktShapeParserClass", CustomWktShapeParser.class.getName(),
        "datelineRule", "ccwRect",
        "validationRule", "repairConvexHull",
        "autoIndex", "true");

    // Then
    assertTrue(ctx.isNormWrapLongitude());
    assertEquals(2.0, ctx.getGeometryFactory().getPrecisionModel().getScale(), 0.0);
    assertTrue("CustomWktShapeParser should have been instantiated", CustomWktShapeParser.wasCreated);
    assertEquals(DatelineRule.ccwRect, ctx.getDatelineRule());
    assertEquals(ValidationRule.repairConvexHull, ctx.getValidationRule());
    assertTrue(ctx.isAutoIndex());
  }

  @Test
  public void should_createNonGeoJtsContextWithCustomWorldBounds_when_configured() {
    // This test verifies a fix for issue #72, ensuring worldBounds works correctly with geo=false.
    // When
    JtsSpatialContext ctx = (JtsSpatialContext) createContextFromArgs(
        "spatialContextFactory", JtsSpatialContextFactory.class.getName(),
        "geo", "false",
        "worldBounds", "ENVELOPE(-500,500,300,-300)");

    // Then
    assertFalse(ctx.isGeo());
    assertEquals(-500, ctx.getWorldBounds().getMinX(), 0.0);
    assertEquals(500, ctx.getWorldBounds().getMaxX(), 0.0);
    assertEquals(-300, ctx.getWorldBounds().getMinY(), 0.0);
    assertEquals(300, ctx.getWorldBounds().getMaxY(), 0.0);
  }

  @Test
  public void should_configureCustomShapeReader_when_readersPropertyIsSet() {
    // When
    JtsSpatialContext ctx = (JtsSpatialContext) createContextFromArgs(
        "spatialContextFactory", JtsSpatialContextFactory.class.getName(),
        "readers", CustomWktShapeParser.class.getName());

    // Then
    assertTrue(ctx.getFormats().getReader(ShapeIO.WKT) instanceof CustomWktShapeParser);
  }

  @Test
  public void should_createContextFromFactory_when_specifiedInSystemProperty() {
    // Given
    System.setProperty(FACTORY_SYSPROP, CustomNonGeoSpatialContextFactory.class.getName());

    // When
    SpatialContext ctx = createContextFromArgs();

    // Then
    assertFalse("Expected a non-geo context from the custom factory", ctx.isGeo());
  }

  /**
   * A custom factory that creates a non-geographic (geo=false) SpatialContext.
   * Used to test system property configuration.
   */
  public static class CustomNonGeoSpatialContextFactory extends SpatialContextFactory {
    @Override
    public SpatialContext newSpatialContext() {
      this.geo = false;
      return new SpatialContext(this);
    }
  }

  /**
   * A custom WKT parser with a static flag to verify its instantiation.
   */
  public static class CustomWktShapeParser extends WKTReader {
    static boolean wasCreated = false; // Flag to verify instantiation in tests.

    public CustomWktShapeParser(JtsSpatialContext ctx, JtsSpatialContextFactory factory) {
      super(ctx, factory);
      wasCreated = true;
    }
  }
}