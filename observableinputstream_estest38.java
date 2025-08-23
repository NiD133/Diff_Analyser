package org.apache.commons.io.input;

import org.junit.Test;

import java.io.IOException;
import java.nio.CharBuffer;

/**
 * Contains tests for {@link ObservableInputStream}, focusing on its consumption behavior.
 */
public class ObservableInputStream_ESTestTest38 extends ObservableInputStream_ESTest_scaffolding {

    /**
     * Tests that the consume() method successfully reads all data from an
     * ObservableInputStream that is backed by a CharBuffer, without throwing an exception.
     */
    @Test(timeout = 4000)
    public void consumeWithCharBufferSourceShouldNotThrowException() throws IOException {
        // Arrange: Create an ObservableInputStream from a CharBuffer source.
        // The buffer is filled with 255 null characters, which is a valid source.
        final CharBuffer sourceBuffer = CharBuffer.allocate(255);
        final ObservableInputStream inputStream = new ObservableInputStream.Builder()
                .setCharSequence(sourceBuffer)
                .get();

        // Act & Assert: The consume() method should read the entire stream
        // without error. The test passes if this call completes successfully.
        inputStream.consume();
    }
}