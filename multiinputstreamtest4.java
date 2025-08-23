package com.google.common.io;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import org.jspecify.annotations.NullUnmarked;

/**
 * Tests for {@link MultiInputStream}.
 * This file focuses on specific behaviors, such as handling uncooperative underlying streams.
 */
@NullUnmarked
public class MultiInputStreamTest extends IoTestCase {

    /**
     * An InputStream that simulates an underlying stream that doesn't support skipping efficiently.
     * This is a valid, though non-performant, implementation of an InputStream, which should be
     * handled gracefully by consumers like {@link MultiInputStream}.
     */
    private static class UncooperativeSkipInputStream extends ByteArrayInputStream {
        UncooperativeSkipInputStream(byte[] buf) {
            super(buf);
        }

        /** This stream is "uncooperative" and always reports that 0 bytes were skipped. */
        @Override
        public long skip(long n) {
            return 0;
        }
    }

    /**
     * Tests that {@code MultiInputStream} correctly skips bytes even when its underlying stream has an
     * "uncooperative" {@code skip()} method that always returns 0. In this scenario, robust stream
     * consumers are expected to fall back to reading and discarding bytes to perform the skip.
     */
    public void testSkip_whenUnderlyingStreamIsUncooperative() throws IOException {
        // Arrange: Create a MultiInputStream from a source whose stream always returns 0 from skip().
        // The stream contains 50 bytes with values 0, 1, ..., 49.
        byte[] data = newPreFilledByteArray(0, 50);
        ByteSource uncooperativeSource = new ByteSource() {
            @Override
            public InputStream openStream() {
                return new UncooperativeSkipInputStream(data);
            }
        };
        MultiInputStream multiStream =
                new MultiInputStream(Collections.singleton(uncooperativeSource).iterator());

        // Assert behavior for edge cases before the main action.
        assertThat(multiStream.skip(-1)).isEqualTo(0L); // Skipping a negative number should do nothing.
        assertThat(multiStream.skip(0)).isEqualTo(0L); // Skipping zero bytes should do nothing.

        // Act: Use a utility that relies on the stream's skip() method to advance.
        ByteStreams.skipFully(multiStream, 20);

        // Assert: We should have skipped the first 20 bytes. The next byte read should be the 21st
        // byte, which has a value of 20.
        assertEquals(20, multiStream.read());
    }
}