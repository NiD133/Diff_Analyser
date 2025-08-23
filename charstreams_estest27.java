package com.google.common.io;

import java.io.IOException;
import java.nio.CharBuffer;
import org.junit.Test;

/**
 * Tests for {@link CharStreams}.
 */
public class CharStreamsTest {

    /**
     * Verifies that copy() throws a NullPointerException when the source Readable is null.
     */
    @Test(expected = NullPointerException.class)
    public void copy_fromNullReadable_throwsNullPointerException() throws IOException {
        // Arrange: Create a destination buffer. The source is intentionally null.
        CharBuffer destinationBuffer = CharStreams.createBuffer();

        // Act: Attempt to copy from a null source.
        // Assert: A NullPointerException is expected, as declared in the @Test annotation.
        CharStreams.copy(null, destinationBuffer);
    }
}