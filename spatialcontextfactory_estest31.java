package org.locationtech.spatial4j.context;

import org.junit.Test;
import org.locationtech.spatial4j.io.GeojsonShapeReader;
import org.locationtech.spatial4j.io.WktShapeReader;

import static org.junit.Assert.*;

/**
 * Tests for the {@link SpatialContextFactory} class, focusing on format creation.
 */
public class SpatialContextFactoryTest {

    /**
     * Verifies that calling {@link SpatialContextFactory#makeFormats(SpatialContext)} on a factory
     * with no explicit configuration will register the default format readers (WKT and GeoJSON).
     */
    @Test
    public void makeFormats_whenNoFormatsAreConfigured_shouldAddDefaultReaders() {
        // Arrange: Create a factory with its default configuration.
        // By default, no format readers are configured, and the 'hasFormatConfig' flag is false.
        SpatialContextFactory factory = new SpatialContextFactory();
        SpatialContext context = new SpatialContext(factory);

        assertTrue("Precondition: Factory should have no readers initially.", factory.readers.isEmpty());
        assertFalse("Precondition: hasFormatConfig should be false for a default factory.", factory.hasFormatConfig);

        // Act: Call the method under test. This should trigger the registration of default formats
        // because none were explicitly configured.
        factory.makeFormats(context);

        // Assert: Verify that the default readers have been added to the factory's list.
        assertEquals("Should have 2 default readers registered.", 2, factory.readers.size());
        assertTrue("WKT reader should be registered by default.", factory.readers.contains(WktShapeReader.class));
        assertTrue("GeoJSON reader should be registered by default.", factory.readers.contains(GeojsonShapeReader.class));

        // Assert that the 'hasFormatConfig' flag remains false, as the formats were added
        // by default, not through explicit user configuration.
        assertFalse("hasFormatConfig should remain false.", factory.hasFormatConfig);

        // Assert that other factory properties remain unchanged, confirming no unexpected side effects.
        assertTrue("The 'geo' property should not be changed.", factory.geo);
        assertFalse("The 'normWrapLongitude' property should not be changed.", factory.normWrapLongitude);
    }
}