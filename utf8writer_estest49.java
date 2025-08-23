package com.fasterxml.jackson.core.io;

import com.fasterxml.jackson.core.util.BufferRecycler;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Contains tests for the {@link UTF8Writer} class.
 */
public class UTF8WriterTest {

    /**
     * Verifies that calling close() multiple times on a UTF8Writer is an
     * idempotent operation and does not throw an exception. This behavior
     * aligns with the contract of {@link java.io.Writer#close()}.
     */
    @Test
    public void callingCloseMultipleTimesShouldNotThrowException() throws IOException {
        // Arrange: Create a UTF8Writer with a minimal IOContext and an in-memory stream.
        // A null ContentReference is acceptable as it's not used in the close() logic.
        IOContext ioContext = new IOContext(new BufferRecycler(), /* contentReference */ null, /* recycling */ false);
        OutputStream outputStream = new ByteArrayOutputStream();
        UTF8Writer writer = new UTF8Writer(ioContext, outputStream);

        // Act: Close the writer twice. The first call closes the stream,
        // and the second call should be ignored.
        writer.close();
        writer.close();

        // Assert: The test passes if no exception is thrown during the second call to close().
        // This implicitly verifies the idempotency of the close() method.
    }
}