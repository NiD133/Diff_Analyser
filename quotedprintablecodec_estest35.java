package org.apache.commons.codec.net;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.nio.charset.UnsupportedCharsetException;
import org.junit.Test;

/**
 * Unit tests for the {@link QuotedPrintableCodec} class, focusing on constructor behavior.
 */
public class QuotedPrintableCodecTest {

    /**
     * Tests that the QuotedPrintableCodec constructor throws an UnsupportedCharsetException
     * when initialized with an invalid or unsupported charset name.
     */
    @Test
    public void constructorWithUnsupportedCharsetNameShouldThrowException() {
        // Arrange: Define an invalid charset name that is highly unlikely to ever be supported.
        final String invalidCharsetName = "X-INVALID-CHARSET";

        // Act & Assert: Verify that instantiating the codec with the invalid name throws the correct exception.
        final UnsupportedCharsetException thrown = assertThrows(
            UnsupportedCharsetException.class,
            () -> new QuotedPrintableCodec(invalidCharsetName)
        );

        // Assert: Further verify that the exception message contains the invalid charset name.
        assertEquals(invalidCharsetName, thrown.getMessage());
    }
}