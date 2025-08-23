package org.locationtech.spatial4j.context;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for {@link SpatialContextFactory}.
 */
public class SpatialContextFactoryTest {

    /**
     * Verifies that a newly instantiated SpatialContextFactory is configured with
     * the expected default values.
     */
    @Test
    public void newFactoryShouldHaveDefaultConfiguration() {
        // Arrange: Create a new SpatialContextFactory instance.
        SpatialContextFactory factory = new SpatialContextFactory();

        // Act: No action is needed as we are testing the initial state after construction.

        // Assert: Verify the default property values.
        // By default, the factory should be configured for a geographic coordinate system.
        assertTrue("Factory should default to geo=true", factory.geo);

        // By default, longitude wrapping should be disabled.
        assertFalse("Factory should default to normWrapLongitude=false", factory.normWrapLongitude);
    }
}