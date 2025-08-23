package com.fasterxml.jackson.core.io;

import com.fasterxml.jackson.core.ErrorReportConfiguration;
import com.fasterxml.jackson.core.StreamReadConstraints;
import com.fasterxml.jackson.core.StreamWriteConstraints;
import com.fasterxml.jackson.core.util.BufferRecycler;
import org.junit.Test;

import java.io.OutputStream;

// Note: The original test class name "UTF8Writer_ESTestTest43" and its scaffolding parent
// are preserved as per the instructions. In a real-world scenario, these would be renamed
// for clarity (e.g., to "UTF8WriterTest").
public class UTF8Writer_ESTestTest43 extends UTF8Writer_ESTest_scaffolding {

    /**
     * Verifies that attempting to write to a UTF8Writer that was initialized
     * with a null OutputStream throws a NullPointerException.
     *
     * This is the expected behavior, as the writer has no underlying stream
     * to write data to.
     */
    @Test(expected = NullPointerException.class)
    public void write_whenOutputStreamIsNull_shouldThrowNullPointerException() {
        // Arrange: Set up the necessary context and a UTF8Writer with a null OutputStream.
        BufferRecycler bufferRecycler = new BufferRecycler();
        
        // IOContext requires several configuration objects for instantiation.
        // We use defaults as their specific values are not relevant to this test.
        IOContext ioContext = new IOContext(
                StreamReadConstraints.defaults(),
                StreamWriteConstraints.defaults(),
                ErrorReportConfiguration.defaults(),
                bufferRecycler,
                ContentReference.REDACTED_CONTENT,
                false);

        // The condition under test: a writer with no underlying output stream.
        UTF8Writer utf8Writer = new UTF8Writer(ioContext, (OutputStream) null);
        char[] someData = {'t', 'e', 's', 't'};

        // Act: Attempt to write data. This action is expected to throw the exception.
        // The specific data and offsets are not critical; any write operation would fail.
        utf8Writer.write(someData, 0, someData.length);

        // Assert: The test passes if a NullPointerException is thrown, which is
        // declaratively handled by the @Test(expected=...) annotation.
    }
}