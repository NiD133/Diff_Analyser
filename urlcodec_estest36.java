package org.apache.commons.codec.net;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * This test suite contains refactored tests for the URLCodec class.
 * The original test was auto-generated and has been improved for clarity and maintainability.
 */
// The original test class name and inheritance are kept to match the provided context.
// In a real-world scenario, the scaffolding and EvoSuite runner would also be removed.
public class URLCodec_ESTestTest36 extends URLCodec_ESTest_scaffolding {

    /**
     * Tests that the encode(Object) method throws an EncoderException when passed an object
     * that is not a String or a byte array. The method is only designed to handle these two types.
     */
    @Test
    public void encodeWithUnsupportedObjectTypeShouldThrowEncoderException() {
        // Arrange: Create a URLCodec instance and an object of an unsupported type.
        // For this test, we use a new Object() as a clear example of an invalid input.
        URLCodec urlCodec = new URLCodec();
        Object unsupportedObject = new Object();

        // Act & Assert: Attempt to encode the unsupported object and verify the resulting exception.
        try {
            urlCodec.encode(unsupportedObject);
            fail("Expected an EncoderException because the input object type is not supported.");
        } catch (EncoderException e) {
            // Verify that the exception message correctly identifies the unsupported type.
            String expectedMessage = "Objects of type " + unsupportedObject.getClass().getName() + " cannot be URL encoded";
            assertEquals("The exception message does not match the expected format.", expectedMessage, e.getMessage());
        }
    }
}