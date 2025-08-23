package com.fasterxml.jackson.core.io;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonEncoding;

import static org.junit.jupiter.api.Assertions.*;

class MergedStreamTest extends com.fasterxml.jackson.core.JUnit5TestBase {

    private static final String FIRST_CHUNK = "ABCDE";
    private static final String SECOND_CHUNK = "FGHIJ";
    private static final int FIRST_CHUNK_OFFSET = 99;

    @Test
    void readsPrefetchedBytesThenDelegatesAndSupportsSkip() throws Exception {
        IOContext ctx = testIOContext();

        // Sanity-check initial IOContext state (not strictly required by MergedStream)
        assertNull(ctx.contentReference().getRawContent());
        assertFalse(ctx.isResourceManaged());

        ctx.setEncoding(JsonEncoding.UTF8);

        try (MergedStream ms = newMergedStream(ctx, FIRST_CHUNK, SECOND_CHUNK, FIRST_CHUNK_OFFSET)) {
            // 1) All bytes from the prefetched buffer should be available initially
            assertEquals(FIRST_CHUNK.length(), ms.available(), "available() should equal prefetched size");

            // 2) mark/reset is not supported while buffered bytes exist
            assertFalse(ms.markSupported(), "mark/reset should not be supported");
            ms.mark(1); // should be a no-op and not throw

            // 3) Read from the prefetched buffer
            assertEquals((byte) 'A', ms.read(), "first byte must come from the prefetched buffer");

            // Skip the next three buffered bytes: B, C, D
            assertEquals(3, ms.skip(3), "should skip the next three buffered bytes");

            // Read the last buffered byte ('E') using read(byte[], off, len)
            byte[] buf = new byte[5];
            int read = ms.read(buf, 1, 1);
            assertEquals(1, read, "should read the last buffered byte only");
            assertEquals((byte) 'E', buf[1]);

            // 4) After buffered bytes are consumed, read from the underlying stream
            int n = ms.read(buf, 0, 3);
            assertEquals(3, n, "should read three bytes from the underlying stream");
            assertEquals((byte) 'F', buf[0]);
            assertEquals((byte) 'G', buf[1]);
            assertEquals((byte) 'H', buf[2]);

            // Two bytes ('I', 'J') should remain
            assertEquals(2, ms.available(), "two bytes should remain available in the underlying stream");

            // Skipping beyond the remaining length should only skip what's left
            assertEquals(2, ms.skip(200), "should skip only the remaining two bytes");

            // Closing the stream should be safe and close the underlying input
            // (no assertion; absence of exception is sufficient)
        }
    }

    private MergedStream newMergedStream(IOContext ctx, String first, String second, int offset) throws IOException {
        byte[] reusableBuf = ctx.allocReadIOBuffer();

        byte[] firstBytes = first.getBytes(StandardCharsets.UTF_8);
        System.arraycopy(firstBytes, 0, reusableBuf, offset, firstBytes.length);

        byte[] secondBytes = second.getBytes(StandardCharsets.UTF_8);
        MergedStream ms = new MergedStream(
                ctx,
                new ByteArrayInputStream(secondBytes),
                reusableBuf,
                offset,
                offset + firstBytes.length
        );

        // After creation, MergedStream owns the buffer/stream; IOContext can be closed.
        ctx.close();
        return ms;
    }
}