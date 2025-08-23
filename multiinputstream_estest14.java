package com.google.common.io;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * This test class contains refactored tests for {@link MultiInputStream}.
 * The original test class name {@code MultiInputStream_ESTestTest14} suggests it was
 * auto-generated. This version is functionally equivalent but prioritizes human
 * readability and standard testing practices.
 */
public class MultiInputStream_ESTestTest14 { // Retaining original class name as requested

    @Test(timeout = 4000)
    public void skip_whenRequestedBytesExceedStreamLength_skipsAllRemainingBytes() throws IOException {
        // Arrange: Create a MultiInputStream from several sources with a total of 6 bytes.
        // The sources are structured as [empty, 6 bytes, empty] to test that the
        // MultiInputStream correctly transitions between underlying streams.
        final int dataSize = 6;
        byte[] data = new byte[dataSize];

        List<ByteSource> sources = Arrays.asList(
            ByteSource.empty(),
            ByteSource.wrap(data),
            ByteSource.empty()
        );

        Iterator<ByteSource> sourcesIterator = sources.iterator();
        InputStream multiInputStream = new MultiInputStream(sourcesIterator);

        // Act: Attempt to skip more bytes than are available in the entire stream.
        long bytesToSkip = 100L;
        long actualBytesSkipped = multiInputStream.skip(bytesToSkip);

        // Assert: The number of skipped bytes should be exactly the total number of bytes
        // available in the stream, and the stream should be at its end.
        assertEquals("Should skip all available bytes", dataSize, actualBytesSkipped);
        assertEquals("Stream should be at the end", -1, multiInputStream.read());
    }
}