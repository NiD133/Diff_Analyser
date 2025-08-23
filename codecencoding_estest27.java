package org.apache.commons.compress.harmony.pack200;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for the {@link CodecEncoding} class.
 */
public class CodecEncodingTest {

    /**
     * Tests that {@link CodecEncoding#getCodec(int, InputStream, Codec)} throws an
     * IOException when provided with an encoding value that is out of the valid range.
     */
    @Test
    public void getCodecShouldThrowIOExceptionForInvalidEncodingValue() {
        // Arrange
        final int invalidEncodingValue = 257;
        final BHSDCodec defaultCodec = Codec.DELTA5;
        // The input stream can be empty as the method should fail before reading from it.
        final InputStream inputStream = new ByteArrayInputStream(new byte[0]);

        // Act & Assert
        try {
            CodecEncoding.getCodec(invalidEncodingValue, inputStream, defaultCodec);
            fail("Expected an IOException to be thrown for an invalid codec value.");
        } catch (IOException e) {
            // Verify that the exception message clearly indicates the invalid value.
            final String expectedMessage = "Invalid codec encoding byte (" + invalidEncodingValue + ") found";
            assertEquals(expectedMessage, e.getMessage());
        } catch (Pack200Exception e) {
            fail("A Pack200Exception was thrown, but an IOException was expected. " + e.getMessage());
        }
    }
}