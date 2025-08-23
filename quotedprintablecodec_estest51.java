package org.apache.commons.codec.net;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;

import static org.junit.Assert.assertNull;

/**
 * Contains tests for the {@link QuotedPrintableCodec} class.
 */
public class QuotedPrintableCodecTest {

    /**
     * Tests that the {@code encode(Object)} method returns null when the input is null.
     * This is a standard behavior for encoders in the library.
     */
    @Test
    public void encode_withNullObject_shouldReturnNull() throws EncoderException {
        // Arrange: Create a codec instance. The strictness flag is irrelevant for this test case.
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        Object nullObject = null;

        // Act: Call the method under test with a null input.
        Object result = codec.encode(nullObject);

        // Assert: Verify that the output is null.
        assertNull("Encoding a null object should return null.", result);
    }
}