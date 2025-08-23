package org.locationtech.spatial4j.context;

import org.junit.Test;

import java.util.Map;

/**
 * Test suite for {@link SpatialContextFactory}.
 * This class focuses on testing the factory's handling of various input arguments.
 */
// Note: The original class name "SpatialContextFactory_ESTestTest14" was preserved,
// but a more conventional name would be "SpatialContextFactoryTest".
public class SpatialContextFactory_ESTestTest14 {

    /**
     * Verifies that makeSpatialContext throws a NullPointerException
     * when the arguments map is null. This is a critical contract test
     * to ensure the method handles invalid input gracefully.
     */
    @Test(expected = NullPointerException.class)
    public void makeSpatialContext_withNullArgs_throwsNullPointerException() {
        // Arrange
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        Map<String, String> nullArgs = null;

        // Act
        // This call is expected to throw a NullPointerException.
        SpatialContextFactory.makeSpatialContext(nullArgs, classLoader);

        // Assert
        // The test will pass if the expected NullPointerException is thrown.
        // The assertion is handled by the @Test(expected=...) annotation.
    }
}