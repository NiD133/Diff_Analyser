package org.apache.commons.compress.harmony.pack200;

import org.junit.Test;

/**
 * This test case verifies the behavior of the CodecEncoding.getSpecifier() method,
 * specifically its handling of null inputs.
 */
public class CodecEncoding_ESTestTest8 extends CodecEncoding_ESTest_scaffolding {

    /**
     * Tests that CodecEncoding.getSpecifier() throws a NullPointerException
     * when the second argument (the default codec) is null.
     */
    @Test(expected = NullPointerException.class)
    public void getSpecifierShouldThrowNullPointerExceptionWhenDefaultCodecIsNull() {
        // Arrange: Create a PopulationCodec to pass as the first argument.
        // The internal codecs can be null as they are not relevant to this test.
        final PopulationCodec codec = new PopulationCodec(null, null, null);

        // Act & Assert: Call the method with a null default codec.
        // This is expected to throw a NullPointerException.
        CodecEncoding.getSpecifier(codec, null);
    }
}