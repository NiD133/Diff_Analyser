package org.apache.commons.compress.utils;

import org.junit.Test;

import java.io.IOException;
import java.nio.channels.SeekableByteChannel;
import java.util.Arrays;
import java.util.List;

/**
 * Tests for {@link MultiReadOnlySeekableByteChannel} focusing on handling of invalid inputs.
 */
public class MultiReadOnlySeekableByteChannelTest {

    /**
     * Verifies that calling position() on a MultiReadOnlySeekableByteChannel throws a
     * NullPointerException if it was constructed with a list containing a null channel.
     *
     * This test ensures the class is not robust against null elements in its input list,
     * which is an important behavior to document and verify. The position() method is
     * expected to fail fast when it encounters the null channel while calculating sizes.
     *
     * @throws IOException for compliance with the SeekableByteChannel interface, not expected here.
     */
    @Test(expected = NullPointerException.class)
    public void positionShouldThrowNullPointerExceptionWhenChannelListContainsNull() throws IOException {
        // Arrange: Create a list of channels that includes a null element, representing
        // an invalid or corrupt set of input channels.
        List<SeekableByteChannel> channelsWithNull = Arrays.asList((SeekableByteChannel) null);
        MultiReadOnlySeekableByteChannel multiChannel = new MultiReadOnlySeekableByteChannel(channelsWithNull);

        // Act: Attempt to set the position on the composite channel. This action should
        // trigger an interaction with the null channel, causing the exception.
        multiChannel.position(1L);

        // Assert: The test passes if a NullPointerException is thrown, as specified
        // by the @Test(expected=...) annotation. No further assertions are needed.
    }
}