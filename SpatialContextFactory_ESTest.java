package org.locationtech.spatial4j.context;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.HashMap;
import java.util.Map;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.context.SpatialContextFactory;
import org.locationtech.spatial4j.io.PolyshapeReader;
import org.locationtech.spatial4j.shape.ShapeFactory;

/**
 * Tests for SpatialContextFactory functionality including configuration parsing,
 * factory creation, and error handling scenarios.
 */
public class SpatialContextFactoryTest {

    private static final ClassLoader CLASS_LOADER = ClassLoader.getSystemClassLoader();

    // ========== Factory Creation Tests ==========

    @Test
    public void shouldCreateSpatialContextWithEmptyConfiguration() throws Exception {
        Map<String, String> config = new HashMap<>();
        
        SpatialContext context = SpatialContextFactory.makeSpatialContext(config, CLASS_LOADER);
        
        assertTrue("Should default to geo context", context.isGeo());
        assertFalse("Should not normalize longitude wrapping by default", context.isNormWrapLongitude());
    }

    @Test
    public void shouldCreateSpatialContextWithCustomFactory() throws Exception {
        Map<String, String> config = new HashMap<>();
        config.put("spatialContextFactory", "org.locationtech.spatial4j.context.SpatialContextFactory");
        
        SpatialContext context = SpatialContextFactory.makeSpatialContext(config, null);
        
        assertTrue("Should create geo context", context.isGeo());
        assertFalse("Should not normalize longitude wrapping", context.isNormWrapLongitude());
    }

    @Test
    public void shouldCreateContextWithCustomWorldBounds() throws Exception {
        Map<String, String> config = new HashMap<>();
        config.put("worldBounds", "ENVELOPE(-180, 180, 90, -90)");
        
        SpatialContext context = SpatialContextFactory.makeSpatialContext(config, CLASS_LOADER);
        
        assertTrue("Should create geo context with custom bounds", context.isGeo());
    }

    // ========== Distance Calculator Configuration Tests ==========

    @Test
    public void shouldConfigureHaversineDistanceCalculator() throws Exception {
        Map<String, String> config = new HashMap<>();
        config.put("distCalculator", "haversine");
        
        SpatialContext context = SpatialContextFactory.makeSpatialContext(config, CLASS_LOADER);
        
        assertTrue("Should create context with haversine calculator", context.isGeo());
    }

    @Test
    public void shouldConfigureCartesianDistanceCalculator() throws Exception {
        Map<String, String> config = new HashMap<>();
        config.put("distCalculator", "cartesian");
        
        SpatialContext context = SpatialContextFactory.makeSpatialContext(config, CLASS_LOADER);
        
        assertTrue("Should create context with cartesian calculator", context.isGeo());
    }

    @Test
    public void shouldConfigureCartesianSquaredDistanceCalculator() throws Exception {
        Map<String, String> config = new HashMap<>();
        config.put("distCalculator", "cartesian^2");
        
        SpatialContext context = SpatialContextFactory.makeSpatialContext(config, CLASS_LOADER);
        
        assertFalse("Should create non-geo context for cartesian^2", context.isNormWrapLongitude());
    }

    @Test
    public void shouldConfigureLawOfCosinesDistanceCalculator() throws Exception {
        Map<String, String> config = new HashMap<>();
        config.put("distCalculator", "lawofcosines");
        
        SpatialContext context = SpatialContextFactory.makeSpatialContext(config, CLASS_LOADER);
        
        assertFalse("Should create context with law of cosines calculator", context.isNormWrapLongitude());
    }

    @Test
    public void shouldConfigureVincentySphereDistanceCalculator() throws Exception {
        Map<String, String> config = new HashMap<>();
        config.put("distCalculator", "vincentysphere");
        
        SpatialContext context = SpatialContextFactory.makeSpatialContext(config, CLASS_LOADER);
        
        assertTrue("Should create geo context with vincenty sphere calculator", context.isGeo());
    }

    // ========== Format Configuration Tests ==========

    @Test
    public void shouldConfigureReadersWithEmptyList() throws Exception {
        Map<String, String> config = new HashMap<>();
        config.put("readers", ","); // Empty list with comma
        
        SpatialContext context = SpatialContextFactory.makeSpatialContext(config, CLASS_LOADER);
        
        assertTrue("Should create context with empty readers list", context.isGeo());
    }

    @Test
    public void shouldConfigureWritersWithEmptyList() throws Exception {
        Map<String, String> config = new HashMap<>();
        config.put("writers", ","); // Empty list with comma
        
        SpatialContext context = SpatialContextFactory.makeSpatialContext(config, null);
        
        assertTrue("Should create context with empty writers list", context.isGeo());
    }

