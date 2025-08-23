package org.apache.commons.compress.harmony.pack200;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.Assert.assertNull;

/**
 * This test class focuses on the behavior of the CodecEncoding.getCodec() method.
 */
public class CodecEncodingTest {

    /**
     * Tests that getCodec() returns the default codec when the encoding value is 0.
     * <p>
     * According to the Pack200 specification, an encoding value of 0 is a special
     * case that signifies "use the default codec for this band". This test verifies
     * that behavior by passing null as the default codec and asserting that null is returned.
     * </p>
     */
    @Test
    public void getCodecWithValueZeroShouldReturnTheProvidedDefaultCodec() throws Exception {
        // Arrange: A value of 0 indicates that the default codec should be used.
        final int encodingValue = 0;
        final Codec defaultCodec = null;
        // The input stream is not used for this encoding value, but the method requires it.
        // An empty stream is sufficient and simpler than using piped streams.
        final InputStream inputStream = new ByteArrayInputStream(new byte[0]);

        // Act: Call the method under test.
        final Codec result = CodecEncoding.getCodec(encodingValue, inputStream, defaultCodec);

        // Assert: The result should be the same as the default codec provided.
        assertNull("When the encoding value is 0, the method should return the provided default codec.", result);
    }
}