package org.locationtech.spatial4j.context;

import org.junit.Test;
import org.locationtech.spatial4j.io.GeoJSONReader;
import org.locationtech.spatial4j.io.WktShapeParser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for the {@link SpatialContextFactory} class, focusing on the
 * configuration of default formats.
 */
public class SpatialContextFactoryTest {

    @Test
    public void checkDefaultFormats_whenNoFormatsConfigured_shouldAddWktAndGeoJsonReaders() {
        // Arrange: Create a factory in its default state where no formats are configured.
        SpatialContextFactory factory = new SpatialContextFactory();
        
        // Pre-condition checks to ensure the initial state is as expected.
        assertFalse("Pre-condition: hasFormatConfig should be false", factory.hasFormatConfig);
        assertTrue("Pre-condition: readers list should be empty", factory.readers.isEmpty());

        // Act: Call the method under test.
        factory.checkDefaultFormats();

        // Assert: Verify that the default readers were added and the state is correct.
        assertEquals("Should have added 2 default readers", 2, factory.readers.size());
        assertTrue("WKT reader should have been added", factory.readers.contains(WktShapeParser.class));
        assertTrue("GeoJSON reader should have been added", factory.readers.contains(GeoJSONReader.class));

        // The method should not change the hasFormatConfig flag itself.
        assertFalse("hasFormatConfig flag should remain false", factory.hasFormatConfig);
    }

    @Test
    public void checkDefaultFormats_whenFormatsAreAlreadyConfigured_shouldDoNothing() {
        // Arrange: Create a factory and set hasFormatConfig to true to simulate
        // that formats have already been configured.
        SpatialContextFactory factory = new SpatialContextFactory();
        factory.hasFormatConfig = true;

        // Act: Call the method under test.
        factory.checkDefaultFormats();

        // Assert: Verify that no readers were added because formats were already configured.
        assertTrue("Readers list should remain empty", factory.readers.isEmpty());
    }
}