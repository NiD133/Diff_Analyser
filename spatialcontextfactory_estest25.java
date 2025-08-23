package org.locationtech.spatial4j.context;

import org.junit.Test;

/**
 * Test suite for {@link SpatialContextFactory}.
 * This class focuses on testing the initialization logic.
 */
public class SpatialContextFactory_ESTestTest25 extends SpatialContextFactory_ESTest_scaffolding {

    /**
     * Verifies that the init() method throws a NullPointerException
     * when the configuration arguments map is null, as this is a required parameter.
     */
    @Test(expected = NullPointerException.class)
    public void init_withNullArgsMap_shouldThrowNullPointerException() {
        // Arrange
        SpatialContextFactory factory = new SpatialContextFactory();
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();

        // Act & Assert
        // The init method is expected to throw a NullPointerException because the 'args' map is null.
        // The @Test(expected = ...) annotation handles the verification.
        factory.init(null, classLoader);
    }
}