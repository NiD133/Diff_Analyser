package com.google.common.io;

import static org.junit.Assert.assertEquals;

import com.google.common.collect.ImmutableList;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import org.junit.Test;

/**
 * Tests for {@link MultiInputStream}.
 */
public class MultiInputStreamTest {

    /**
     * Verifies that the available() method returns the number of bytes available
     * from the current underlying stream when the MultiInputStream is constructed
     * with a single source.
     */
    @Test
    public void available_withSingleStream_returnsStreamAvailableBytes() throws IOException {
        // Arrange
        final int sourceSize = 8;
        byte[] sourceData = new byte[sourceSize];
        ByteSource singleByteSource = ByteSource.wrap(sourceData);
        Iterator<ByteSource> sourcesIterator = ImmutableList.of(singleByteSource).iterator();

        // Act
        // The MultiInputStream constructor opens the first stream.
        InputStream multiInputStream = new MultiInputStream(sourcesIterator);
        int availableBytes = multiInputStream.available();

        // Assert
        assertEquals("The available bytes should match the size of the single source.",
            sourceSize, availableBytes);
    }
}