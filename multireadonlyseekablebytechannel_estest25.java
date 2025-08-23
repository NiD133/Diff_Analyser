package org.apache.commons.compress.utils;

import org.junit.Test;

import java.nio.channels.SeekableByteChannel;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link MultiReadOnlySeekableByteChannel}.
 */
public class MultiReadOnlySeekableByteChannelTest {

    @Test
    public void isOpen_shouldReturnTrue_whenChannelIsNewlyCreated() {
        // Arrange: Create a multi-channel instance.
        // The list of underlying channels can be empty, as the 'open' state
        // is independent of the number of concatenated channels.
        List<SeekableByteChannel> emptyChannelList = Collections.emptyList();
        MultiReadOnlySeekableByteChannel multiChannel = new MultiReadOnlySeekableByteChannel(emptyChannelList);

        // Act: Check the status of the newly created channel.
        boolean isOpen = multiChannel.isOpen();

        // Assert: The channel should report itself as open immediately after creation.
        assertTrue("A newly instantiated MultiReadOnlySeekableByteChannel should be open.", isOpen);
    }
}