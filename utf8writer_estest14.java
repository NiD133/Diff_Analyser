package com.fasterxml.jackson.core.io;

import com.fasterxml.jackson.core.ErrorReportConfiguration;
import com.fasterxml.jackson.core.StreamReadConstraints;
import com.fasterxml.jackson.core.StreamWriteConstraints;
import com.fasterxml.jackson.core.util.BufferRecycler;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Unit tests for the {@link UTF8Writer} class, focusing on its handling of null inputs.
 */
public class UTF8WriterTest {

    /**
     * Verifies that calling the write(char[]) method with a null array
     * correctly throws a NullPointerException.
     */
    @Test(expected = NullPointerException.class)
    public void write_withNullCharArray_shouldThrowNullPointerException() throws IOException {
        // Arrange: Set up a UTF8Writer with standard components.
        // A ByteArrayOutputStream is a simple and sufficient sink for this test.
        IOContext ioContext = new IOContext(
                StreamReadConstraints.defaults(),
                StreamWriteConstraints.defaults(),
                ErrorReportConfiguration.defaults(),
                new BufferRecycler(),
                ContentReference.redacted(),
                true);
        UTF8Writer utf8Writer = new UTF8Writer(ioContext, new ByteArrayOutputStream());

        // Act: Attempt to write a null char array.
        // The @Test(expected) annotation will handle the assertion.
        utf8Writer.write((char[]) null);
    }
}