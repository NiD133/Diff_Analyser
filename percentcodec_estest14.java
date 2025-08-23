package org.apache.commons.codec.net;

import org.apache.commons.codec.DecoderException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Contains tests for the {@link PercentCodec} class.
 * This class focuses on improving a specific auto-generated test case.
 */
public class PercentCodecTest {

    /**
     * Verifies that the decode(Object) method throws a DecoderException when passed an object
     * that is not a byte array, as the method is only designed to handle byte arrays.
     */
    @Test
    public void decodeWithUnsupportedObjectTypeShouldThrowDecoderException() {
        // Arrange: Create a codec and an input object of an unsupported type.
        // In this case, we use the codec instance itself as the invalid input.
        final PercentCodec percentCodec = new PercentCodec();
        final Object invalidInput = percentCodec;
        final String expectedMessage = "Objects of type " + invalidInput.getClass().getName() + " cannot be Percent decoded";

        // Act & Assert
        try {
            percentCodec.decode(invalidInput);
            fail("Expected a DecoderException to be thrown for an unsupported object type.");
        } catch (final DecoderException e) {
            // Verify that the correct exception was thrown with the expected message.
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}