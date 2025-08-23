package org.apache.commons.io.input;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.junit.Test;

/**
 * Tests for {@link ObservableInputStream}.
 */
public class ObservableInputStreamTest {

    @Test
    public void consumeShouldPropagateIOExceptionFromClosedUnderlyingStream() {
        // Arrange: Create an ObservableInputStream with an effectively closed underlying stream.
        // A BufferedInputStream initialized with a null source stream will throw an
        // IOException("Stream closed") on any read attempt.
        final InputStream closedUnderlyingStream = new BufferedInputStream(null);
        final ObservableInputStream observableInputStream = new ObservableInputStream(closedUnderlyingStream);

        // Act & Assert: Verify that calling consume() propagates the expected IOException
        // from the underlying stream.
        final IOException thrown = assertThrows(IOException.class, observableInputStream::consume);
        assertEquals("Stream closed", thrown.getMessage());
    }
}