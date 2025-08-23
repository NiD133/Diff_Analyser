package org.locationtech.spatial4j.context;

import org.junit.Test;

/**
 * Unit tests for {@link SpatialContextFactory}.
 */
public class SpatialContextFactoryTest {

    /**
     * Tests that {@link SpatialContextFactory#makeFormats(SpatialContext)} throws a
     * {@link NullPointerException} when the provided context is null.
     * This test ensures that the method correctly handles invalid input by failing fast.
     */
    @Test(expected = NullPointerException.class)
    public void makeFormats_givenNullContext_throwsNullPointerException() {
        // Arrange
        SpatialContextFactory factory = new SpatialContextFactory();

        // Act
        // The following call is expected to throw a NullPointerException because
        // the context argument cannot be null.
        factory.makeFormats(null);

        // Assert
        // The test passes if the expected exception is thrown, as declared
        // in the @Test annotation. No further assertions are needed.
    }
}