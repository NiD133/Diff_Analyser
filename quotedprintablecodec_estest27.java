package org.apache.commons.codec.net;

import org.junit.Test;

import java.nio.charset.Charset;

/**
 * Contains improved tests for the {@link QuotedPrintableCodec} class, focusing on error handling.
 */
public class QuotedPrintableCodecImprovedTest {

    /**
     * Tests that attempting to encode a String using a QuotedPrintableCodec
     * initialized with a null charset results in a NullPointerException.
     *
     * <p>The encoding process for a String requires a valid charset to convert the
     * string into bytes. Providing a null charset creates an invalid state for the
     * codec, and the operation is expected to fail when the charset is accessed
     * internally.</p>
     */
    @Test(expected = NullPointerException.class)
    public void encodeStringWithNullCharsetShouldThrowNullPointerException() {
        // Arrange: Create a codec instance with a null charset, which is an invalid
        // configuration for encoding strings.
        QuotedPrintableCodec codec = new QuotedPrintableCodec((Charset) null);
        Object stringToEncode = "This is a test string.";

        // Act: Attempt to encode the string object.
        // The assertion is handled by the 'expected' attribute of the @Test annotation,
        // which verifies that a NullPointerException is thrown.
        codec.encode(stringToEncode);
    }
}