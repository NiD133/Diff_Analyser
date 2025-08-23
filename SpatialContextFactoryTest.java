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

import static org.junit.Assert.*;

public class SpatialContextFactoryTest {

  // System property key used by SpatialContextFactory for factory lookup
  public static final String FACTORY_PROP = "SpatialContextFactory";

  @Before
  public void setUp() {
    // Ensure side-effect flags from previous tests are cleared
    CustomWktShapeParser.once = false;
  }

  @After
  public void tearDown() {
    // Ensure system property does not leak between tests
    System.getProperties().remove(FACTORY_PROP);
  }

  // -------- Helper methods --------

  /**
   * Creates a SpatialContext using provided key-value pairs.
   * Expects an even number of args: key1, value1, key2, value2, ...
   */
  private SpatialContext createContext(String... kvPairs) {
    Map<String, String> args = toArgsMap(kvPairs);
    return SpatialContextFactory.makeSpatialContext(args, getClass().getClassLoader());
  }

  /**
   * Convenience for creating a SpatialContext with a specific factory class.
   */
  private SpatialContext createContextWithFactory(Class<? extends SpatialContextFactory> factoryClass,
                                                  String... kvPairs) {
    Map<String, String> args = toArgsMap(kvPairs);
    args.put("spatialContextFactory", factoryClass.getName());
    return SpatialContextFactory.makeSpatialContext(args, getClass().getClassLoader());
  }

  private Map<String, String> toArgsMap(String... kvPairs) {
    if (kvPairs.length % 2 != 0) {
      throw new IllegalArgumentException("Arguments must be in key/value pairs");
    }
    Map<String, String> args = new HashMap<>();
    for (int i = 0; i < kvPairs.length; i += 2) {
      args.put(kvPairs[i], kvPairs[i + 1]);
    }
    return args;
  }

  // -------- Tests: defaults --------

  @Test
  public void testDefaultsMatchSpatialContextGeo() {
    // Given the library-provided default GEO context
    SpatialContext expected = SpatialContext.GEO;

    // When creating a context with no configuration
    SpatialContext actual = createContext();

    // Then the defaults should match
    assertEquals(expected.getClass(), actual.getClass());
    assertEquals(expected.isGeo(), actual.isGeo());
    assertEquals(expected.getDistCalc(), actual.getDistCalc());
    assertEquals(expected.getWorldBounds(), actual.getWorldBounds());
  }

  // -------- Tests: custom simple configurations --------

  @Test
  public void testNonGeoDefaults() {
    // When geo=false is specified
    SpatialContext ctx = createContext("geo", "false");

    // Then a cartesian calculator is used and geo flag is false
    assertFalse(ctx.isGeo());
    assertEquals(new CartesianDistCalc(), ctx.getDistCalc());
  }

  @Test
  public void testNonGeoWithSquaredCartesianAndWorldBounds() {
    // ENVELOPE order is: ENVELOPE(xMin, xMax, yMax, yMin)
    SpatialContext ctx = createContext(
        "geo", "false",
        "distCalculator", "cartesian^2",
        "worldBounds", "ENVELOPE(-100, 75, 200, 0)"
    );

    assertEquals(new CartesianDistCalc(true), ctx.getDistCalc());
    assertEquals(new RectangleImpl(-100, 75, 0, 200, ctx), ctx.getWorldBounds());
  }

  @Test
  public void testGeoWithLawOfCosines() {
    SpatialContext ctx = createContext(
        "geo", "true",
        "distCalculator", "lawOfCosines"
    );

    assertTrue(ctx.isGeo());
    assertEquals(new GeodesicSphereDistCalc.LawOfCosines(), ctx.getDistCalc());
  }

  // -------- Tests: JTS factory configurations --------

  @Test
  public void testJtsContextFactory_withAdvancedOptions() {
    JtsSpatialContext ctx = (JtsSpatialContext) createContextWithFactory(
        JtsSpatialContextFactory.class,
        "geo", "true",
        "normWrapLongitude", "true",
        "precisionScale", "2.0",
        "wktShapeParserClass", CustomWktShapeParser.class.getName(), // legacy option still supported
        "datelineRule", "ccwRect",
        "validationRule", "repairConvexHull",
        "autoIndex", "true"
    );

    assertTrue("Expected longitude to be wrap-normalized", ctx.isNormWrapLongitude());
    assertEquals(2.0, ctx.getGeometryFactory().getPrecisionModel().getScale(), 0.0);
    assertTrue("Custom WKT parser should have been instantiated", CustomWktShapeParser.once);
    assertEquals(DatelineRule.ccwRect, ctx.getDatelineRule());
    assertEquals(ValidationRule.repairConvexHull, ctx.getValidationRule());
  }

  @Test
  public void testJtsContextFactory_nonGeoWithCustomWorldBounds() {
    // Ensure geo=false with worldBounds works (regression for #72)
    JtsSpatialContext ctx = (JtsSpatialContext) createContextWithFactory(
        JtsSpatialContextFactory.class,
        "geo", "false",
        "worldBounds", "ENVELOPE(-500,500,300,-300)",
        "normWrapLongitude", "true",
        "precisionScale", "2.0",
        "wktShapeParserClass", CustomWktShapeParser.class.getName(),
        "datelineRule", "ccwRect",
        "validationRule", "repairConvexHull",
        "autoIndex", "true"
    );

    assertEquals(300.0, ctx.getWorldBounds().getMaxY(), 0.0);
  }

  // -------- Tests: formats configuration --------

  @Test
  public void testFormatsConfig_registersCustomReader() {
    JtsSpatialContext ctx = (JtsSpatialContext) createContextWithFactory(
        JtsSpatialContextFactory.class,
        "readers", CustomWktShapeParser.class.getName()
    );

    assertTrue(ctx.getFormats().getReader(ShapeIO.WKT) instanceof CustomWktShapeParser);
  }

  // -------- Tests: factory lookup via system property --------

  @Test
  public void testFactoryLookup_viaSystemProperty() {
    System.setProperty(FACTORY_PROP, DSCF.class.getName());

    // DSCF.newSpatialContext sets geo=false
    assertFalse(createContext().isGeo());
  }

  // -------- Test support classes --------

  /**
   * Test factory used to verify system property factory lookup behavior.
   */
  public static class DSCF extends SpatialContextFactory {
    @Override
    public SpatialContext newSpatialContext() {
      geo = false;
      return new SpatialContext(this);
    }
  }

  /**
   * Custom WKT parser used to verify that a provided reader class is instantiated.
   */
  public static class CustomWktShapeParser extends WKTReader {
    static boolean once = false; // Flag to verify instantiation in tests

    public CustomWktShapeParser(JtsSpatialContext ctx, JtsSpatialContextFactory factory) {
      super(ctx, factory);
      once = true;
    }
  }
}