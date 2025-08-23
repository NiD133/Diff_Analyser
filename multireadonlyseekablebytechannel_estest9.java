package org.apache.commons.compress.utils;

import org.junit.Test;
import java.io.IOException;
import java.nio.channels.SeekableByteChannel;
import java.util.Collections;
import java.util.List;

/**
 * Tests for {@link MultiReadOnlySeekableByteChannel}.
 * This test focuses on the behavior of the position(long, long) method.
 */
// The class name and inheritance are kept from the original to allow a direct replacement.
public class MultiReadOnlySeekableByteChannel_ESTestTest9 extends MultiReadOnlySeekableByteChannel_ESTest_scaffolding {

    /**
     * Verifies that calling position(channel, offset) throws a NullPointerException
     * if the channel was constructed with a list containing a null element.
     */
    @Test(timeout = 4000, expected = NullPointerException.class)
    public void positionWithChannelAndOffsetThrowsNPEWhenChannelListContainsNull() throws IOException {
        // Arrange: Create a MultiReadOnlySeekableByteChannel with a list
        // containing a null element. This represents an invalid state.
        List<SeekableByteChannel> channelsWithNull = Collections.singletonList(null);
        MultiReadOnlySeekableByteChannel multiChannel = new MultiReadOnlySeekableByteChannel(channelsWithNull);

        // Act & Assert: Calling position() should fail fast with a NullPointerException
        // when it encounters the null channel. The specific position values are
        // arbitrary for this test case.
        multiChannel.position(-1L, -1L);
    }
}