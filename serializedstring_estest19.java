package com.fasterxml.jackson.core.io;

import org.junit.Test;
import java.io.IOException;
import java.io.OutputStream;

/**
 * This test suite focuses on the SerializedString class.
 * This specific test case verifies the behavior of the writeUnquotedUTF8 method.
 */
public class SerializedString_ESTestTest19 extends SerializedString_ESTest_scaffolding {

    /**
     * Verifies that writeUnquotedUTF8 throws a NullPointerException
     * when the provided OutputStream is null.
     */
    @Test(timeout = 4000, expected = NullPointerException.class)
    public void writeUnquotedUTF8ShouldThrowNullPointerExceptionWhenOutputStreamIsNull() throws IOException {
        // Arrange: Create a SerializedString instance. The content is irrelevant for this test.
        SerializedString serializedString = new SerializedString("any-value");

        // Act: Attempt to write to a null OutputStream.
        // Assert: A NullPointerException is expected, as declared in the @Test annotation.
        serializedString.writeUnquotedUTF8(null);
    }
}