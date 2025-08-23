package com.fasterxml.jackson.core.io;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Readability-first tests for MergedStream.
 * These tests focus on the core contract:
 * - Read from the provided prefix buffer first, then from the underlying stream.
 * - Support for skip, available, and read overloads.
 * - Behavior when no prefix is provided (pure delegation).
 * - Delegation of mark/reset/close when applicable.
 */
class MergedStreamTest {

    private static MergedStream merged(byte[] prefix, String underlying) {
        InputStream in = new ByteArrayInputStream(underlying.getBytes(StandardCharsets.UTF_8));
        int start = 0;
        int end = (prefix == null) ? 0 : prefix.length;
        return new MergedStream(null, in, prefix, start, end);
    }

    @Test
    void readsPrefixThenDelegates_readSingleBytes() throws IOException {
        MergedStream in = merged("abc".getBytes(StandardCharsets.US_ASCII), "XYZ");

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int r;
        while ((r = in.read()) != -1) {
            out.write(r);
        }

        assertEquals("abcXYZ", out.toString(StandardCharsets.US_ASCII));
    }

    @Test
    void readIntoArray_spansPrefixAndUnderlying() throws IOException {
        MergedStream in = merged("hello".getBytes(StandardCharsets.US_ASCII), "world");

        byte[] buf = new byte[7];
        int n = in.read(buf, 0, buf.length);
        assertEquals(7, n);
        assertEquals("hellowo", new String(buf, StandardCharsets.US_ASCII));

        byte[] rest = new byte[10];
        n = in.read(rest, 0, rest.length);
        assertEquals(3, n);
        assertEquals("rld", new String(rest, 0, n, StandardCharsets.US_ASCII));

        assertEquals(-1, in.read());
    }

    @Test
    void skip_skipsAcrossPrefixAndUnderlying() throws IOException {
        MergedStream in = merged("abc".getBytes(StandardCharsets.US_ASCII), "XYZ");

        long skipped = in.skip(4); // skip 'a','b','c','X'
        assertEquals(4, skipped);

        assertEquals('Y', in.read());
        assertEquals('Z', in.read());
        assertEquals(-1, in.read());
    }

    @Test
    void available_countsPrefixPlusUnderlying() throws IOException {
        byte[] prefix = "abc".getBytes(StandardCharsets.US_ASCII);   // 3
        String underlying = "12345";                                 // 5
        MergedStream in = merged(prefix, underlying);

        // Expect prefix(3) + underlying.available()(5) = 8
        assertEquals(8, in.available());

        // Consume 2 bytes from prefix
        byte[] tmp = new byte[2];
        assertEquals(2, in.read(tmp, 0, 2));
        assertEquals(6, in.available()); // remaining 1 from prefix + 5 underlying
    }

    @Test
    void zeroLengthRead_returnsZero_andDoesNotAdvance() throws IOException {
        MergedStream in = merged("A".getBytes(StandardCharsets.US_ASCII), "B");

        byte[] buf = new byte[8];
        assertEquals(0, in.read(buf, 0, 0)); // zero-length read

        // Next byte should still be the first prefix byte
        assertEquals('A', in.read());
        assertEquals('B', in.read());
        assertEquals(-1, in.read());
    }

    @Test
    void withoutPrefix_behavesLikeUnderlying() throws IOException {
        MergedStream in = merged(null, "data");

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buf = new byte[16];
        int n;
        while ((n = in.read(buf)) != -1) {
            out.write(buf, 0, n);
        }

        assertEquals("data", out.toString(StandardCharsets.US_ASCII));
    }

    @Test
    void close_closesUnderlyingStream() throws IOException {
        class CloseTrackingInputStream extends FilterInputStream {
            boolean closed;
            CloseTrackingInputStream(InputStream delegate) { super(delegate); }
            @Override public void close() throws IOException { closed = true; super.close(); }
        }

        CloseTrackingInputStream base = new CloseTrackingInputStream(
                new ByteArrayInputStream("x".getBytes(StandardCharsets.US_ASCII))
        );
        MergedStream in = new MergedStream(null, base, null, 0, 0);

        in.close();
        assertTrue(base.closed, "Underlying stream should be closed by MergedStream#close()");
    }

    @Test
    void markSupported_and_reset_delegateWhenNoPrefix() throws IOException {
        // ByteArrayInputStream supports mark/reset; no prefix so MergedStream should delegate
        InputStream base = new ByteArrayInputStream("abcdef".getBytes(StandardCharsets.US_ASCII));
        MergedStream in = new MergedStream(null, base, null, 0, 0);

        assertTrue(in.markSupported());

        in.mark(100);
        byte[] first = new byte[3];
        assertEquals(3, in.read(first));
        assertArrayEquals("abc".getBytes(StandardCharsets.US_ASCII), first);

        in.reset();
        byte[] again = new byte[3];
        assertEquals(3, in.read(again));
        assertArrayEquals("abc".getBytes(StandardCharsets.US_ASCII), again);
    }

    @Test
    void respectsStartAndEndWindowOnPrefix() throws IOException {
        byte[] raw = new byte[] {10, 20, 30, 40}; // We'll expose only [20, 30]
        MergedStream in = new MergedStream(null, new ByteArrayInputStream(new byte[0]), raw, 1, 3);

        assertEquals(20, in.read());
        assertEquals(30, in.read());
        assertEquals(-1, in.read()); // underlying is empty
    }
}