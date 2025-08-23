package org.locationtech.spatial4j.context;

import org.junit.Test;
import org.locationtech.spatial4j.context.jts.JtsSpatialContext;
import org.locationtech.spatial4j.context.jts.JtsSpatialContextFactory;
import org.locationtech.spatial4j.io.ShapeIO;
import org.locationtech.spatial4j.io.ShapeReader;
import org.locationtech.spatial4j.io.WKTReader;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link SpatialContextFactory} focusing on its ability to configure
 * components like custom format readers.
 */
public class SpatialContextFactoryTest {

    /**
     * A test-specific implementation of WKTReader to verify that the factory
     * can instantiate and register custom format readers.
     */
    public static class CustomWktReader extends WKTReader {
        public CustomWktReader(JtsSpatialContext ctx, JtsSpatialContextFactory factory) {
            super(ctx, factory);
        }
    }

    @Test
    public void factoryShouldCreateContextWithCustomReader() {
        // Arrange: Define configuration to use a JTS context and our custom WKT reader.
        Map<String, String> args = new HashMap<>();
        args.put("spatialContextFactory", JtsSpatialContextFactory.class.getName());
        args.put("readers", CustomWktReader.class.getName());

        // Act: Create the SpatialContext using the factory with the specified arguments.
        SpatialContext context = SpatialContextFactory.makeSpatialContext(args, getClass().getClassLoader());

        // Assert: Verify the context is a JTS context and uses the specified custom reader.
        assertTrue("Context should be a JtsSpatialContext", context instanceof JtsSpatialContext);

        ShapeReader wktReader = context.getFormats().getReader(ShapeIO.WKT);
        assertNotNull("A WKT reader should be available", wktReader);
        assertTrue(
                "The registered WKT reader should be our custom implementation",
                wktReader instanceof CustomWktReader
        );
    }
}