package com.fasterxml.jackson.core.io;

import com.fasterxml.jackson.core.util.BufferRecycler;
import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

import static org.junit.Assert.fail;

/**
 * Contains tests for the {@link UTF8Writer} class.
 */
// The original class name 'UTF8Writer_ESTestTest26' is kept for context,
// but in a real project, it would be merged into a single 'UTF8WriterTest' class.
public class UTF8Writer_ESTestTest26 {

    /**
     * Tests that writing to a UTF8Writer configured with a null OutputStream
     * throws a NullPointerException when the internal buffer is full and needs to be flushed.
     */
    @Test
    public void write_whenBufferOverflowsWithNullOutputStream_shouldThrowNullPointerException() {
        // Arrange
        // The UTF8Writer needs an IOContext, primarily to allocate its internal buffer.
        // We use a standard BufferRecycler for this. Other IOContext parameters are not
        // relevant to this test and can be created from defaults.
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(null, null, null, bufferRecycler, null, false);

        // The key part of the test setup: instantiate UTF8Writer with a null OutputStream.
        UTF8Writer writer = new UTF8Writer(ioContext, (OutputStream) null);

        // The default buffer size for write encoding is 2000 bytes.
        // We create a character array larger than this to guarantee a buffer flush attempt.
        // Each 'a' character is a single byte in UTF-8, so 2001 chars will overflow a 2000-byte buffer.
        char[] largeCharArray = new char[2001];
        Arrays.fill(largeCharArray, 'a');

        // Act & Assert
        try {
            // This write operation will fill the internal buffer and attempt to flush it.
            // Since the underlying OutputStream is null, a NullPointerException is expected.
            writer.write(largeCharArray);
            fail("Expected a NullPointerException because the underlying OutputStream is null and the buffer needs to be flushed.");
        } catch (NullPointerException e) {
            // This is the expected outcome. The test passes.
        } catch (IOException e) {
            // Fail the test if any other IO-related exception occurs.
            fail("An unexpected IOException was thrown: " + e.getMessage());
        }
    }
}