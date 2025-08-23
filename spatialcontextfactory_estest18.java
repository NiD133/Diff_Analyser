package org.locationtech.spatial4j.context;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Test suite for {@link SpatialContextFactory}.
 */
public class SpatialContextFactoryTest {

    /**
     * Tests that makeBinaryCodec throws a RuntimeException when the provided
     * SpatialContext is null, as this leads to a NullPointerException internally.
     */
    @Test
    public void makeBinaryCodec_withNullContext_throwsRuntimeException() {
        // Arrange: Create an instance of the factory.
        SpatialContextFactory factory = new SpatialContextFactory();

        // Act & Assert: Attempt to create a binary codec with a null context.
        try {
            factory.makeBinaryCodec(null);
            fail("Expected a RuntimeException to be thrown when the context is null.");
        } catch (RuntimeException e) {
            // Verify that the exception was thrown for the correct reason.
            // The underlying implementation throws a NullPointerException,
            // which is then wrapped in a RuntimeException.
            Throwable cause = e.getCause();
            assertNotNull("The RuntimeException should have a cause.", cause);
            assertTrue(
                "The cause of the exception should be a NullPointerException.",
                cause instanceof NullPointerException
            );
        }
    }
}