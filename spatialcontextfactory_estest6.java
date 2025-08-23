package org.locationtech.spatial4j.context;

import org.junit.Test;
import java.util.Collections;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link SpatialContextFactory} class.
 * This class focuses on testing the initialization logic.
 */
public class SpatialContextFactoryTest {

    /**
     * Tests that calling initField with a key that is not present in the arguments map
     * does not alter the factory's default state.
     */
    @Test
    public void initFieldWithMissingKeyShouldNotAlterFactoryState() {
        // Arrange: Create a factory and provide it with an empty map of arguments.
        SpatialContextFactory factory = new SpatialContextFactory();
        factory.args = Collections.emptyMap();

        // Act: Attempt to initialize the "readers" field. Since the "readers" key
        // is not in the args map, this call should be a no-op.
        factory.initField("readers");

        // Assert: Verify that the factory's properties retain their default values,
        // confirming that the initField call had no unintended side effects.
        assertTrue("The 'geo' property should be true by default", factory.geo);
        assertFalse("The 'normWrapLongitude' property should be false by default", factory.normWrapLongitude);
        assertFalse("The 'hasFormatConfig' property should be false by default", factory.hasFormatConfig);
    }
}