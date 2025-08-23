package org.locationtech.spatial4j.context;

import org.junit.Test;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for {@link SpatialContextFactory}.
 */
public class SpatialContextFactoryTest {

    /**
     * This test verifies that the SpatialContextFactory can gracefully handle a 'readers'
     * configuration property that is technically present but effectively empty.
     * A value of "," for the 'readers' key will be split into empty strings,
     * which the factory should ignore, resulting in a SpatialContext with default settings.
     */
    @Test
    public void makeSpatialContext_withMalformedReadersList_shouldCreateContextWithDefaults() {
        // Arrange: Create a configuration map where the 'readers' list is malformed.
        // This tests the robustness of the configuration parser.
        Map<String, String> config = new HashMap<>();
        config.put("readers", ",");
        
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();

        // Act: Create a SpatialContext using the factory method with the test configuration.
        SpatialContext context = SpatialContextFactory.makeSpatialContext(config, classLoader);

        // Assert: The factory should fall back to default settings for the context.
        // The default context is geographic and does not wrap the longitude.
        assertTrue("Context should be geographic by default", context.isGeo());
        assertFalse("Context should not wrap longitude by default", context.isNormWrapLongitude());
    }
}