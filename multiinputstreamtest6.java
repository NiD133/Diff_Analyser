package com.google.common.io;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import org.jspecify.annotations.NullUnmarked;

/**
 * Tests for {@link MultiInputStream}, focusing on edge cases and potential performance issues.
 */
@NullUnmarked
public class MultiInputStreamTest extends IoTestCase {

    /**
     * A large number of sources used to test for stack overflow issues when advancing through
     * many empty streams.
     */
    private static final int LARGE_NUMBER_OF_EMPTY_SOURCES = 10_000_000;

    /**
     * Verifies that creating a {@link MultiInputStream} with a huge number of empty sources
     * does not cause a {@link StackOverflowError} when read.
     *
     * <p>This test specifically addresses a bug where advancing to the next stream was implemented
     * recursively, leading to stack overflow with many empty sources.
     *
     * @see <a href="https://github.com/google/guava/issues/2996">Guava issue #2996</a>
     */
    public void testRead_withManyEmptySources_doesNotCauseStackOverflow() throws IOException {
        // Arrange: Create a MultiInputStream from a very large number of empty sources.
        // The core of the test is to ensure this setup doesn't break the stream's implementation.
        InputStream streamWithManyEmptySources = createStreamFromManyEmptySources();
        byte[] buffer = new byte[16]; // Buffer size is arbitrary.

        // Act: Attempt to read from the stream. This will trigger the internal `advance()`
        // method repeatedly until the end of all concatenated streams is reached.
        int bytesRead = streamWithManyEmptySources.read(buffer);

        // Assert: The primary assertion is implicit: no StackOverflowError was thrown.
        // We also assert that the stream correctly reports its end.
        assertEquals("Should reach the end of the stream without error", -1, bytesRead);
    }

    /** Creates a MultiInputStream from a large number of empty ByteSources. */
    private static MultiInputStream createStreamFromManyEmptySources() throws IOException {
        return new MultiInputStream(
            Collections.nCopies(LARGE_NUMBER_OF_EMPTY_SOURCES, ByteSource.empty()).iterator());
    }
}