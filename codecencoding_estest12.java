package org.apache.commons.compress.harmony.pack200;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * This class contains tests for the {@link CodecEncoding} class.
 * This specific test focuses on the {@code getCodec} method.
 */
public class CodecEncodingTest { // Renamed from CodecEncoding_ESTestTest12 for clarity

    /**
     * Tests that getCodec, when given an encoding value of 152, correctly reads a
     * byte from the input stream to construct a new BHSDCodec.
     * According to the Pack200 specification, value 152 should result in a
     * BHSDCodec(2, b+1), where 'b' is the byte read from the stream.
     */
    @Test
    public void getCodecForValue152ShouldCreateBHSDCodecFromStreamByte() throws Exception {
        // Arrange
        final int encodingValue = 152;
        final byte streamByte = 99;
        final InputStream inputStream = new ByteArrayInputStream(new byte[]{streamByte});

        // The default codec is a required parameter but is not used for this specific encoding value.
        final Codec defaultCodec = Codec.SIGNED5;

        // The expected codec is a BHSDCodec with b=2 and h=(streamByte + 1).
        final Codec expectedCodec = new BHSDCodec(2, streamByte + 1);

        // Act
        final Codec resultCodec = CodecEncoding.getCodec(encodingValue, inputStream, defaultCodec);

        // Assert
        assertEquals("The returned codec should match the expected BHSDCodec configuration.",
                expectedCodec, resultCodec);
    }
}