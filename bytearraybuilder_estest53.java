package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link ByteArrayBuilder} class.
 */
public class ByteArrayBuilderTest {

    /**
     * Verifies that the release() method can be called multiple times without
     * causing errors or side effects. This ensures the method is idempotent.
     */
    @Test
    public void releaseShouldBeSafeToCallMultipleTimes() {
        // Arrange: Create a ByteArrayBuilder with a buffer recycler.
        // The recycler is needed to test the full logic of the release() method.
        BufferRecycler bufferRecycler = new BufferRecycler();
        ByteArrayBuilder builder = new ByteArrayBuilder(bufferRecycler);

        // Act: Call release() twice. The second call should be a safe no-op.
        builder.release();
        builder.release();

        // Assert: The builder should be in a clean state after being released.
        // The current segment length is expected to be 0.
        // This test also implicitly verifies that the second call to release()
        // does not throw an exception.
        assertEquals(0, builder.getCurrentSegmentLength());
    }
}