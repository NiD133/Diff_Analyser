package org.locationtech.spatial4j.context;

import org.junit.Test;
import org.locationtech.spatial4j.io.BinaryCodec;
import org.locationtech.spatial4j.io.SupportedFormats;
import org.locationtech.spatial4j.shape.ShapeFactory;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class SpatialContextFactoryTest {

  private static ClassLoader cl() {
    return ClassLoader.getSystemClassLoader();
  }

  @Test
  public void createWithDefaults_shouldProduceGeoContext() {
    Map<String, String> args = new HashMap<>();
    SpatialContext ctx = SpatialContextFactory.makeSpatialContext(args, cl());

    assertNotNull(ctx);
    assertTrue("Default context should be geo", ctx.isGeo());
    assertFalse("Default should not normalize/wrap longitude", ctx.isNormWrapLongitude());
  }

  @Test(expected = NullPointerException.class)
  public void rejectNullArgs_shouldThrowNPE() {
    SpatialContextFactory.makeSpatialContext(null, cl());
  }

  @Test
  public void unknownDistCalculator_shouldFail() {
    Map<String, String> args = new HashMap<>();
    args.put("distCalculator", "not_a_calculator");

    try {
      SpatialContextFactory.makeSpatialContext(args, cl());
      fail("Expected failure for unknown distance calculator");
    } catch (RuntimeException e) {
      // message is implementation-specific; type is sufficient
    }
  }

  @Test
  public void distCalculator_variants_shouldSucceed() {
    assertCreatesContextWithDistCalc("haversine");
    assertCreatesContextWithDistCalc("lawofcosines");
    assertCreatesContextWithDistCalc("vincentysphere");
    assertCreatesContextWithDistCalc("cartesian");
    assertCreatesContextWithDistCalc("cartesian^2");
  }

  private void assertCreatesContextWithDistCalc(String calcName) {
    Map<String, String> args = new HashMap<>();
    args.put("distCalculator", calcName);

    SpatialContext ctx = SpatialContextFactory.makeSpatialContext(args, cl());
    assertNotNull("Context should be created for calculator " + calcName, ctx);
    // Norm/wrap longitude is false by default
    assertFalse(ctx.isNormWrapLongitude());
    // The factory defaults to geo=true unless explicitly configured otherwise
    assertTrue(ctx.isGeo());
  }

  @Test
  public void readersConfigWithComma_shouldSucceed() {
    Map<String, String> args = new HashMap<>();
    args.put("readers", ",");

    SpatialContext ctx = SpatialContextFactory.makeSpatialContext(args, cl());
    assertNotNull(ctx);
  }

  @Test
  public void writersConfigWithComma_shouldSucceed() {
    Map<String, String> args = new HashMap<>();
    args.put("writers", ",");

    SpatialContext ctx = SpatialContextFactory.makeSpatialContext(args, cl());
    assertNotNull(ctx);
  }

  @Test
  public void invalidReaderClass_shouldFail() {
    Map<String, String> args = new HashMap<>();
    args.put("readers", "not.a.real.ReaderClass");

    try {
      SpatialContextFactory.makeSpatialContext(args, cl());
      fail("Expected failure for invalid reader class name");
    } catch (RuntimeException e) {
      // message content is not asserted to avoid brittleness
    }
  }

  @Test
  public void invalidWriterClass_shouldFail() {
    Map<String, String> args = new HashMap<>();
    args.put("writers", "not.a.real.WriterClass");

    try {
      SpatialContextFactory.makeSpatialContext(args, cl());
      fail("Expected failure for invalid writer class name");
    } catch (RuntimeException e) {
      // message content is not asserted to avoid brittleness
    }
  }

  @Test
  public void invalidShapeFactoryClassName_shouldFail() {
    Map<String, String> args = new HashMap<>();
    args.put("shapeFactoryClass", "shapeFactoryClass");

    try {
      SpatialContextFactory.makeSpatialContext(args, cl());
      fail("Expected failure for invalid shapeFactoryClass");
    } catch (RuntimeException e) {
      // type only
    }
  }

  @Test
  public void spatialContextFactoryParam_unknownClass_shouldFail() {
    Map<String, String> args = new HashMap<>();
    args.put("spatialContextFactory", "spatialContextFactory"); // not a real class

    try {
      SpatialContextFactory.makeSpatialContext(args, cl());
      fail("Expected failure for unknown spatialContextFactory class");
    } catch (RuntimeException e) {
      // type only
    }
  }

  @Test
  public void spatialContextFactoryParam_validClass_shouldSucceed() {
    Map<String, String> args = new HashMap<>();
    args.put("spatialContextFactory", "org.locationtech.spatial4j.context.SpatialContextFactory");

    SpatialContext ctx = SpatialContextFactory.makeSpatialContext(args, null);
    assertNotNull(ctx);
    assertTrue(ctx.isGeo());
    assertFalse(ctx.isNormWrapLongitude());
  }

  @Test
  public void settingShapeFactoryClassToInterface_shouldFailWhenCreatingContext() {
    SpatialContextFactory f = new SpatialContextFactory();
    // deliberately set to an interface, which can't be instantiated
    @SuppressWarnings("unchecked")
    Class<? extends ShapeFactory> iface = (Class<? extends ShapeFactory>) ShapeFactory.class;
    f.shapeFactoryClass = iface;

    try {
      f.newSpatialContext();
      fail("Expected failure when shapeFactoryClass is an interface");
    } catch (RuntimeException e) {
      // type only
    }
  }

  @Test
  public void addReaderIfNoggitExists_shouldNotThrow() {
    SpatialContextFactory f = new SpatialContextFactory();
    // Using a class that definitely exists; method should never throw
    f.addReaderIfNoggitExists(org.locationtech.spatial4j.io.PolyshapeReader.class);
    // no assertions; just ensuring no exception is thrown
  }

  @Test
  public void makeFormats_and_makeBinaryCodec_shouldWorkWithDefaultContext() {
    SpatialContextFactory f = new SpatialContextFactory();
    SpatialContext ctx = new SpatialContext(f);

    SupportedFormats formats = f.makeFormats(ctx);
    assertNotNull(formats);

    BinaryCodec codec = f.makeBinaryCodec(ctx);
    assertNotNull(codec);
  }
}