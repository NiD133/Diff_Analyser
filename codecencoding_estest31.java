package org.apache.commons.compress.harmony.pack200;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.InputStream;
import java.io.IOException;

import org.apache.commons.compress.harmony.pack200.BHSDCodec;
import org.apache.commons.compress.harmony.pack200.Codec;
import org.apache.commons.compress.harmony.pack200.CodecEncoding;
import org.apache.commons.compress.harmony.pack200.Pack200Exception;

/**
 * This test class is not part of the original submission.
 * It is a container for the improved test method.
 * The original test extended a scaffolding class, which is omitted here for brevity.
 */
public class CodecEncodingImprovedTest {

    /**
     * Tests that getCodec() throws an EOFException when the input stream
     * is exhausted while parsing a custom codec definition.
     */
    @Test(timeout = 4000, expected = EOFException.class)
    public void getCodecWithCustomEncodingShouldThrowEOFExceptionIfStreamIsExhausted() throws IOException, Pack200Exception {
        // Arrange
        // The Pack200 specification states that for a codec specifier value >= 116,
        // additional bytes are read from the input stream to define the codec.
        final int customCodecSpecifier = 116;

        // We provide a stream with only one byte, which is insufficient to define
        // the custom codec, thus forcing an End-Of-File (EOF) condition.
        final byte[] insufficientData = { 0x00 };
        final InputStream inputStream = new ByteArrayInputStream(insufficientData);
        final BHSDCodec defaultCodec = Codec.MDELTA5;

        // Act
        // This call is expected to throw an EOFException because the stream
        // will run out of data while trying to read the codec's parameters.
        CodecEncoding.getCodec(customCodecSpecifier, inputStream, defaultCodec);

        // Assert
        // The 'expected = EOFException.class' annotation on the @Test method
        // handles the assertion, failing the test if this exception is not thrown.
    }
}