package org.apache.commons.compress.harmony.pack200;

import static org.junit.Assert.assertThrows;

import java.io.InputStream;
import org.junit.Test;

/**
 * This class contains tests for the {@link CodecEncoding#getCodec(int, InputStream, Codec)} method.
 * This improved test focuses on clarity and maintainability.
 */
public class CodecEncodingTest {

    /**
     * Tests that getCodec() throws a NullPointerException when the input stream is null
     * but the encoding value requires data to be read from it.
     */
    @Test
    public void getCodecShouldThrowNullPointerExceptionWhenInputStreamIsRequiredAndNull() {
        // Arrange
        // According to the Pack200 specification, a value >= 116 requires reading
        // from the input stream to determine the full codec definition. We use 128 as a representative value.
        final int valueRequiringInputStream = 128;
        final InputStream nullInputStream = null;
        final Codec defaultCodec = Codec.BYTE1;

        // Act & Assert
        // The call should fail with a NullPointerException because the stream is null,
        // but the method attempts to read from it based on the provided value.
        assertThrows(NullPointerException.class, () -> {
            CodecEncoding.getCodec(valueRequiringInputStream, nullInputStream, defaultCodec);
        });
    }
}