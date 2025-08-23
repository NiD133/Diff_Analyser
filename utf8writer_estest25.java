package com.fasterxml.jackson.core.io;

import com.fasterxml.jackson.core.ErrorReportConfiguration;
import com.fasterxml.jackson.core.StreamReadConstraints;
import com.fasterxml.jackson.core.StreamWriteConstraints;
import com.fasterxml.jackson.core.util.BufferRecycler;
import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;

/**
 * This test class focuses on the behavior of the UTF8Writer.
 * Note: The original class name "UTF8Writer_ESTestTest25" was likely auto-generated.
 * A more conventional name would be "UTF8WriterTest".
 */
public class UTF8Writer_ESTestTest25 {

    /**
     * Verifies that calling write(String, int, int) with a null string
     * argument throws a NullPointerException. This aligns with the general
     * contract of the {@link java.io.Writer} class.
     */
    @Test(expected = NullPointerException.class, timeout = 4000)
    public void write_withNullString_shouldThrowNullPointerException() throws IOException {
        // Arrange: Set up the necessary context and the UTF8Writer instance.
        // The OutputStream can be null for this test because the exception is
        // triggered by the invalid input argument before the stream is used.
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(
                StreamReadConstraints.defaults(),
                StreamWriteConstraints.defaults(),
                ErrorReportConfiguration.defaults(),
                bufferRecycler,
                ContentReference.unknown(),
                false);
        UTF8Writer utf8Writer = new UTF8Writer(ioContext, (OutputStream) null);

        // Act: Attempt to write from a null string.
        // The @Test(expected=...) annotation asserts that a NullPointerException is thrown.
        utf8Writer.write((String) null, 0, 1);
    }
}