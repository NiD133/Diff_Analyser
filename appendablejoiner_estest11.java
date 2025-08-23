package org.apache.commons.lang3;

import org.junit.Test;

import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.ReadOnlyBufferException;
import java.util.Collections;

/**
 * Tests for {@link AppendableJoiner}.
 */
public class AppendableJoinerTest {

    @Test(expected = ReadOnlyBufferException.class)
    public void joinToReadOnlyBufferShouldThrowException() throws IOException {
        // Arrange: Create a joiner and a read-only buffer as the target.
        final AppendableJoiner<String> joiner = AppendableJoiner.builder().get();
        final CharBuffer readOnlyBuffer = CharBuffer.wrap("cannot-be-modified");
        final Iterable<String> elements = Collections.emptyList();

        // Act: Attempt to join elements into the read-only buffer.
        // The joiner will first attempt to append its prefix, which triggers the exception.
        joiner.joinA(readOnlyBuffer, elements);

        // Assert: The @Test(expected) annotation verifies that a ReadOnlyBufferException is thrown.
    }
}