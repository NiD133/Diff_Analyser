package org.apache.commons.codec.net;

import org.apache.commons.codec.DecoderException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

// The test class name and inheritance are kept from the original for context.
public class BCodec_ESTestTest30 extends BCodec_ESTest_scaffolding {

    /**
     * Tests that the decode(Object) method throws a DecoderException when passed
     * an object that is not a String, as specified by its contract.
     */
    @Test
    public void decode_withUnsupportedObjectType_shouldThrowDecoderException() {
        // Arrange: Create a codec instance and an input object of an unsupported type.
        // The decode(Object) method is designed to only accept Strings.
        BCodec codec = new BCodec();
        Object unsupportedInput = new BCodec(); // Any non-String object will do.

        try {
            // Act: Attempt to decode the unsupported object.
            codec.decode(unsupportedInput);
            fail("Expected a DecoderException to be thrown for an unsupported object type.");
        } catch (final DecoderException e) {
            // Assert: Verify that the correct exception was thrown with the expected message.
            final String expectedMessage = "Objects of type " +
                                           unsupportedInput.getClass().getName() +
                                           " cannot be decoded using BCodec";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}