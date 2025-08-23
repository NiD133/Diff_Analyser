package org.apache.commons.codec.net;

import org.junit.Test;

/**
 * Contains tests for the {@link URLCodec} class, focusing on its behavior
 * when constructed with specific charsets.
 */
public class URLCodecTest {

    /**
     * Verifies that calling encode() on a URLCodec instance initialized with a null
     * charset throws a NullPointerException.
     * <p>
     * The encoding process internally requires a charset to convert the input string
     * to bytes. When this charset is null, the underlying call to
     * {@code String.getBytes(charsetName)} results in an NPE, which is the expected
     * behavior being tested here.
     */
    @Test(expected = NullPointerException.class)
    public void encodeObjectWithNullCharsetShouldThrowNullPointerException() {
        // Arrange: Create a URLCodec with a null charset.
        URLCodec urlCodec = new URLCodec(null);
        Object objectToEncode = "any string";

        // Act: Attempt to encode an object. This should trigger the exception.
        urlCodec.encode(objectToEncode);

        // Assert: The @Test(expected) annotation handles the assertion that a
        // NullPointerException was thrown.
    }
}