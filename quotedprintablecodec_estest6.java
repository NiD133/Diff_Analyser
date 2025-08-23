package org.apache.commons.codec.net;

import org.junit.Test;
import java.nio.charset.Charset;
import static org.junit.Assert.assertNull;

/**
 * Unit tests for {@link QuotedPrintableCodec}.
 */
public class QuotedPrintableCodecTest {

    /**
     * Tests that getCharset() returns null if the codec was constructed with a null Charset.
     * This verifies that the constructor correctly handles and stores a null value.
     */
    @Test
    public void getCharsetShouldReturnNullWhenConstructedWithNullCharset() {
        // Arrange: Create a codec instance, explicitly passing null for the charset.
        // The cast to (Charset) is necessary to resolve ambiguity between constructors.
        final QuotedPrintableCodec codec = new QuotedPrintableCodec((Charset) null);

        // Act: Retrieve the charset from the codec instance.
        final Charset resultCharset = codec.getCharset();

        // Assert: Verify that the retrieved charset is null, as expected.
        assertNull("The charset should be null when the codec is initialized with a null Charset.", resultCharset);
    }
}