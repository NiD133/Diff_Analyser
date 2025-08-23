package org.locationtech.spatial4j.context;

import org.junit.Test;

/**
 * Test suite for {@link SpatialContextFactory}.
 * This class focuses on testing specific behaviors of the factory initialization.
 */
public class SpatialContextFactoryTest {

    /**
     * Verifies that calling initFormats() on a SpatialContextFactory instance
     * created with the default constructor throws a NullPointerException.
     * <p>
     * This occurs because the default constructor does not initialize the internal 'args'
     * map, and initFormats() attempts to access it without a null check.
     */
    @Test(expected = NullPointerException.class)
    public void initFormats_whenArgsNotInitialized_throwsNullPointerException() {
        // Arrange: Create a factory using the default constructor.
        // In this state, the internal 'args' map is null.
        SpatialContextFactory factory = new SpatialContextFactory();

        // Act: Attempt to initialize formats. This is expected to fail.
        factory.initFormats();

        // Assert: The test passes if a NullPointerException is thrown,
        // which is handled by the @Test(expected=...) annotation.
    }
}