package org.apache.commons.codec.net;

import org.junit.Test;
import static org.junit.Assert.assertSame;

/**
 * Contains tests for the {@link PercentCodec} class.
 */
public class PercentCodecTest {

    /**
     * Tests that encoding an empty byte array returns the original array instance.
     * This is an important optimization to avoid unnecessary object allocation when
     * the input is empty.
     */
    @Test
    public void encodeEmptyByteArrayShouldReturnSameInstance() {
        // Arrange
        final byte[] emptyArray = new byte[0];
        final PercentCodec percentCodec = new PercentCodec(new byte[0], true);

        // Act
        final byte[] result = percentCodec.encode(emptyArray);

        // Assert
        // The codec should return the original instance for an empty input array.
        assertSame("Encoding an empty array should return the same object instance", emptyArray, result);
    }
}