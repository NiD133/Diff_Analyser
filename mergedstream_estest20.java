package com.fasterxml.jackson.core.io;

import com.fasterxml.jackson.core.ErrorReportConfiguration;
import com.fasterxml.jackson.core.StreamReadConstraints;
import com.fasterxml.jackson.core.StreamWriteConstraints;
import com.fasterxml.jackson.core.util.BufferRecycler;
import org.junit.Test;

import java.io.InputStream;
import java.io.PipedInputStream;

import static org.junit.Assert.assertThrows;

/**
 * Contains improved tests for the {@link MergedStream} class.
 */
public class MergedStreamTest {

    /**
     * Tests that calling {@link MergedStream#read(byte[])} throws a NullPointerException
     * when the initial buffer provided to the constructor is exhausted and the underlying
     * {@link IOContext} attempts to release a buffer it does not manage.
     * <p>
     * The {@link MergedStream} is designed to release its initial buffer back to the
     * {@link IOContext} once it has been fully read. This test simulates a scenario where
     * the buffer was not allocated by the {@link IOContext}'s BufferRecycler. When the
     * read operation exhausts the buffer, it triggers an internal free method,
     * which in turn calls {@code ioContext.releaseReadIOBuffer()}. This call fails with an NPE
     * because the context's internal logic expects a buffer it manages, leading to the
     * tested behavior.
     */
    @Test
    public void readThrowsNPEWhenReleasingBufferNotAllocatedByContext() {
        // Arrange
        // 1. Set up a minimal IOContext. The BufferRecycler is the key component.
        BufferRecycler bufferRecycler = new BufferRecycler();
        ContentReference contentReference = ContentReference.rawReference(false, new Object());
        IOContext ioContext = new IOContext(
                StreamReadConstraints.defaults(),
                StreamWriteConstraints.defaults(),
                ErrorReportConfiguration.defaults(),
                bufferRecycler,
                contentReference,
                false);

        // 2. The underlying input stream is not used in this scenario but is required by the constructor.
        InputStream underlyingInputStream = new PipedInputStream();

        // 3. Create a buffer that is NOT managed by the IOContext.
        byte[] externalBuffer = new byte[2];
        int bufferStart = 0;
        int bufferEnd = 2;

        MergedStream mergedStream = new MergedStream(ioContext, underlyingInputStream, externalBuffer, bufferStart, bufferEnd);

        // Act & Assert
        // Attempting to read from the stream will consume the `externalBuffer`.
        // Once the buffer is exhausted within the same read call, MergedStream tries to
        // release it back to the IOContext, which is expected to cause an NPE.
        byte[] readBuffer = new byte[2];
        assertThrows(NullPointerException.class, () -> {
            mergedStream.read(readBuffer);
        });
    }
}