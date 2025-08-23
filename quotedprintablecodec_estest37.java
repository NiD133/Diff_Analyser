package org.apache.commons.codec.net;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Tests for the {@link QuotedPrintableCodec} class.
 */
public class QuotedPrintableCodecTest {

    /**
     * Tests that the constructor throws an IllegalArgumentException when a null
     * charset name is provided.
     */
    @Test
    public void constructorWithNullCharsetNameShouldThrowIllegalArgumentException() {
        try {
            // Attempt to instantiate the codec with a null charset name.
            new QuotedPrintableCodec((String) null);
            fail("Expected an IllegalArgumentException to be thrown for a null charset name.");
        } catch (final IllegalArgumentException e) {
            // Verify that the caught exception has the expected message.
            // The underlying java.nio.charset.Charset.forName(null) call is the source of this exception.
            assertEquals("Null charset name", e.getMessage());
        }
    }
}