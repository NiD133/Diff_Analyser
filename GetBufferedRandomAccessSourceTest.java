package com.itextpdf.text.io;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for GetBufferedRandomAccessSource focused on very small sources.
 *
 * Historically, get(position) could throw ArrayIndexOutOfBoundsException
 * when the underlying source length was less than 4 bytes. These tests
 * ensure correct behavior for 1-, 2-, and 3-byte sources.
 */
public class GetBufferedRandomAccessSourceTest {

    @Test
    public void singleByteSource_getReturnsTheOnlyByte() throws Exception {
        // Given a 1-byte source
        byte[] data = new byte[] { 42 };
        ArrayRandomAccessSource underlying = new ArrayRandomAccessSource(data);
        GetBufferedRandomAccessSource buffered = new GetBufferedRandomAccessSource(underlying);

        // When reading the first (and only) byte
        int value = buffered.get(0);

        // Then the value is returned without throwing
        assertEquals("Expected to read the only byte from a 1-byte source", 42, value);
    }

    @Test
    public void twoByteSource_getReturnsEachByteByIndex() throws Exception {
        // Given a 2-byte source
        byte[] data = new byte[] { 10, 20 };
        ArrayRandomAccessSource underlying = new ArrayRandomAccessSource(data);
        GetBufferedRandomAccessSource buffered = new GetBufferedRandomAccessSource(underlying);

        // Then both bytes are readable by index without throwing
        assertEquals(10, buffered.get(0));
        assertEquals(20, buffered.get(1));
    }

    @Test
    public void threeByteSource_getReturnsEachByteByIndex() throws Exception {
        // Given a 3-byte source
        byte[] data = new byte[] { 1, 2, 3 };
        ArrayRandomAccessSource underlying = new ArrayRandomAccessSource(data);
        GetBufferedRandomAccessSource buffered = new GetBufferedRandomAccessSource(underlying);

        // Then all bytes are readable by index without throwing
        assertEquals(1, buffered.get(0));
        assertEquals(2, buffered.get(1));
        assertEquals(3, buffered.get(2));
    }
}