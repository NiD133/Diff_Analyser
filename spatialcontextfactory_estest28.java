package org.locationtech.spatial4j.context;

import org.junit.Test;
import org.locationtech.spatial4j.io.BinaryCodec;
import org.locationtech.spatial4j.shape.impl.ShapeFactoryImpl;

import static org.junit.Assert.*;

/**
 * Tests for {@link SpatialContextFactory} to verify its initial state and default configuration.
 */
public class SpatialContextFactoryTest {

    /**
     * Verifies that a newly instantiated SpatialContextFactory is configured with the expected
     * default values. This test serves as documentation for the factory's default behavior.
     */
    @Test
    public void newFactoryShouldHaveDefaultSettings() {
        // Arrange: Create a new factory instance.
        SpatialContextFactory factory = new SpatialContextFactory();

        // Act: No action is needed as we are testing the state immediately after construction.

        // Assert: Verify that the factory's public fields are set to their expected defaults.
        assertTrue("Should be geographic (geo=true) by default", factory.geo);
        assertFalse("Longitude wrapping should be disabled by default", factory.normWrapLongitude);
        assertFalse("Should not have format configurations by default", factory.hasFormatConfig);

        // Assert other important defaults for a more complete picture of the initial state.
        assertNull("Distance calculator should be null by default (determined by context)", factory.distCalc);
        assertNull("World bounds should be null by default (determined by context)", factory.worldBounds);
        assertEquals("Default shape factory should be ShapeFactoryImpl",
                ShapeFactoryImpl.class, factory.shapeFactoryClass);
        assertEquals("Default binary codec should be BinaryCodec",
                BinaryCodec.class, factory.binaryCodecClass);
        assertTrue("Readers list should be empty by default", factory.readers.isEmpty());
        assertTrue("Writers list should be empty by default", factory.writers.isEmpty());
    }
}