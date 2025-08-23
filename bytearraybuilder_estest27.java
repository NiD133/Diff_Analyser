package com.fasterxml.jackson.core.util;

import org.junit.Test;

/**
 * This test case verifies the behavior of the ByteArrayBuilder when it is
 * initialized with inconsistent parameters.
 */
public class ByteArrayBuilderTest {

    /**
     * Tests that {@link ByteArrayBuilder#toByteArray()} throws an
     * {@link ArrayIndexOutOfBoundsException} if the builder was created via
     * {@link ByteArrayBuilder#fromInitial(byte[], int)} with a length
     * parameter that is greater than the actual length of the provided byte array.
     *
     * The factory method `fromInitial` trusts the provided length, and the error
     * only manifests when the data is later accessed by `toByteArray`.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void toByteArrayShouldThrowExceptionWhenInitialLengthExceedsBufferCapacity() {
        // Arrange: Create an empty buffer but provide an invalid length that is
        // larger than the buffer's actual capacity.
        byte[] emptyBuffer = new byte[0];
        int invalidLength = 100; // Any value > 0 is sufficient to demonstrate the issue.

        // The fromInitial method does not validate the provided length against the array's size upon creation.
        ByteArrayBuilder builder = ByteArrayBuilder.fromInitial(emptyBuffer, invalidLength);

        // Act: Attempt to create the final byte array. This operation tries to copy
        // 'invalidLength' bytes from the 'emptyBuffer', which will cause the exception.
        builder.toByteArray();

        // Assert: The test expects an ArrayIndexOutOfBoundsException, which is
        // declared in the @Test annotation, so no further assertions are needed.
    }
}