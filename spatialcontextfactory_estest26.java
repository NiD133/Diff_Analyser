package org.locationtech.spatial4j.context;

import org.junit.Test;
import org.locationtech.spatial4j.io.PolyshapeReader;

import java.util.Collections;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Tests for the {@link SpatialContextFactory} class.
 */
public class SpatialContextFactoryTest {

    @Test
    public void addReaderIfNoggitExists_shouldAddReader_whenNotAlreadyPresent() {
        // Arrange
        SpatialContextFactory factory = new SpatialContextFactory();
        Map<String, String> emptyConfig = Collections.emptyMap();
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();

        // The init method is a prerequisite for many factory operations.
        factory.init(emptyConfig, classLoader);
        assertTrue("The readers list should be empty before the test.", factory.readers.isEmpty());

        // Act
        // Attempt to add a new shape reader class to the factory's configuration.
        factory.addReaderIfNoggitExists(PolyshapeReader.class);

        // Assert
        // 1. Verify the primary outcome: the reader was successfully added.
        assertEquals("There should be exactly one reader in the list.", 1, factory.readers.size());
        assertTrue("The readers list should contain PolyshapeReader.", factory.readers.contains(PolyshapeReader.class));

        // 2. Verify no unintended side effects on other factory properties.
        assertTrue("The 'geo' flag should retain its default value of true.", factory.geo);
        assertFalse("The 'hasFormatConfig' flag should not be modified by this operation.", factory.hasFormatConfig);
        assertFalse("The 'normWrapLongitude' flag should retain its default value of false.", factory.normWrapLongitude);
    }
}