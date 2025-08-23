package org.apache.commons.compress.harmony.pack200;

import java.io.InputStream;
import org.junit.Test;

/**
 * Test suite for the {@link CodecEncoding} class, focusing on its exception handling.
 */
public class CodecEncodingTest {

    /**
     * Verifies that {@code CodecEncoding.getCodec()} throws a {@code NullPointerException}
     * when a null {@code InputStream} is provided for an encoding that requires reading
     * from the stream.
     *
     * <p>The encoding value 181 is chosen because it falls into a range (148-188) that
     * mandates reading additional data from the input stream to define the codec.
     * Passing a null stream under these conditions should result in an immediate
     * {@code NullPointerException}.</p>
     *
     * @throws Exception to allow checked exceptions from the method under test to propagate,
     *                   which is standard practice in test methods.
     */
    @Test(expected = NullPointerException.class)
    public void getCodecShouldThrowNullPointerExceptionForNullStreamWhenRequired() throws Exception {
        // Arrange: Define an encoding value that requires stream access.
        final int encodingThatRequiresStream = 181;
        final InputStream nullInputStream = null;
        final Codec defaultCodec = null; // This argument is not relevant for this specific failure path.

        // Act & Assert: This call is expected to throw a NullPointerException.
        CodecEncoding.getCodec(encodingThatRequiresStream, nullInputStream, defaultCodec);
    }
}