package org.apache.commons.io.function;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link Uncheck} utility class, focusing on successful execution paths.
 */
class UncheckTest {

    private static final byte[] TEST_DATA = {'a', 'b'};

    private ByteArrayInputStream newInputStream() {
        return new ByteArrayInputStream(TEST_DATA);
    }

    /**
     * Tests that {@link Uncheck#accept(IOConsumer, Object)} successfully executes a
     * given IO action that does not throw an exception.
     */
    @Test
    void accept_whenConsumerSucceeds_executesAction() {
        // Arrange: Create an input stream with the content "ab".
        final ByteArrayInputStream inputStream = newInputStream();
        final long bytesToSkip = 1L;

        // Act: Use Uncheck.accept to invoke the skip method on the stream.
        // The 'skip' method can throw an IOException, but is not expected to here.
        Uncheck.accept(inputStream::skip, bytesToSkip);

        // Assert: Verify the action was executed by checking the stream's state.
        // The next byte read should be 'b', confirming one byte was skipped.
        // We use Uncheck.get for the read, as read() also throws a checked IOException.
        final int nextByte = Uncheck.get(inputStream::read);
        assertEquals('b', nextByte);
    }
}