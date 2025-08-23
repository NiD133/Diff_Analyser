package com.fasterxml.jackson.core.io;

import com.fasterxml.jackson.core.ErrorReportConfiguration;
import com.fasterxml.jackson.core.StreamReadConstraints;
import com.fasterxml.jackson.core.StreamWriteConstraints;
import com.fasterxml.jackson.core.util.BufferRecycler;
import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

// The original class name 'UTF8Writer_ESTestTest30' is non-descriptive.
// A better name would be 'UTF8WriterTest'.
public class UTF8Writer_ESTestTest30 extends UTF8Writer_ESTest_scaffolding {

    /**
     * Tests that the convertSurrogate() method correctly throws an IOException
     * when provided with an invalid second part of a surrogate pair.
     *
     * In this scenario, the first part of the surrogate is implicitly 0 (the default),
     * and the second part is a value far outside the valid range.
     */
    @Test
    public void convertSurrogate_withInvalidSecondPart_throwsIOException() throws IOException {
        // Arrange
        // The IOContext is required by the UTF8Writer constructor.
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(
                StreamReadConstraints.defaults(),
                StreamWriteConstraints.defaults(),
                ErrorReportConfiguration.defaults(),
                bufferRecycler,
                ContentReference.unknown(),
                false // isResourceManaged
        );

        // The OutputStream is not used by the convertSurrogate method, so it can be null for this test.
        UTF8Writer writer = new UTF8Writer(ioContext, (OutputStream) null);

        // An integer value that is well outside the valid range for the second surrogate character (0xDC00-0xDFFF).
        int invalidSecondSurrogate = 20000000; // 0x1312d00 in hex

        // Act & Assert
        try {
            writer.convertSurrogate(invalidSecondSurrogate);
            fail("Expected an IOException to be thrown for an invalid surrogate pair");
        } catch (IOException e) {
            // The first surrogate part is 0 because it was never set.
            String expectedMessage = "Broken surrogate pair: first char 0x0, second 0x1312d00; illegal combination";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}