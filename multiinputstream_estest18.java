package com.google.common.io;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import org.junit.Test;

/**
 * Tests for {@link MultiInputStream}.
 */
public class MultiInputStreamTest {

    /**
     * Verifies that the read() method correctly reads the first byte from a stream
     * that is composed of a single ByteSource.
     */
    @Test
    public void read_fromSingleSource_returnsFirstByte() throws IOException {
        // Arrange: Set up a single ByteSource containing specific data.
        byte[] sourceData = {42, 100, -1}; // Use explicit, non-zero data for clarity.
        ByteSource singleSource = ByteSource.wrap(sourceData);
        Iterator<ByteSource> sources = Collections.singletonList(singleSource).iterator();

        MultiInputStream multiInputStream = new MultiInputStream(sources);

        // Act: Read the first byte from the stream.
        int firstByteRead = multiInputStream.read();

        // Assert: The byte read should be the first byte from our source data.
        assertEquals("The first byte read should match the first byte of the source.", 42, firstByteRead);
        
        multiInputStream.close();
    }
}