package org.apache.commons.codec.net;

import org.junit.Test;
import java.nio.charset.IllegalCharsetNameException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Tests for the constructor of {@link QuotedPrintableCodec}.
 */
public class QuotedPrintableCodecTest {

    /**
     * Tests that the constructor taking a charset name throws
     * an IllegalCharsetNameException when provided with a syntactically invalid name.
     */
    @Test
    public void constructorShouldThrowExceptionForInvalidCharsetName() {
        // Arrange: An invalid charset name containing illegal characters.
        final String invalidCharsetName = "4 >&$1^)O:\"";

        // Act & Assert: Verify that creating a codec with the invalid name throws the expected exception.
        // The assertThrows method executes the lambda and asserts that it throws the specified exception type.
        IllegalCharsetNameException thrown = assertThrows(
            IllegalCharsetNameException.class,
            () -> new QuotedPrintableCodec(invalidCharsetName)
        );

        // Assert: Further verify that the exception message correctly reports the invalid name.
        assertEquals(invalidCharsetName, thrown.getMessage());
    }
}