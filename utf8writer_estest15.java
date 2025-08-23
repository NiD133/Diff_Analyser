package com.fasterxml.jackson.core.io;

import com.fasterxml.jackson.core.ErrorReportConfiguration;
import com.fasterxml.jackson.core.StreamReadConstraints;
import com.fasterxml.jackson.core.StreamWriteConstraints;
import com.fasterxml.jackson.core.util.BufferRecycler;
import org.junit.Test;

import java.io.IOException;
import java.io.PipedOutputStream;

/**
 * Unit tests for the {@link UTF8Writer} class.
 */
public class UTF8WriterTest {

    /**
     * Verifies that calling {@link UTF8Writer#write(String)} with a null argument
     * correctly throws a NullPointerException.
     */
    @Test(expected = NullPointerException.class)
    public void writeString_whenGivenNullInput_shouldThrowNullPointerException() throws IOException {
        // Arrange: Set up the necessary context and a dummy output stream for the UTF8Writer.
        // The IOContext requires several configuration objects, which are created with default values.
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(
                StreamReadConstraints.defaults(),
                StreamWriteConstraints.defaults(),
                ErrorReportConfiguration.defaults(),
                bufferRecycler,
                null, // The content reference is not relevant for this test.
                true);

        // A PipedOutputStream serves as a simple, in-memory sink for this test.
        PipedOutputStream outputStream = new PipedOutputStream();
        UTF8Writer utf8Writer = new UTF8Writer(ioContext, outputStream);

        // Act & Assert: Call the method under test with null input.
        // The @Test(expected) annotation asserts that a NullPointerException is thrown.
        utf8Writer.write((String) null);
    }
}