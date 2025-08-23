package org.apache.commons.codec.net;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.apache.commons.codec.DecoderException;
import org.junit.Test;

/**
 * This test class contains tests for the BCodec, focusing on its
 * adherence to RFC 1522 decoding rules.
 */
public class BCodecTest {

    /**
     * Tests that the decodeText method throws a DecoderException when given a
     * malformed input string that contains multiple RFC 1522 start sequences ("=?").
     */
    @Test
    public void decodeTextShouldThrowDecoderExceptionForMalformedInputWithMultiplePrefixes() {
        // Arrange
        BCodec bCodec = new BCodec();
        // An RFC 1522 "encoded-word" must start with "=?" and end with "?=".
        // This input is malformed because it contains multiple start sequences.
        String malformedText = "=?=?=?ZCg05nk5fYK>>";
        String expectedErrorMessage = "RFC 1522 violation: malformed encoded content";

        // Act & Assert
        try {
            bCodec.decodeText(malformedText);
            fail("Expected a DecoderException to be thrown for malformed input");
        } catch (DecoderException e) {
            // Verify that the exception message is correct.
            assertEquals(expectedErrorMessage, e.getMessage());
        } catch (Exception e) {
            // Fail the test if an unexpected exception type is thrown.
            fail("Expected a DecoderException, but caught " + e.getClass().getName());
        }
    }
}