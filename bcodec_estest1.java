package org.apache.commons.codec.net;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link BCodec}.
 */
public class BCodecTest {

    /**
     * Tests that the getEncoding() method returns the correct encoding scheme identifier, "B",
     * as specified by RFC 1522 for B-encoding.
     */
    @Test
    public void getEncodingShouldReturnB() {
        // Arrange
        final BCodec codec = new BCodec();
        final String expectedEncoding = "B";

        // Act
        final String actualEncoding = codec.getEncoding();

        // Assert
        assertEquals("The encoding identifier should be 'B'", expectedEncoding, actualEncoding);
    }
}