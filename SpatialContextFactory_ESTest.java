package org.locationtech.spatial4j.context;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.HashMap;
import java.util.Map;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.context.SpatialContextFactory;
import org.locationtech.spatial4j.io.PolyshapeReader;
import org.locationtech.spatial4j.shape.ShapeFactory;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class SpatialContextFactory_ESTest extends SpatialContextFactory_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testShapeFactoryClassConstructor() throws Throwable {
        HashMap<String, String> config = new HashMap<>();
        config.put("shapeFactoryClass", "org.locationtech.spatial4j.shape.impl.BufferedLineString");
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();

        try {
            SpatialContextFactory.makeSpatialContext(config, classLoader);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            verifyException("org.locationtech.spatial4j.context.SpatialContextFactory", e);
        }
    }

    @Test(timeout = 4000)
    public void testInvalidWritersFormat() throws Throwable {
        HashMap<String, String> config = new HashMap<>();
        config.put("writers", ", worldBounds=");
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();

        try {
            SpatialContextFactory.makeSpatialContext(config, classLoader);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            verifyException("org.locationtech.spatial4j.context.SpatialContextFactory", e);
        }
    }

    @Test(timeout = 4000)
    public void testInvalidReadersFormat() throws Throwable {
        HashMap<String, String> config = new HashMap<>();
        config.put("readers", "%{Y,*MT'j<t9mHJr/M_");

        try {
            SpatialContextFactory.makeSpatialContext(config, null);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            verifyException("org.locationtech.spatial4j.context.SpatialContextFactory", e);
        }
    }

    @Test(timeout = 4000)
    public void testInitializationWithEmptyConfig() throws Throwable {
        HashMap<String, String> config = new HashMap<>();
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        SpatialContextFactory factory = new SpatialContextFactory();

        factory.init(config, classLoader);
        factory.initWorldBounds();

        assertFalse(factory.hasFormatConfig);
        assertFalse(factory.normWrapLongitude);
        assertTrue(factory.geo);
    }

    @Test(timeout = 4000)
    public void testInitializationWithNullClassLoader() throws Throwable {
        SpatialContextFactory factory = new SpatialContextFactory();
        HashMap<String, String> config = new HashMap<>();

        factory.init(config, null);
        factory.initFormats();

        assertFalse(factory.hasFormatConfig);
        assertTrue(factory.geo);
        assertFalse(factory.normWrapLongitude);
    }

    @Test(timeout = 4000)
    public void testFieldInitialization() throws Throwable {
        HashMap<String, String> config = new HashMap<>();
        SpatialContextFactory factory = new SpatialContextFactory();
        factory.args = config;

        factory.initField("readers");

        assertFalse(factory.normWrapLongitude);
        assertTrue(factory.geo);
        assertFalse(factory.hasFormatConfig);
    }

    @Test(timeout = 4000)
    public void testCalculatorInitialization() throws Throwable {
        HashMap<String, String> config = new HashMap<>();
        SpatialContextFactory factory = new SpatialContextFactory();
        factory.args = config;

        factory.initCalculator();

        assertTrue(factory.geo);
        assertFalse(factory.hasFormatConfig);
        assertFalse(factory.normWrapLongitude);
    }

    @Test(timeout = 4000)
    public void testDefaultFormatsCheck() throws Throwable {
        SpatialContextFactory factory = new SpatialContextFactory();

        factory.checkDefaultFormats();

        assertFalse(factory.hasFormatConfig);
        assertFalse(factory.normWrapLongitude);
        assertTrue(factory.geo);
    }

    @Test(timeout = 4000)
    public void testInvalidSpatialContextFactory() throws Throwable {
        HashMap<String, String> config = new HashMap<>();
        config.put("spatialContextFactory", "spatialContextFactory");
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();

        try {
            SpatialContextFactory.makeSpatialContext(config, classLoader);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            verifyException("org.locationtech.spatial4j.context.SpatialContextFactory", e);
        }
    }

    @Test(timeout = 4000)
    public void testEmptyFieldInitialization() throws Throwable {
        SpatialContextFactory factory = new SpatialContextFactory();

        try {
            factory.initField("");
            fail("Expecting exception: Error");
        } catch (Error e) {
            verifyException("org.locationtech.spatial4j.context.SpatialContextFactory", e);
        }
    }

    @Test(timeout = 4000)
    public void testShapeFactoryCreation() throws Throwable {
        SpatialContextFactory factory = new SpatialContextFactory();
        SpatialContext context = new SpatialContext(factory);

        factory.makeShapeFactory(context);

        assertFalse(factory.hasFormatConfig);
        assertTrue(factory.geo);
        assertFalse(factory.normWrapLongitude);
    }

    @Test(timeout = 4000)
    public void testBinaryCodecCreation() throws Throwable {
        SpatialContextFactory factory = new SpatialContextFactory();
        HashMap<String, String> config = new HashMap<>();
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        SpatialContext context = SpatialContextFactory.makeSpatialContext(config, classLoader);

        factory.makeBinaryCodec(context);

        assertTrue(factory.geo);
        assertFalse(factory.normWrapLongitude);
    }

    @Test(timeout = 4000)
    public void testInvalidShapeFactoryClass() throws Throwable {
        SpatialContextFactory factory = new SpatialContextFactory();
        Class<ShapeFactory> shapeFactoryClass = ShapeFactory.class;
        factory.shapeFactoryClass = shapeFactoryClass;

        try {
            factory.newSpatialContext();
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            verifyException("org.locationtech.spatial4j.context.SpatialContextFactory", e);
        }
    }

    @Test(timeout = 4000)
    public void testNullConfigForSpatialContext() throws Throwable {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();

        try {
            SpatialContextFactory.makeSpatialContext(null, classLoader);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.locationtech.spatial4j.context.SpatialContextFactory", e);
        }
    }

    @Test(timeout = 4000)
    public void testNoClassDefFoundError() throws Throwable {
        HashMap<String, String> config = new HashMap<>();
        config.putIfAbsent("shapeFactoryClass", "org.locationtech.spatial4j.context.spatialcontext");
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();

        try {
            SpatialContextFactory.makeSpatialContext(config, classLoader);
            fail("Expecting exception: NoClassDefFoundError");
        } catch (NoClassDefFoundError e) {
        }
    }

    @Test(timeout = 4000)
    public void testNullShapeFactoryCreation() throws Throwable {
        SpatialContextFactory factory = new SpatialContextFactory();

        try {
            factory.makeShapeFactory(null);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            verifyException("org.locationtech.spatial4j.context.SpatialContextFactory", e);
        }
    }

    @Test(timeout = 4000)
    public void testNullFormatsCreation() throws Throwable {
        SpatialContextFactory factory = new SpatialContextFactory();

        try {
            factory.makeFormats(null);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            verifyException("org.locationtech.spatial4j.context.SpatialContextFactory", e);
        }
    }

    @Test(timeout = 4000)
    public void testNullBinaryCodecCreation() throws Throwable {
        SpatialContextFactory factory = new SpatialContextFactory();

        try {
            factory.makeBinaryCodec(null);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            verifyException("org.locationtech.spatial4j.context.SpatialContextFactory", e);
        }
    }

    @Test(timeout = 4000)
    public void testNullWorldBoundsInitialization() throws Throwable {
        SpatialContextFactory factory = new SpatialContextFactory();

        try {
            factory.initWorldBounds();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.locationtech.spatial4j.context.SpatialContextFactory", e);
        }
    }

    @Test(timeout = 4000)
    public void testInvalidReadersInitialization() throws Throwable {
        HashMap<String, String> config = new HashMap<>();
        config.put("readers", "readers");
        SpatialContextFactory factory = new SpatialContextFactory();
        factory.args = config;

        try {
            factory.initFormats();
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            verifyException("org.locationtech.spatial4j.context.SpatialContextFactory", e);
        }
    }

    @Test(timeout = 4000)
    public void testNullFormatsInitialization() throws Throwable {
        SpatialContextFactory factory = new SpatialContextFactory();

        try {
            factory.initFormats();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.locationtech.spatial4j.context.SpatialContextFactory", e);
        }
    }

    @Test(timeout = 4000)
    public void testNullFieldInitialization() throws Throwable {
        SpatialContextFactory factory = new SpatialContextFactory();

        try {
            factory.initField("shapeFactoryClass");
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.locationtech.spatial4j.context.SpatialContextFactory", e);
        }
    }

    @Test(timeout = 4000)
    public void testNullCalculatorInitialization() throws Throwable {
        SpatialContextFactory factory = new SpatialContextFactory();

        try {
            factory.initCalculator();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.locationtech.spatial4j.context.SpatialContextFactory", e);
        }
    }

    @Test(timeout = 4000)
    public void testInvalidShapeFactoryClassInitialization() throws Throwable {
        HashMap<String, String> config = new HashMap<>();
        config.put("shapeFactoryClass", "shapeFactoryClass");
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        SpatialContextFactory factory = new SpatialContextFactory();

        try {
            factory.init(config, classLoader);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            verifyException("org.locationtech.spatial4j.context.SpatialContextFactory", e);
        }
    }

    @Test(timeout = 4000)
    public void testNullConfigInitialization() throws Throwable {
        SpatialContextFactory factory = new SpatialContextFactory();
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();

        try {
            factory.init(null, classLoader);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.locationtech.spatial4j.context.SpatialContextFactory", e);
        }
    }

    @Test(timeout = 4000)
    public void testAddReaderIfNoggitExists() throws Throwable {
        SpatialContextFactory factory = new SpatialContextFactory();
        HashMap<String, String> config = new HashMap<>();
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        factory.init(config, classLoader);

        Class<PolyshapeReader> readerClass = PolyshapeReader.class;
        factory.addReaderIfNoggitExists(readerClass);

        assertTrue(factory.geo);
        assertFalse(factory.hasFormatConfig);
        assertFalse(factory.normWrapLongitude);
    }

    @Test(timeout = 4000)
    public void testAddReaderIfNoggitExistsWithoutInit() throws Throwable {
        SpatialContextFactory factory = new SpatialContextFactory();
        Class<PolyshapeReader> readerClass = PolyshapeReader.class;

        factory.addReaderIfNoggitExists(readerClass);

        assertTrue(factory.geo);
        assertFalse(factory.normWrapLongitude);
        assertFalse(factory.hasFormatConfig);
    }

    @Test(timeout = 4000)
    public void testNewSpatialContext() throws Throwable {
        SpatialContextFactory factory = new SpatialContextFactory();

        factory.newSpatialContext();

        assertFalse(factory.normWrapLongitude);
        assertTrue(factory.geo);
        assertFalse(factory.hasFormatConfig);
    }

    @Test(timeout = 4000)
    public void testInvalidLegacyShapeWriter() throws Throwable {
        HashMap<String, String> config = new HashMap<>();
        config.put("shapeFactoryClass", "org.locationtech.spatial4j.io.LegacyShapeWriter");
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();

        try {
            SpatialContextFactory.makeSpatialContext(config, classLoader);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            verifyException("org.locationtech.spatial4j.context.SpatialContextFactory", e);
        }
    }

    @Test(timeout = 4000)
    public void testValidWorldBounds() throws Throwable {
        HashMap<String, String> config = new HashMap<>();
        config.put("worldBounds", "KLjy(V*@=mG*& @#o");
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();

        SpatialContext context = SpatialContextFactory.makeSpatialContext(config, classLoader);

        assertTrue(context.isGeo());
    }

    @Test(timeout = 4000)
    public void testFormatsCreation() throws Throwable {
        SpatialContextFactory factory = new SpatialContextFactory();
        SpatialContext context = new SpatialContext(factory);

        factory.makeFormats(context);

        assertFalse(factory.normWrapLongitude);
        assertFalse(factory.hasFormatConfig);
        assertTrue(factory.geo);
    }

    @Test(timeout = 4000)
    public void testInvalidWritersInitialization() throws Throwable {
        HashMap<String, String> config = new HashMap<>();
        config.putIfAbsent("writers", "org.locationtech.spatial4j.shape.impl.RectangleImpl");

        try {
            SpatialContextFactory.makeSpatialContext(config, null);
            fail("Expecting exception: ClassCastException");
        } catch (ClassCastException e) {
        }
    }

    @Test(timeout = 4000)
    public void testValidWritersInitialization() throws Throwable {
        HashMap<String, String> config = new HashMap<>();
        config.putIfAbsent("writers", ",");
        
        SpatialContext context = SpatialContextFactory.makeSpatialContext(config, null);

        assertTrue(context.isGeo());
    }

    @Test(timeout = 4000)
    public void testInvalidWktShapeParserClass() throws Throwable {
        HashMap<String, String> config = new HashMap<>();
        config.put("wktShapeParserClass", "wktShapeParserClass");
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();

        try {
            SpatialContextFactory.makeSpatialContext(config, classLoader);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            verifyException("org.locationtech.spatial4j.context.SpatialContextFactory", e);
        }
    }

    @Test(timeout = 4000)
    public void testValidReadersInitialization() throws Throwable {
        HashMap<String, String> config = new HashMap<>();
        config.put("readers", ",");
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();

        SpatialContext context = SpatialContextFactory.makeSpatialContext(config, classLoader);

        assertTrue(context.isGeo());
        assertFalse(context.isNormWrapLongitude());
    }

    @Test(timeout = 4000)
    public void testUnknownCalculator() throws Throwable {
        HashMap<String, String> config = new HashMap<>();
        config.putIfAbsent("distCalculator", "distCalculator");
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();

        try {
            SpatialContextFactory.makeSpatialContext(config, classLoader);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            verifyException("org.locationtech.spatial4j.context.SpatialContextFactory", e);
        }
    }

    @Test(timeout = 4000)
    public void testCartesianSquaredCalculator() throws Throwable {
        HashMap<String, String> config = new HashMap<>();
        config.put("distCalculator", "cartesian^2");
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();

        SpatialContext context = SpatialContextFactory.makeSpatialContext(config, classLoader);

        assertFalse(context.isNormWrapLongitude());
    }

    @Test(timeout = 4000)
    public void testVincentySphereCalculator() throws Throwable {
        HashMap<String, String> config = new HashMap<>();
        config.putIfAbsent("distCalculator", "vincentysphere");
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();

        SpatialContext context = SpatialContextFactory.makeSpatialContext(config, classLoader);

        assertTrue(context.isGeo());
        assertFalse(context.isNormWrapLongitude());
    }

    @Test(timeout = 4000)
    public void testLawOfCosinesCalculator() throws Throwable {
        HashMap<String, String> config = new HashMap<>();
        config.put("distCalculator", "lawofcosines");
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();

        SpatialContext context = SpatialContextFactory.makeSpatialContext(config, classLoader);

        assertFalse(context.isNormWrapLongitude());
    }

    @Test(timeout = 4000)
    public void testHaversineCalculator() throws Throwable {
        HashMap<String, String> config = new HashMap<>();
        config.put("distCalculator", "haversine");
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();

        SpatialContext context = SpatialContextFactory.makeSpatialContext(config, classLoader);

        assertTrue(context.isGeo());
        assertFalse(context.isNormWrapLongitude());
    }

    @Test(timeout = 4000)
    public void testCartesianCalculator() throws Throwable {
        HashMap<String, String> config = new HashMap<>();
        config.putIfAbsent("distCalculator", "cartesian");
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();

        SpatialContext context = SpatialContextFactory.makeSpatialContext(config, classLoader);

        assertFalse(context.isNormWrapLongitude());
        assertTrue(context.isGeo());
    }

    @Test(timeout = 4000)
    public void testUnsupportedFieldType() throws Throwable {
        HashMap<String, String> config = new HashMap<>();
        config.put("readers", ",");
        SpatialContextFactory factory = new SpatialContextFactory();
        factory.args = config;

        try {
            factory.initField("readers");
            fail("Expecting exception: Error");
        } catch (Error e) {
            verifyException("org.locationtech.spatial4j.context.SpatialContextFactory", e);
        }
    }

    @Test(timeout = 4000)
    public void testGeoConfig() throws Throwable {
        HashMap<String, String> config = new HashMap<>();
        config.put("geo", "geo");
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();

        SpatialContext context = SpatialContextFactory.makeSpatialContext(config, classLoader);

        assertFalse(context.isNormWrapLongitude());
    }

    @Test(timeout = 4000)
    public void testValidSpatialContextFactory() throws Throwable {
        HashMap<String, String> config = new HashMap<>();
        config.put("spatialContextFactory", "org.locationtech.spatial4j.context.SpatialContextFactory");

        SpatialContext context = SpatialContextFactory.makeSpatialContext(config, null);

        assertFalse(context.isNormWrapLongitude());
        assertTrue(context.isGeo());
    }
}