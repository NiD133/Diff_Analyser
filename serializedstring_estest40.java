package com.fasterxml.jackson.core.io;

import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;

/**
 * This class contains tests for the {@link SerializedString} class.
 * The original test was auto-generated and has been improved for clarity.
 */
public class SerializedString_ESTestTest40 extends SerializedString_ESTest_scaffolding {

    /**
     * Verifies that calling {@link SerializedString#writeQuotedUTF8(OutputStream)} with a null
     * OutputStream throws a {@link NullPointerException}.
     */
    @Test(expected = NullPointerException.class)
    public void writeQuotedUTF8_withNullOutputStream_shouldThrowNullPointerException() throws IOException {
        // Arrange: Create a SerializedString instance. The actual string content is not
        // relevant for this test, as we are only checking for null-safety.
        SerializedString serializedString = new SerializedString("any value");

        // Act & Assert: Attempt to write to a null output stream.
        // The @Test(expected) annotation asserts that a NullPointerException is thrown.
        serializedString.writeQuotedUTF8(null);
    }
}