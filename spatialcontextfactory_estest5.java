package org.locationtech.spatial4j.context;

import org.junit.Test;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link SpatialContextFactory} class.
 */
public class SpatialContextFactoryTest {

    /**
     * Verifies that calling {@link SpatialContextFactory#initFormats()} with an
     * empty configuration does not set the {@code hasFormatConfig} flag.
     */
    @Test
    public void initFormats_withEmptyConfiguration_shouldNotSetHasFormatConfigFlag() {
        // Arrange: Create a factory and initialize it with an empty configuration map.
        SpatialContextFactory factory = new SpatialContextFactory();
        Map<String, String> emptyArgs = new HashMap<>();
        factory.init(emptyArgs, null);

        // Act: Call the method responsible for initializing shape formats.
        factory.initFormats();

        // Assert: Verify that the hasFormatConfig flag is false, as no format
        // configuration (e.g., 'readers' or 'writers') was provided.
        assertFalse(
            "hasFormatConfig should be false when no format keys are in the arguments",
            factory.hasFormatConfig
        );

        // Also, confirm that other properties retain their default values, ensuring
        // that initFormats() has no unintended side effects.
        assertTrue("The 'geo' property should retain its default value of true", factory.geo);
        assertFalse("The 'normWrapLongitude' property should retain its default value of false", factory.normWrapLongitude);
    }
}