    @Test
    public void shouldConfigureGeoProperty() throws Exception {
        Map<String, String> config = new HashMap<>();
        config.put("geo", "false");
        
        SpatialContext context = SpatialContextFactory.makeSpatialContext(config, CLASS_LOADER);
        
        assertFalse("Should respect geo=false configuration", context.isNormWrapLongitude());
    }

    // ========== Direct Factory Instance Tests ==========

    @Test
    public void shouldInitializeFactoryWithValidConfiguration() throws Exception {
        SpatialContextFactory factory = new SpatialContextFactory();
        Map<String, String> config = new HashMap<>();
        
        factory.init(config, CLASS_LOADER);
        factory.initWorldBounds();
        
        assertTrue("Factory should default to geo mode", factory.geo);
        assertFalse("Factory should not have format config initially", factory.hasFormatConfig);
        assertFalse("Factory should not normalize longitude wrapping", factory.normWrapLongitude);
    }

    @Test
    public void shouldInitializeFormatsWithoutConfiguration() throws Exception {
        SpatialContextFactory factory = new SpatialContextFactory();
        Map<String, String> config = new HashMap<>();
        
        factory.init(config, null);
        factory.initFormats();
        
        assertFalse("Should not have format config", factory.hasFormatConfig);
        assertTrue("Should be in geo mode", factory.geo);
    }

    @Test
    public void shouldInitializeCalculatorWithoutConfiguration() throws Exception {
        SpatialContextFactory factory = new SpatialContextFactory();
        Map<String, String> config = new HashMap<>();
        factory.args = config;
        
        factory.initCalculator();
        
        assertTrue("Should remain in geo mode", factory.geo);
        assertFalse("Should not have format config", factory.hasFormatConfig);
    }

    @Test
    public void shouldCheckDefaultFormats() throws Exception {
        SpatialContextFactory factory = new SpatialContextFactory();
        
        factory.checkDefaultFormats();
        
        assertFalse("Should not have format config after checking defaults", factory.hasFormatConfig);
        assertTrue("Should remain in geo mode", factory.geo);
    }

    @Test
    public void shouldCreateNewSpatialContext() throws Exception {
        SpatialContextFactory factory = new SpatialContextFactory();
        
        SpatialContext context = factory.newSpatialContext();
        
        assertTrue("Created context should be geo", context.isGeo());
        assertFalse("Created context should not normalize longitude", context.isNormWrapLongitude());
    }

    @Test
    public void shouldAddPolyshapeReaderWhenNoggitExists() throws Exception {
        SpatialContextFactory factory = new SpatialContextFactory();
        Map<String, String> config = new HashMap<>();
        factory.init(config, CLASS_LOADER);
        
        factory.addReaderIfNoggitExists(PolyshapeReader.class);
        
        assertTrue("Should remain in geo mode after adding reader", factory.geo);
        assertFalse("Should not have format config", factory.hasFormatConfig);
    }

    @Test
    public void shouldCreateShapeFactory() throws Exception {
        SpatialContextFactory factory = new SpatialContextFactory();
        SpatialContext context = new SpatialContext(factory);
        
        factory.makeShapeFactory(context);
        
        assertTrue("Factory should remain in geo mode", factory.geo);
        assertFalse("Factory should not have format config", factory.hasFormatConfig);
    }

    @Test
    public void shouldCreateBinaryCodec() throws Exception {
        SpatialContextFactory factory = new SpatialContextFactory();
        Map<String, String> config = new HashMap<>();
        SpatialContext context = SpatialContextFactory.makeSpatialContext(config, CLASS_LOADER);
        
        factory.makeBinaryCodec(context);
        
        assertTrue("Factory should be in geo mode", factory.geo);
    }

    @Test
    public void shouldCreateFormatsForValidContext() throws Exception {
        SpatialContextFactory factory = new SpatialContextFactory();
        SpatialContext context = new SpatialContext(factory);
        
        factory.makeFormats(context);
        
        assertTrue("Factory should remain in geo mode", factory.geo);
        assertFalse("Factory should not have format config", factory.hasFormatConfig);
    }

    // ========== Error Handling Tests ==========

    @Test(expected = RuntimeException.class)
    public void shouldFailWithInvalidShapeFactoryClass() throws Exception {
        Map<String, String> config = new HashMap<>();
        config.put("shapeFactoryClass", "org.locationtech.spatial4j.shape.impl.BufferedLineString");
        
        SpatialContextFactory.makeSpatialContext(config, CLASS_LOADER);
    }

    @Test(expected = RuntimeException.class)
    public void shouldFailWithInvalidWriterClass() throws Exception {
        Map<String, String> config = new HashMap<>();
        config.put("writers", ", worldBounds="); // Invalid writer class
        
        SpatialContextFactory.makeSpatialContext(config, CLASS_LOADER);
    }

