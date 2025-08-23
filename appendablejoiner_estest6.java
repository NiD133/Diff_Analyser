package org.apache.commons.lang3;

import org.junit.Test;

import java.nio.CharBuffer;

import static org.junit.Assert.fail;

/**
 * Contains tests for the {@link AppendableJoiner} class, focusing on edge cases.
 * This class corresponds to the original AppendableJoiner_ESTestTest6.
 */
public class AppendableJoiner_ESTestTest6 {

    /**
     * Tests that {@link AppendableJoiner#joinSB} throws an IndexOutOfBoundsException
     * when a prefix CharSequence is a view on another CharBuffer that has been consumed.
     *
     * <p>This scenario highlights a subtle behavior of Java's NIO Buffers. When a
     * {@code CharBuffer} is used as a {@code CharSequence}, its methods (like {@code charAt})
     * are relative to the buffer's current position. If the buffer is "consumed" by advancing
     * its position, any subsequent attempt to read from the {@code CharSequence} view can
     * result in an out-of-bounds access.</p>
     */
    @Test
    public void testJoinSBWithConsumedCharBufferViewAsPrefixThrowsException() {
        // Arrange: Create a source CharBuffer and a CharSequence view of it.
        StringBuilder target = new StringBuilder();
        CharBuffer sourceBuffer = CharBuffer.wrap("ab");

        // This CharSequence is a live view of the sourceBuffer. Its behavior, such as
        // the characters it presents, depends on the sourceBuffer's current state
        // (specifically, its position and limit).
        CharSequence bufferView = sourceBuffer;

        // Act 1: Consume the source buffer by advancing its position to the limit.
        // This simulates a read operation that has exhausted the buffer. After this,
        // the buffer's position is 2, and its limit is 2.
        sourceBuffer.position(sourceBuffer.limit());

        // Act 2 & Assert: Attempt to join using the consumed view as a prefix.
        // This should fail because the view will try to read from an invalid position.
        try {
            // The join operation first appends the prefix. StringBuilder.append(CharSequence)
            // will call bufferView.charAt(0). This translates to:
            // sourceBuffer.get(sourceBuffer.position() + 0)
            // which is sourceBuffer.get(2), causing an IndexOutOfBoundsException.
            // The other arguments can be minimal as they are not reached.
            AppendableJoiner.joinSB(target, bufferView, "", "", null);
            fail("Expected an IndexOutOfBoundsException to be thrown.");
        } catch (final IndexOutOfBoundsException e) {
            // This is the expected outcome. The test passes.
        }
    }
}