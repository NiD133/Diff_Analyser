package org.locationtech.spatial4j.context;

import org.junit.Test;

/**
 * Unit tests for {@link SpatialContextFactory}.
 */
public class SpatialContextFactoryTest {

    /**
     * Verifies that calling the internal initCalculator() method without proper
     * initialization of the factory's arguments map results in a NullPointerException.
     * This test ensures the method is not robust against out-of-sequence calls.
     */
    @Test(expected = NullPointerException.class)
    public void initCalculator_whenArgsNotInitialized_throwsNullPointerException() {
        // Arrange: Create a factory instance directly.
        // The standard initialization via makeSpatialContext(args, loader) is skipped,
        // so the internal 'args' map remains null.
        SpatialContextFactory factory = new SpatialContextFactory();

        // Act: Call the protected initCalculator method.
        // This method expects the 'args' map to be non-null and will attempt to
        // access it, which should trigger the expected NullPointerException.
        factory.initCalculator();

        // Assert: The @Test(expected) annotation handles the exception verification.
    }
}