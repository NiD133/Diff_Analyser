package org.apache.commons.lang3;

import org.junit.Test;

import java.io.IOException;
import java.nio.BufferOverflowException;
import java.nio.CharBuffer;

/**
 * This test case focuses on the behavior of {@link AppendableJoiner} when used with
 * an {@link Appendable} that has limited capacity.
 */
public class AppendableJoinerTest {

    /**
     * Tests that calling joinA with a buffer that has zero capacity
     * correctly throws a BufferOverflowException.
     */
    @Test(expected = BufferOverflowException.class)
    public void testJoinToZeroCapacityBufferThrowsException() throws IOException {
        // Arrange: Create a joiner and an Appendable with no available space.
        final AppendableJoiner<String> joiner = AppendableJoiner.<String>builder().get();
        final CharBuffer zeroCapacityBuffer = CharBuffer.allocate(0);
        final String[] elementsToJoin = {"any-element"};

        // Act: Attempt to join elements into the buffer that cannot hold any data.
        // The joiner will try to append the first element, causing an overflow immediately.
        joiner.joinA(zeroCapacityBuffer, elementsToJoin);

        // Assert: The test expects a BufferOverflowException, which is verified by the
        // @Test(expected=...) annotation. No further assertions are needed.
    }
}