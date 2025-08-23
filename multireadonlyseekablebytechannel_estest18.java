package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.nio.channels.SeekableByteChannel;
import java.util.List;
import org.junit.Test;

/**
 * Tests for {@link MultiReadOnlySeekableByteChannel}.
 */
public class MultiReadOnlySeekableByteChannelTest {

    /**
     * Verifies that the constructor throws a NullPointerException when the provided
     * list of channels is null, as this is a contract violation.
     */
    @Test
    public void constructorShouldThrowNullPointerExceptionForNullChannelList() {
        // The constructor is expected to reject a null list of channels.
        // We use assertThrows to verify that the correct exception is thrown
        // and to inspect its properties, such as the message.
        final NullPointerException exception = assertThrows(
            NullPointerException.class,
            () -> new MultiReadOnlySeekableByteChannel((List<SeekableByteChannel>) null)
        );

        // The source class uses Objects.requireNonNull(channels, "channels"),
        // so we can assert that the exception message is "channels".
        // This makes the test more precise.
        assertEquals("channels", exception.getMessage());
    }
}