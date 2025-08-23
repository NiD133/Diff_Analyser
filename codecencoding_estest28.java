package org.apache.commons.compress.harmony.pack200;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

// The original test class name is kept for context.
public class CodecEncoding_ESTestTest28 extends CodecEncoding_ESTest_scaffolding {

    /**
     * Tests that getCodec() for encoding format 140 correctly decodes a RunCodec.
     * A RunCodec encoding requires reading two additional bytes from the input stream
     * to determine its sub-codecs.
     */
    @Test(timeout = 4000)
    public void getCodecWithFormat140_shouldReturnRunCodec_andConsumeTwoBytes() throws Exception {
        // Arrange
        final int runCodecEncodingFormat = 140;
        final int expectedKValue = 4096; // The 'K' parameter of the RunCodec for this format.
        final int expectedBytesConsumed = 2; // A RunCodec reads two bytes for its sub-codecs.

        // Set up an input stream. The content doesn't matter for this test, only that
        // it has enough bytes to be read.
        byte[] inputData = new byte[10];
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputData);
        final int initialAvailableBytes = inputStream.available();

        // A default codec is required by the getCodec method signature.
        // Its specific value is not relevant to this particular decoding path.
        final Codec defaultCodec = new PopulationCodec(Codec.UDELTA5, Codec.BYTE1, Codec.UDELTA5);

        // Act
        Codec resultCodec = CodecEncoding.getCodec(runCodecEncodingFormat, inputStream, defaultCodec);

        // Assert
        // 1. Verify the returned codec is the correct type.
        assertNotNull("The returned codec should not be null.", resultCodec);
        assertTrue("Codec should be an instance of RunCodec for format " + runCodecEncodingFormat,
                   resultCodec instanceof RunCodec);
        RunCodec runCodec = (RunCodec) resultCodec;

        // 2. Verify the properties of the created RunCodec.
        assertEquals("The K-value of the RunCodec is incorrect.", expectedKValue, runCodec.getK());

        // 3. Verify that the correct number of bytes were consumed from the input stream.
        final int finalAvailableBytes = inputStream.available();
        assertEquals("The number of bytes consumed from the stream is incorrect.",
                     expectedBytesConsumed, initialAvailableBytes - finalAvailableBytes);
    }
}