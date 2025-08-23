package com.fasterxml.jackson.core.io;

import java.io.*;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonEncoding;

import static org.junit.jupiter.api.Assertions.*;

class MergedStreamTest extends com.fasterxml.jackson.core.JUnit5TestBase {

    @Test
    void testMergedStreamFunctionality() throws Exception {
        // Create an IOContext for managing I/O resources
        IOContext ioContext = testIOContext();

        // Allocate a buffer and fill it with the string "ABCDE" starting at index 99
        byte[] primaryBuffer = ioContext.allocReadIOBuffer();
        System.arraycopy("ABCDE".getBytes("UTF-8"), 0, primaryBuffer, 99, 5);

        // Create a secondary buffer with the string "FGHIJ"
        byte[] secondaryBuffer = "FGHIJ".getBytes("UTF-8");

        // Verify initial state of the IOContext
        assertNull(ioContext.contentReference().getRawContent());
        assertFalse(ioContext.isResourceManaged());

        // Set the encoding for the IOContext
        ioContext.setEncoding(JsonEncoding.UTF8);

        // Create a MergedStream with the primary and secondary buffers
        MergedStream mergedStream = new MergedStream(ioContext, new ByteArrayInputStream(secondaryBuffer),
                                                     primaryBuffer, 99, 99 + 5);

        // Close the IOContext as it's no longer needed
        ioContext.close();

        // Test the MergedStream's functionality

        // Verify that 5 bytes are available from the primary buffer
        assertEquals(5, mergedStream.available());

        // Check if mark is supported (it shouldn't be)
        assertFalse(mergedStream.markSupported());

        // Attempt to mark the stream (should not throw an exception)
        mergedStream.mark(1);

        // Read the first byte and verify it is 'A'
        assertEquals((byte) 'A', mergedStream.read());

        // Skip the next 3 bytes
        assertEquals(3, mergedStream.skip(3));

        // Prepare a buffer to read data into
        byte[] readBuffer = new byte[5];

        // Read 3 bytes into the buffer starting at index 1
        // The read method is allowed to return between 1 and 3 bytes, but we expect 1
        assertEquals(1, mergedStream.read(readBuffer, 1, 3));
        assertEquals((byte) 'E', readBuffer[1]);

        // Read the next 3 bytes into the buffer starting at index 0
        assertEquals(3, mergedStream.read(readBuffer, 0, 3));
        assertEquals((byte) 'F', readBuffer[0]);
        assertEquals((byte) 'G', readBuffer[1]);
        assertEquals((byte) 'H', readBuffer[2]);

        // Verify that 2 bytes are still available
        assertEquals(2, mergedStream.available());

        // Skip the remaining bytes
        assertEquals(2, mergedStream.skip(200));

        // Close the MergedStream
        mergedStream.close();
    }
}