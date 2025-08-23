package org.apache.commons.codec.net;

import org.apache.commons.codec.CodecPolicy;
import org.junit.Test;

import java.nio.charset.Charset;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Test suite for {@link BCodec}.
 */
public class BCodecTest {

    @Test
    public void constructorWithNullCharsetShouldThrowNullPointerException() {
        // Arrange: Define the invalid input (a null charset) and a valid policy.
        final Charset nullCharset = null;
        final CodecPolicy policy = CodecPolicy.STRICT;

        // Act & Assert: Verify that calling the constructor with a null charset
        // throws a NullPointerException. We use assertThrows for clear and concise
        // exception testing.
        final NullPointerException exception = assertThrows(
            "Constructor should reject a null charset.",
            NullPointerException.class,
            () -> new BCodec(nullCharset, policy)
        );

        // Further assert that the exception message correctly identifies the problematic parameter.
        // This confirms the validation is working as expected.
        assertEquals(
            "The exception message should indicate the 'charset' parameter is the cause.",
            "charset",
            exception.getMessage()
        );
    }
}