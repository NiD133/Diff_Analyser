package org.apache.commons.io;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit tests for {@link ByteOrderMark}.
 */
public class ByteOrderMarkTest {

    /**
     * Tests that the length() method returns the correct size for the UTF-8 BOM,
     * which is defined to be 3 bytes.
     */
    @Test
    public void testLengthForUtf8BomIs3() {
        // Arrange
        final ByteOrderMark utf8Bom = ByteOrderMark.UTF_8;

        // Act
        final int length = utf8Bom.length();

        // Assert
        assertEquals(3, length);
    }
}