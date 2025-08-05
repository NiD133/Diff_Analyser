/*
 * Refactored for clarity and maintainability
 */
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

    // ===== Factory Initialization & Configuration Tests =====
    @Test(timeout = 4000)
    public void testInitWithEmptyArgsSucceeds() throws Throwable {
        HashMap<String, String> args = new HashMap<>();
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        SpatialContextFactory factory = new SpatialContextFactory();
        factory.init(args, classLoader);
        factory.initWorldBounds();
        assertFalse(factory.hasFormatConfig);
    }

    @Test(timeout = 4000)
    public void testInitFormatsWithEmptyArgsSucceeds() throws Throwable {
        SpatialContextFactory factory = new SpatialContextFactory();
        HashMap<String, String> args = new HashMap<>();
        factory.init(args, null);
        factory.initFormats();
        assertFalse(factory.hasFormatConfig);
    }

    @Test(timeout = 4000)
    public void testAddReaderWithNoggitSucceeds() throws Throwable {
        SpatialContextFactory factory = new SpatialContextFactory();
        factory.addReaderIfNoggitExists(PolyshapeReader.class);
        assertTrue(factory.geo);
    }

    @Test(timeout = 4000)
    public void testDefaultSpatialContextCreationSucceeds() throws Throwable {
        SpatialContextFactory factory = new SpatialContextFactory();
        factory.newSpatialContext();
        assertTrue(factory.geo);
    }

    // ===== Exception Handling & Edge Cases =====
    @Test(timeout = 4000)
    public void testInvalidShapeFactoryClassThrowsException() throws Throwable {
        HashMap<String, String> args = new HashMap<>();
        args.put("shapeFactoryClass", "org.locationtech.spatial4j.shape.impl.BufferedLineString");
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        
        try {
            SpatialContextFactory.makeSpatialContext(args, classLoader);
            fail("Expected RuntimeException due to invalid shape factory class");
        } catch (RuntimeException e) {
            assertTrue(e.getMessage().contains("needs a constructor that takes"));
        }
    }

    @Test(timeout = 4000)
    public void testInvalidFormatClassThrowsException() throws Throwable {
        HashMap<String, String> args = new HashMap<>();
        args.put("writers", ", worldBounds=");
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        
        try {
            SpatialContextFactory.makeSpatialContext(args, classLoader);
            fail("Expected RuntimeException due to invalid format class");
        } catch (RuntimeException e) {
            assertEquals("Unable to find format class", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testNullArgsThrowsNullPointerException() throws Throwable {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        try {
            SpatialContextFactory.makeSpatialContext(null, classLoader);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }

    @Test(timeout = 4000)
    public void testMissingFormatClassThrowsException() throws Throwable {
        SpatialContextFactory factory = new SpatialContextFactory();
        HashMap<String, String> args = new HashMap<>();
        args.put("readers", "invalidReader");
        factory.args = args;
        
        try {
            factory.initFormats();
            fail("Expected RuntimeException due to missing format class");
        } catch (RuntimeException e) {
            assertEquals("Unable to find format class", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testInitFieldWithEmptyNameThrowsError() throws Throwable {
        SpatialContextFactory factory = new SpatialContextFactory();
        try {
            factory.initField("");
            fail("Expected Error due to empty field name");
        } catch (Error e) {
            assertTrue(e.getMessage().contains("NoSuchFieldException"));
        }
    }

    // ===== Distance Calculator Configuration Tests =====
    @Test(timeout = 4000)
    public void testValidDistanceCalculatorsSucceed() throws Throwable {
        String[] calculators = {
            "cartesian^2", "vincentysphere", "lawofcosines", "haversine", "cartesian"
        };
        
        for (String calculator : calculators) {
            HashMap<String, String> args = new HashMap<>();
            args.put("distCalculator", calculator);
            ClassLoader classLoader = ClassLoader.getSystemClassLoader();
            SpatialContext ctx = SpatialContextFactory.makeSpatialContext(args, classLoader);
            assertNotNull(ctx);
            assertFalse(ctx.isNormWrapLongitude());
        }
    }

    @Test(timeout = 4000)
    public void testInvalidDistanceCalculatorThrowsException() throws Throwable {
        HashMap<String, String> args = new HashMap<>();
        args.put("distCalculator", "invalidCalculator");
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        
        try {
            SpatialContextFactory.makeSpatialContext(args, classLoader);
            fail("Expected RuntimeException due to invalid distance calculator");
        } catch (RuntimeException e) {
            assertEquals("Unknown calculator: invalidCalculator", e.getMessage());
        }
    }

    // ===== Format Configuration Tests =====
    @Test(timeout = 4000)
    public void testValidFormatConfigurationsSucceed() throws Throwable {
        HashMap<String, String> args = new HashMap<>();
        args.put("readers", ",");
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        SpatialContext ctx = SpatialContextFactory.makeSpatialContext(args, classLoader);
        assertTrue(ctx.isGeo());
    }

    @Test(timeout = 4000)
    public void testInvalidWktParserClassThrowsException() throws Throwable {
        HashMap<String, String> args = new HashMap<>();
        args.put("wktShapeParserClass", "invalidParser");
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        
        try {
            SpatialContextFactory.makeSpatialContext(args, classLoader);
            fail("Expected RuntimeException due to invalid WKT parser class");
        } catch (RuntimeException e) {
            assertEquals("Unable to find format class", e.getMessage());
        }
    }

    // ===== World Bounds Configuration Tests =====
    @Test(timeout = 4000)
    public void testWorldBoundsParsingSucceeds() throws Throwable {
        HashMap<String, String> args = new HashMap<>();
        args.put("worldBounds", "KLjy(V*@=mG*& @#o");
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        SpatialContext ctx = SpatialContextFactory.makeSpatialContext(args, classLoader);
        assertTrue(ctx.isGeo());
    }

    @Test(timeout = 4000)
    public void testUninitializedWorldBoundsThrowsException() throws Throwable {
        SpatialContextFactory factory = new SpatialContextFactory();
        try {
            factory.initWorldBounds();
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }

    // ===== Constructor Validation Tests =====
    @Test(timeout = 4000)
    public void testValidSpatialContextFactorySucceeds() throws Throwable {
        HashMap<String, String> args = new HashMap<>();
        args.put("spatialContextFactory", 
                 "org.locationtech.spatial4j.context.SpatialContextFactory");
        SpatialContext ctx = SpatialContextFactory.makeSpatialContext(args, null);
        assertTrue(ctx.isGeo());
    }

    @Test(timeout = 4000)
    public void testInvalidSpatialContextFactoryThrowsException() throws Throwable {
        HashMap<String, String> args = new HashMap<>();
        args.put("spatialContextFactory", "invalidFactory");
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        
        try {
            SpatialContextFactory.makeSpatialContext(args, classLoader);
            fail("Expected RuntimeException");
        } catch (RuntimeException e) {
            assertTrue(e.getMessage().contains("ClassNotFoundException"));
        }
    }
}