package com.fasterxml.jackson.core.io;

import com.fasterxml.jackson.core.ErrorReportConfiguration;
import com.fasterxml.jackson.core.StreamReadConstraints;
import com.fasterxml.jackson.core.StreamWriteConstraints;
import com.fasterxml.jackson.core.util.BufferRecycler;
import org.junit.Test;

import java.io.IOException;
import java.io.PipedOutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

// Note: The original test class name 'UTF8Writer_ESTestTest37' and scaffolding are kept for context.
// In a real-world scenario, this test would be part of a consolidated 'UTF8WriterTest' class.
public class UTF8Writer_ESTestTest37 extends UTF8Writer_ESTest_scaffolding {

    /**
     * Verifies that calling write() with a Unicode code point larger than the
     * maximum allowed value (U+10FFFF) results in an IOException.
     */
    @Test
    public void write_withInvalidCodePoint_shouldThrowIOException() {
        // Arrange
        // Set up the necessary context and a dummy output stream for the writer.
        IOContext ioContext = new IOContext(
                StreamReadConstraints.defaults(),
                StreamWriteConstraints.defaults(),
                ErrorReportConfiguration.defaults(),
                new BufferRecycler(),
                ContentReference.UNKNOWN_CONTENT,
                false
        );
        UTF8Writer utf8Writer = new UTF8Writer(ioContext, new PipedOutputStream());

        // The largest valid Unicode code point is 0x10FFFF.
        // We use a value just beyond this limit to test the boundary condition.
        int invalidCodePoint = 0x10FFFF + 1; // This is 0x110000

        // Act & Assert
        try {
            utf8Writer.write(invalidCodePoint);
            fail("Expected an IOException because the code point exceeds the valid Unicode range.");
        } catch (IOException e) {
            String expectedMessage = String.format(
                "Illegal character point (0x%X) to output; max is 0x10FFFF as per RFC 4627",
                invalidCodePoint
            );
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}