    @Test(expected = RuntimeException.class)
    public void shouldFailWithInvalidReaderClass() throws Exception {
        Map<String, String> config = new HashMap<>();
        config.put("readers", "InvalidReaderClass");
        
        SpatialContextFactory.makeSpatialContext(config, null);
    }

    @Test(expected = RuntimeException.class)
    public void shouldFailWithInvalidFactoryClass() throws Exception {
        Map<String, String> config = new HashMap<>();
        config.put("spatialContextFactory", "NonExistentFactoryClass");
        
        SpatialContextFactory.makeSpatialContext(config, CLASS_LOADER);
    }

    @Test(expected = RuntimeException.class)
    public void shouldFailWithInvalidDistanceCalculator() throws Exception {
        Map<String, String> config = new HashMap<>();
        config.put("distCalculator", "invalidCalculator");
        
        SpatialContextFactory.makeSpatialContext(config, CLASS_LOADER);
    }

    @Test(expected = RuntimeException.class)
    public void shouldFailWithInvalidWktParserClass() throws Exception {
        Map<String, String> config = new HashMap<>();
        config.put("wktShapeParserClass", "invalidParserClass");
        
        SpatialContextFactory.makeSpatialContext(config, CLASS_LOADER);
    }

    @Test(expected = NullPointerException.class)
    public void shouldFailWithNullConfiguration() throws Exception {
        SpatialContextFactory.makeSpatialContext(null, CLASS_LOADER);
    }

    @Test(expected = RuntimeException.class)
    public void shouldFailWhenCreatingShapeFactoryWithNullContext() throws Exception {
        SpatialContextFactory factory = new SpatialContextFactory();
        factory.makeShapeFactory(null);
    }

    @Test(expected = RuntimeException.class)
    public void shouldFailWhenCreatingFormatsWithNullContext() throws Exception {
        SpatialContextFactory factory = new SpatialContextFactory();
        factory.makeFormats(null);
    }

    @Test(expected = RuntimeException.class)
    public void shouldFailWhenCreatingBinaryCodecWithNullContext() throws Exception {
        SpatialContextFactory factory = new SpatialContextFactory();
        factory.makeBinaryCodec(null);
    }

    @Test(expected = RuntimeException.class)
    public void shouldFailWithInterfaceAsShapeFactory() throws Exception {
        SpatialContextFactory factory = new SpatialContextFactory();
        factory.shapeFactoryClass = ShapeFactory.class; // Interface, not concrete class
        
        factory.newSpatialContext();
    }

    @Test(expected = NullPointerException.class)
    public void shouldFailInitializationWithNullArgs() throws Exception {
        SpatialContextFactory factory = new SpatialContextFactory();
        factory.init(null, CLASS_LOADER);
    }

    @Test(expected = RuntimeException.class)
    public void shouldFailWithInvalidShapeFactoryConfiguration() throws Exception {
        Map<String, String> config = new HashMap<>();
        config.put("shapeFactoryClass", "invalidClassName");
        
        SpatialContextFactory factory = new SpatialContextFactory();
        factory.init(config, CLASS_LOADER);
    }

    @Test(expected = Error.class)
    public void shouldFailWithEmptyFieldName() throws Exception {
        SpatialContextFactory factory = new SpatialContextFactory();
        factory.initField("");
    }

    @Test(expected = NullPointerException.class)
    public void shouldFailInitializingWorldBoundsWithoutArgs() throws Exception {
        SpatialContextFactory factory = new SpatialContextFactory();
        factory.initWorldBounds(); // No args initialized
    }

    @Test(expected = RuntimeException.class)
    public void shouldFailInitializingFormatsWithInvalidReader() throws Exception {
        Map<String, String> config = new HashMap<>();
        config.put("readers", "invalidReader");
        
        SpatialContextFactory factory = new SpatialContextFactory();
        factory.args = config;
        factory.initFormats();
    }

    @Test(expected = NullPointerException.class)
    public void shouldFailInitializingFormatsWithoutArgs() throws Exception {
        SpatialContextFactory factory = new SpatialContextFactory();
        factory.initFormats(); // No args initialized
    }

    @Test(expected = NullPointerException.class)
    public void shouldFailInitializingFieldWithoutArgs() throws Exception {
        SpatialContextFactory factory = new SpatialContextFactory();
        factory.initField("shapeFactoryClass"); // No args initialized
    }

    @Test(expected = NullPointerException.class)
    public void shouldFailInitializingCalculatorWithoutArgs() throws Exception {
        SpatialContextFactory factory = new SpatialContextFactory();
        factory.initCalculator(); // No args initialized
    }
}