package com.fasterxml.jackson.core.io;

import java.io.*;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.core.JsonEncoding;

import static org.junit.jupiter.api.Assertions.*;

class MergedStreamTest extends com.fasterxml.jackson.core.JUnit5TestBase {
    private static final String FIRST_DATA = "ABCDE";
    private static final String SECOND_DATA = "FGHIJ";
    private static final int BUFFER_OFFSET = 99;
    private static final int TEMP_BUFFER_SIZE = 5;

    @Test
    void testMergedStreamOperations() throws Exception {
        // Setup IOContext and buffers
        IOContext ctxt = testIOContext();
        byte[] recyclableBuffer = ctxt.allocReadIOBuffer();
        byte[] firstDataBytes = FIRST_DATA.getBytes("UTF-8");
        byte[] secondDataBytes = SECOND_DATA.getBytes("UTF-8");
        
        // Copy first data segment into recyclable buffer at specified offset
        System.arraycopy(firstDataBytes, 0, recyclableBuffer, BUFFER_OFFSET, firstDataBytes.length);

        // Verify initial context state
        assertNull(ctxt.contentReference().getRawContent());
        assertFalse(ctxt.isResourceManaged());
        ctxt.setEncoding(JsonEncoding.UTF8);

        // Create merged stream that combines:
        // 1. Buffered data (ABCDE at offset 99)
        // 2. Underlying stream data (FGHIJ)
        MergedStream mergedStream = new MergedStream(
            ctxt,
            new ByteArrayInputStream(secondDataBytes),
            recyclableBuffer,
            BUFFER_OFFSET,
            BUFFER_OFFSET + firstDataBytes.length
        );
        
        // Close context immediately to verify stream independence
        ctxt.close();

        try (mergedStream) { // Ensure stream closure even if tests fail
            // --- Test 1: Initial available bytes (buffered data) ---
            assertEquals(firstDataBytes.length, mergedStream.available());

            // --- Test 2: Mark/reset unsupported with buffered data ---
            assertFalse(mergedStream.markSupported());
            mergedStream.mark(1); // Should be no-op without exception

            // --- Test 3: Read individual bytes ---
            assertEquals((byte) 'A', mergedStream.read()); // First buffered byte

            // --- Test 4: Skip bytes ---
            assertEquals(3, mergedStream.skip(3)); // Skip B, C, D

            // --- Test 5: Partial buffer read ---
            byte[] readBuffer = new byte[TEMP_BUFFER_SIZE];
            // Request 3 bytes but only 1 available in buffer (E)
            assertEquals(1, mergedStream.read(readBuffer, 1, 3));
            assertEquals((byte) 'E', readBuffer[1]); // Verify correct byte

            // --- Test 6: Read from underlying stream ---
            assertEquals(3, mergedStream.read(readBuffer, 0, 3)); // Read F, G, H
            assertEquals((byte) 'F', readBuffer[0]);
            assertEquals((byte) 'G', readBuffer[1]);
            assertEquals((byte) 'H', readBuffer[2]);

            // --- Test 7: Available bytes after partial read ---
            assertEquals(2, mergedStream.available()); // I, J remaining

            // --- Test 8: Skip beyond available data ---
            assertEquals(2, mergedStream.skip(200)); // Skip remaining I, J
        }
    }
}