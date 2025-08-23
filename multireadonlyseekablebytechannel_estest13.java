package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.nio.channels.SeekableByteChannel;
import org.junit.Test;

/**
 * Tests for {@link MultiReadOnlySeekableByteChannel}.
 */
public class MultiReadOnlySeekableByteChannelTest {

    /**
     * Verifies that the forSeekableByteChannels() factory method throws a NullPointerException
     * when the input array of channels is null, as specified by its contract.
     */
    @Test
    public void forSeekableByteChannelsShouldThrowNullPointerExceptionForNullInput() {
        try {
            // The cast to SeekableByteChannel[] is necessary to resolve ambiguity
            // for the varargs method call with a null argument.
            MultiReadOnlySeekableByteChannel.forSeekableByteChannels((SeekableByteChannel[]) null);
            fail("Expected a NullPointerException to be thrown for a null channels array.");
        } catch (final NullPointerException e) {
            // The underlying implementation uses Objects.requireNonNull(channels, "channels"),
            // so we can assert the specific message for a more robust test.
            assertEquals("channels", e.getMessage());
        }
    }
}