package com.google.common.io;

import org.junit.Test;
import java.io.IOException;
import java.io.StringReader;

/**
 * This test suite contains an improved test case for the
 * {@link CharStreams#readLines(Readable, LineProcessor)} method.
 */
public class CharStreams_ESTestTest14 extends CharStreams_ESTest_scaffolding {

    /**
     * Verifies that readLines() throws a NullPointerException when the provided
     * LineProcessor is null. This is expected behavior, as the method uses
     * Preconditions to check for non-null arguments.
     */
    @Test(expected = NullPointerException.class)
    public void readLines_withNullProcessor_throwsNullPointerException() throws IOException {
        // Arrange: Create a simple, non-null Readable instance. A StringReader is
        // sufficient as the content does not matter for this test.
        StringReader dummyReader = new StringReader("any content");

        // Act: Call the method under test with a null LineProcessor.
        // Assert: The @Test(expected=...) annotation handles the assertion,
        // ensuring that a NullPointerException is thrown.
        CharStreams.readLines(dummyReader, null);
    }
}