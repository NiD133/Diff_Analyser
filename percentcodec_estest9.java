package org.apache.commons.codec.net;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for the PercentCodec class.
 * Note: The original class name 'PercentCodec_ESTestTest9' was changed to 'PercentCodecTest'
 * to follow standard naming conventions.
 */
public class PercentCodecTest {

    /**
     * Tests that the encode(Object) method throws an EncoderException when
     * passed an object that is not a byte array.
     */
    @Test
    public void encodeShouldThrowEncoderExceptionForUnsupportedObjectType() {
        // Arrange: Create a codec instance and an object of an unsupported type.
        // Using a plain Object is a clear and simple way to represent this case.
        PercentCodec percentCodec = new PercentCodec();
        Object unsupportedObject = new Object();

        // Act & Assert: Verify that encoding the unsupported object throws the correct exception.
        try {
            percentCodec.encode(unsupportedObject);
            fail("Expected an EncoderException to be thrown for an unsupported object type.");
        } catch (final EncoderException e) {
            // Verify that the exception type is correct and the message is as expected.
            final String expectedMessage = "Objects of type " + unsupportedObject.getClass().getName() + " cannot be Percent encoded";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}