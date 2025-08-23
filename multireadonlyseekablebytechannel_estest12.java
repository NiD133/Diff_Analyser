package org.apache.commons.compress.utils;

import org.junit.Test;

import java.nio.channels.SeekableByteChannel;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;


/**
 * Tests for {@link MultiReadOnlySeekableByteChannel}.
 */
// The original class name "MultiReadOnlySeekableByteChannel_ESTestTest12"
// was likely auto-generated. A more conventional name is used here.
public class MultiReadOnlySeekableByteChannelTest {

    /**
     * Verifies that the isOpen() method propagates a NullPointerException if the
     * underlying list of channels contains a null element. The implementation of
     * isOpen() iterates through all channels, so it should fail when it
     * encounters a null reference.
     */
    @Test
    public void isOpenShouldThrowNullPointerExceptionWhenChannelListContainsNull() {
        // Arrange: Create a MultiReadOnlySeekableByteChannel backed by a list
        // that contains a single null channel.
        final List<SeekableByteChannel> channelsWithNull = Collections.singletonList(null);
        final MultiReadOnlySeekableByteChannel multiChannel =
            new MultiReadOnlySeekableByteChannel(channelsWithNull);

        // Act & Assert: Expect a NullPointerException when isOpen() is called.
        // The use of a try-catch block explicitly pinpoints the method call
        // that is expected to fail.
        try {
            multiChannel.isOpen();
            fail("Expected a NullPointerException to be thrown, but no exception occurred.");
        } catch (final NullPointerException e) {
            // The expected exception was caught, so the test passes.
            // No further checks on the exception are necessary.
        }
    }
}