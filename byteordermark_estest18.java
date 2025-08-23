package org.apache.commons.io;

import static org.junit.Assert.assertNotEquals;
import org.junit.Test;

/**
 * Tests for the equals() method in {@link ByteOrderMark}.
 */
public class ByteOrderMarkTest {

    /**
     * Tests that the equals() method returns false when comparing two ByteOrderMark
     * instances that have different byte sequences.
     */
    @Test
    public void testEqualsReturnsFalseForDifferentBoms() {
        // Arrange: Create two distinct ByteOrderMark instances.
        // The predefined UTF-32LE BOM has bytes {0xFF, 0xFE, 0x00, 0x00}.
        final ByteOrderMark bom1 = ByteOrderMark.UTF_32LE;

        // Create a custom BOM with a completely different byte sequence.
        final ByteOrderMark bom2 = new ByteOrderMark("CUSTOM_BOM", 10, 20, 30);

        // Act & Assert: The two BOMs should not be equal because their underlying
        // byte arrays and charset names are different.
        assertNotEquals(bom1, bom2);
    }
}