package org.apache.commons.codec.net;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.nio.charset.IllegalCharsetNameException;

/**
 * Tests for the RFC1522Codec class, focusing on exception handling for invalid inputs.
 */
public class RFC1522CodecTest {

    /**
     * Verifies that the encodeText method throws an IllegalCharsetNameException
     * when provided with a syntactically invalid charset name.
     */
    @Test
    public void encodeTextWithInvalidCharsetNameShouldThrowException() {
        // Arrange
        final BCodec codec = new BCodec();
        final String inputText = "Any text can be used here";
        // A charset name containing spaces is syntactically invalid.
        final String invalidCharsetName = "This is an invalid charset name";

        // Act & Assert
        try {
            codec.encodeText(inputText, invalidCharsetName);
            fail("Expected an IllegalCharsetNameException to be thrown due to the invalid charset name.");
        } catch (final IllegalCharsetNameException e) {
            // This is the expected outcome.
            // We can optionally assert that the exception message contains the invalid name
            // to make the test more robust.
            assertEquals(invalidCharsetName, e.getMessage());
        } catch (final Exception e) {
            // Fail the test if any other unexpected exception is thrown.
            fail("Expected IllegalCharsetNameException, but caught " + e.getClass().getSimpleName());
        }
    }
}