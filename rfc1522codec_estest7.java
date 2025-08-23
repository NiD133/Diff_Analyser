package org.apache.commons.codec.net;

import org.junit.Test;
import java.nio.charset.Charset;
import org.apache.commons.codec.EncoderException;

/**
 * This test class verifies the behavior of the abstract RFC1522Codec class,
 * specifically focusing on shared utility methods. It uses the concrete QCodec
 * implementation to instantiate and test the abstract class's functionality.
 */
public class RFC1522CodecTest {

    /**
     * Tests that the encodeText method throws a NullPointerException when the
     * provided Charset is null. The RFC 1522 encoding process requires a
     * non-null character set to correctly encode the text.
     *
     * @throws EncoderException This is a declared exception for the encodeText method,
     *                          but a NullPointerException is expected in this specific test case.
     */
    @Test(expected = NullPointerException.class)
    public void encodeTextShouldThrowNullPointerExceptionForNullCharset() throws EncoderException {
        // Arrange: Create an instance of a concrete RFC1522Codec implementation.
        // The text content is irrelevant for this test.
        final QCodec qCodec = new QCodec();
        final String textToEncode = "any text";

        // Act & Assert: Call the method with a null Charset.
        // The @Test(expected) annotation asserts that a NullPointerException is thrown.
        qCodec.encodeText(textToEncode, (Charset) null);
    }
}