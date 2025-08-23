package org.apache.commons.codec.net;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.nio.charset.UnsupportedCharsetException;
import org.junit.Test;

/**
 * Tests for the {@link BCodec} class, focusing on its constructor behavior.
 */
public class BCodecConstructorTest {

    /**
     * Tests that the BCodec constructor that accepts a charset name throws
     * an UnsupportedCharsetException when provided with an invalid or
     * unsupported charset name.
     */
    @Test
    public void constructorWithUnsupportedCharsetNameShouldThrowException() {
        // Arrange: Define an invalid charset name that is not supported by the Java runtime.
        final String unsupportedCharsetName = "p-Ubb";

        // Act & Assert: Verify that instantiating BCodec with the unsupported charset name
        // throws the expected exception. The BCodec constructor delegates to Charset.forName(),
        // which is responsible for throwing this exception.
        UnsupportedCharsetException thrown = assertThrows(
            UnsupportedCharsetException.class,
            () -> new BCodec(unsupportedCharsetName)
        );

        // Further Assert: Check that the exception message correctly identifies the problematic charset.
        assertEquals(unsupportedCharsetName, thrown.getMessage());
    }
}