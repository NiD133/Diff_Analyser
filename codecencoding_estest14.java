package org.apache.commons.compress.harmony.pack200;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

/**
 * This test class is a refactored version of an auto-generated test.
 * The original class name and inheritance structure have been preserved.
 */
public class CodecEncoding_ESTestTest14 extends CodecEncoding_ESTest_scaffolding {

    /**
     * Tests that getCodec() throws a NullPointerException when the input stream is null
     * but the encoding value requires reading from it to determine the final codec.
     *
     * @throws IOException      if an I/O error occurs (as declared by the method under test).
     * @throws Pack200Exception if a Pack200 error occurs (as declared by the method under test).
     */
    @Test(timeout = 4000, expected = NullPointerException.class)
    public void getCodecShouldThrowNPEWhenInputStreamIsNullAndRequired() throws IOException, Pack200Exception {
        // According to the Pack200 specification, an encoding value >= 116
        // requires reading extra bytes from the input stream to define the codec.
        final int encodingValueRequiringStream = 117;
        final BHSDCodec defaultCodec = Codec.SIGNED5;

        // Act: Call getCodec with a null input stream.
        // Assert: The @Test(expected) annotation verifies that a NullPointerException is thrown.
        CodecEncoding.getCodec(encodingValueRequiringStream, null, defaultCodec);
    }
}