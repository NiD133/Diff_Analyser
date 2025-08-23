package org.apache.commons.io.output;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * This test class has been improved to focus on a specific error handling scenario
 * in {@link DeferredFileOutputStream}, a subclass of {@link ThresholdingOutputStream}.
 */
public class ThresholdingOutputStream_ESTestTest10 {

    /**
     * Tests that an {@link IllegalArgumentException} is thrown when writing to a
     * {@link DeferredFileOutputStream} if the write operation causes the memory
     * threshold to be exceeded and the configured temporary file prefix is invalid.
     * <p>
     * The exception is expected because exceeding the threshold triggers the creation
     * of a temporary file, which fails due to the invalid prefix.
     * </p>
     */
    @Test(timeout = 4000)
    public void testWriteBeyondThresholdWithInvalidPrefixThrowsIllegalArgumentException() {
        // Arrange: Set up a stream with a low threshold and an invalid file prefix.
        final int thresholdBytes = 16;
        // This prefix is invalid because it contains a file separator character, which is
        // not allowed by the underlying temporary file creation mechanism.
        final String invalidPrefix = "invalid/prefix-";

        final DeferredFileOutputStream.Builder builder = new DeferredFileOutputStream.Builder()
                .setThreshold(thresholdBytes)
                .setPrefix(invalidPrefix);

        // Act & Assert: Verify that an exception is thrown when the write operation
        // exceeds the threshold, triggering a failed attempt to create a temporary file.
        final IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
            try (DeferredFileOutputStream stream = builder.get()) {
                stream.write(new byte[thresholdBytes + 1]);
            }
        });

        // Further assert that the exception message confirms the cause of the failure.
        assertTrue(
            "Expected exception message to contain 'Invalid prefix or suffix'",
            e.getMessage().contains("Invalid prefix or suffix")
        );
    }
}