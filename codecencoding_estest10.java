package org.apache.commons.compress.harmony.pack200;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

// Note: The original test class name and scaffolding are preserved.
// Unused imports from the original test have been removed for clarity.
public class CodecEncoding_ESTestTest10 extends CodecEncoding_ESTest_scaffolding {

    /**
     * Tests that getCodec() with a specifier value of 116 correctly consumes two
     * additional bytes from the input stream to define a composite codec.
     */
    @Test(timeout = 4000)
    public void testGetCodecWithCompositeSpecifierConsumesBytesFromStream() throws Exception {
        // --- Arrange ---

        // A specifier value >= 116 indicates a non-canonical, composite codec
        // whose definition is read from the input stream.
        final int COMPOSITE_CODEC_SPECIFIER = 116;
        final int INITIAL_CODEC_SPECIFIER = 30;
        final int initialStreamSize = 19;

        // The input stream contains data for multiple operations.
        // byte[0] = 0: Consumed by the initial 'decode' call during setup.
        // byte[1] = 101: First byte read by getCodec(116, ...).
        // byte[2] = 0: Second byte read by getCodec(116, ...).
        byte[] inputData = new byte[initialStreamSize];
        inputData[1] = (byte) 101;
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputData);

        // Setup step 1: Partially consume the stream to prepare for the main test action.
        // This simulates a real-world scenario where the stream is already in use.
        BHSDCodec initialCodec = CodecEncoding.getCanonicalCodec(INITIAL_CODEC_SPECIFIER);
        initialCodec.decode(inputStream); // Consumes 1 byte (the leading zero).

        // Setup step 2: Create a default codec required by the getCodec method signature.
        BHSDCodec innerCodec = CodecEncoding.getCanonicalCodec(INITIAL_CODEC_SPECIFIER);
        RunCodec defaultCodec = new RunCodec(67, innerCodec, innerCodec);

        // --- Act ---

        // Call the method under test. This is expected to consume two bytes (101 and 0)
        // from the stream to construct the new codec.
        CodecEncoding.getCodec(COMPOSITE_CODEC_SPECIFIER, inputStream, defaultCodec);

        // --- Assert ---

        // Verify that the stream position has advanced as expected.
        // 19 (initial) - 1 (from decode) - 2 (from getCodec) = 16 bytes remaining.
        int expectedRemainingBytes = initialStreamSize - 3;
        assertEquals("getCodec() should consume the correct number of bytes.",
                expectedRemainingBytes, inputStream.available());

        // Also, verify a side-effect of the initial setup 'decode' call to ensure
        // our test setup was correct.
        assertEquals("Last band length from the initial decode should be 1.",
                1, initialCodec.lastBandLength);
    }
}