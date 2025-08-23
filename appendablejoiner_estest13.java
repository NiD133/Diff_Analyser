package org.apache.commons.lang3;

import org.junit.Test;
import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.ReadOnlyBufferException;
import org.apache.commons.lang3.function.FailableBiConsumer;

/**
 * This test case focuses on the behavior of the AppendableJoiner's static join methods
 * when provided with an invalid {@link Appendable}.
 */
public class AppendableJoinerTest {

    /**
     * Tests that {@code AppendableJoiner.joinA} throws a {@link ReadOnlyBufferException}
     * when the target {@link Appendable} is a read-only buffer.
     * <p>
     * The {@link CharBuffer#wrap(CharSequence)} method creates a read-only buffer.
     * The joiner should throw an exception as soon as it attempts to write to this
     * buffer (e.g., by appending the prefix).
     * </p>
     */
    @Test(expected = ReadOnlyBufferException.class)
    public void testJoinIntoReadOnlyAppendableThrowsException() throws IOException {
        // Arrange: Create a read-only Appendable. CharBuffer.wrap() is a convenient way to do this.
        Appendable readOnlyBuffer = CharBuffer.wrap("initial-content");

        // The specific elements and joiner configuration are secondary, as the exception
        // should occur on the first attempt to write to the buffer.
        String[] elementsToJoin = {"a", "b", "c"};
        CharSequence prefix = "[";
        CharSequence delimiter = ",";
        CharSequence suffix = "]";

        // Act: Attempt to join elements into the read-only buffer.
        // This call is expected to throw a ReadOnlyBufferException.
        AppendableJoiner.joinA(readOnlyBuffer, prefix, suffix, delimiter, FailableBiConsumer.nop(), elementsToJoin);

        // Assert: The 'expected' attribute of the @Test annotation handles the exception assertion.
    }
}