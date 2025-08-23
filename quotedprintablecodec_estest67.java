package org.apache.commons.codec.net;

import org.junit.Test;
import java.nio.charset.Charset;

/**
 * This test case verifies the behavior of the QuotedPrintableCodec's decode method
 * when the codec is constructed with a null charset.
 */
public class QuotedPrintableCodecExceptionTest {

    /**
     * Tests that calling decode(Object) with a String on a codec initialized
     * with a null charset throws a NullPointerException.
     *
     * The internal call to decode the string requires a charset to construct the
     * final string. Passing a null charset to the underlying String constructor
     * results in an NPE, which is the expected behavior in this scenario.
     */
    @Test(expected = NullPointerException.class)
    public void decodeObjectAsStringWithNullCharsetShouldThrowNullPointerException() throws Exception {
        // Arrange: Create a codec with a null charset.
        final QuotedPrintableCodec codec = new QuotedPrintableCodec((Charset) null);
        final Object input = "";

        // Act: Attempt to decode the object, which is a String.
        codec.decode(input);

        // Assert: A NullPointerException is expected, as specified by the @Test annotation.
    }
}