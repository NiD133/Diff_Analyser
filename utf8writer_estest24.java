package com.fasterxml.jackson.core.io;

import com.fasterxml.jackson.core.ErrorReportConfiguration;
import com.fasterxml.jackson.core.StreamReadConstraints;
import com.fasterxml.jackson.core.StreamWriteConstraints;
import com.fasterxml.jackson.core.util.BufferRecycler;
import org.junit.Test;

import java.io.OutputStream;

/**
 * This test suite focuses on verifying the behavior of the {@link UTF8Writer} class.
 */
public class UTF8WriterTest {

    /**
     * Verifies that calling {@link UTF8Writer#write(String, int, int)} with a negative
     * length value correctly throws an {@link IndexOutOfBoundsException}.
     * <p>
     * This behavior is mandated by the general contract of {@link java.io.Writer}.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void writeString_withNegativeLength_shouldThrowException() throws Exception {
        // Arrange: Set up the necessary context and the UTF8Writer instance.
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(
                StreamReadConstraints.defaults(),
                StreamWriteConstraints.defaults(),
                ErrorReportConfiguration.defaults(),
                bufferRecycler,
                ContentReference.unknown(),
                false);

        // The OutputStream can be null because the method should throw an exception
        // before attempting any actual I/O operations.
        UTF8Writer writer = new UTF8Writer(ioContext, (OutputStream) null);

        final String stringToWrite = "";
        final int anyOffset = -19; // The offset value is not the primary focus here.
        final int negativeLength = -1981; // A negative length is an invalid argument.

        // Act: Attempt to write a portion of a string using a negative length.
        writer.write(stringToWrite, anyOffset, negativeLength);

        // Assert: The test succeeds if an IndexOutOfBoundsException is thrown,
        // as specified by the @Test(expected=...) annotation.
    }
}