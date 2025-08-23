package org.apache.commons.io.input;

import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Contains tests for the {@link ObservableInputStream} class.
 */
public class ObservableInputStreamTest {

    /**
     * Tests that calling {@code noteDataBytes(byte[], int, int)} with a null buffer
     * correctly throws a {@code NullPointerException}. The method is expected to
     * perform a null check on its arguments before proceeding.
     */
    @Test(expected = NullPointerException.class)
    public void noteDataBytesWithNullBufferShouldThrowException() {
        // Arrange: Create an ObservableInputStream.
        // The underlying stream's content is irrelevant for this test, so an empty
        // one is used for simplicity. No observers are needed either.
        final InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        final ObservableInputStream observableInputStream = new ObservableInputStream(inputStream);

        // Act: Call the method under test with a null buffer.
        // The offset and length values are arbitrary, as the null check on the buffer
        // should occur first. This call is expected to throw the exception.
        observableInputStream.noteDataBytes(null, 3677, -1);

        // Assert: The test passes if a NullPointerException is thrown,
        // which is handled by the @Test(expected = ...) annotation.
    }
}