package org.apache.commons.compress.utils;

import org.junit.Test;
import java.io.IOException;
import java.nio.channels.SeekableByteChannel;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertSame;

/**
 * This test class contains tests for MultiReadOnlySeekableByteChannel.
 * Note: The original class name and scaffolding are preserved from the source,
 * but in a real-world scenario, they would be renamed for clarity (e.g., MultiReadOnlySeekableByteChannelTest).
 */
public class MultiReadOnlySeekableByteChannel_ESTestTest21 extends MultiReadOnlySeekableByteChannel_ESTest_scaffolding {

    /**
     * Verifies that the position(long) method returns the same channel instance,
     * adhering to the SeekableByteChannel interface contract which allows for method chaining.
     */
    @Test
    public void positionShouldReturnTheSameChannelInstance() throws IOException {
        // Arrange: Create a MultiReadOnlySeekableByteChannel from an empty list of channels.
        // This is the simplest valid state for the object under test.
        List<SeekableByteChannel> emptyChannelList = Collections.emptyList();
        MultiReadOnlySeekableByteChannel multiChannel = new MultiReadOnlySeekableByteChannel(emptyChannelList);

        // Act: Call the position() method.
        SeekableByteChannel resultChannel = multiChannel.position(0L);

        // Assert: The returned channel should be the exact same instance as the one we called the method on.
        assertSame("The position() method must return 'this' to allow for method chaining.",
                multiChannel, resultChannel);
    }
}