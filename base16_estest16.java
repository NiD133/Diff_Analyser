package org.apache.commons.codec.binary;

import org.apache.commons.codec.binary.BaseNCodec.Context;
import org.junit.Test;

/**
 * Unit tests for the {@link Base16} class.
 */
public class Base16Test {

    /**
     * Tests that the decode method throws a NullPointerException when the input byte array is null.
     * This is the expected behavior as per the method's contract.
     */
    @Test(expected = NullPointerException.class)
    public void decodeWithNullArrayShouldThrowNullPointerException() {
        // Arrange: Create a Base16 instance and a context for the operation.
        final Base16 base16 = new Base16();
        final Context context = new Context();
        
        // The offset and length values are arbitrary since the null check happens first.
        final int offset = 0;
        final int length = 10;

        // Act: Call the decode method with a null input array.
        base16.decode(null, offset, length, context);

        // Assert: The test will pass only if a NullPointerException is thrown,
        // which is handled by the 'expected' attribute of the @Test annotation.
    }
}