package org.apache.commons.compress.harmony.pack200;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * This test suite is focused on the CodecEncoding class.
 * The original test was auto-generated and has been refactored for clarity.
 */
public class CodecEncoding_ESTestTest15 { // Note: Test class name kept for consistency.

    /**
     * Tests that getCodec() throws an IllegalArgumentException when provided with a negative
     * encoding value, as this is an invalid input.
     */
    @Test
    public void getCodecWithNegativeEncodingValueShouldThrowIllegalArgumentException() {
        // Arrange
        final int negativeEncodingValue = -1;
        final String expectedErrorMessage = "Encoding cannot be less than zero";

        // The input stream and default codec are required by the method signature but are not
        // relevant to this specific test case. We use simple dummy values.
        final InputStream dummyInputStream = new ByteArrayInputStream(new byte[0]);
        final Codec dummyDefaultCodec = Codec.UNSIGNED5;

        // Act & Assert
        try {
            CodecEncoding.getCodec(negativeEncodingValue, dummyInputStream, dummyDefaultCodec);
            fail("Expected an IllegalArgumentException to be thrown, but it was not.");
        } catch (final IllegalArgumentException e) {
            // Verify that the exception has the expected message.
            assertEquals(expectedErrorMessage, e.getMessage());
        } catch (final Exception e) {
            // Fail the test if an unexpected exception type is thrown.
            fail("Expected an IllegalArgumentException, but caught " + e.getClass().getName());
        }
    }
}