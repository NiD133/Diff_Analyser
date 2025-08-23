package org.apache.commons.io;

import static org.junit.Assert.assertFalse;
import org.junit.Test;

/**
 * Contains tests for the {@link ByteOrderMark#equals(Object)} method.
 */
public class ByteOrderMarkTest {

    /**
     * Tests that the equals() method correctly returns false when comparing two
     * different ByteOrderMark instances.
     */
    @Test
    public void testEqualsIsFalseForDifferentBoms() {
        // Arrange: Define two different ByteOrderMark constants.
        final ByteOrderMark utf32le = ByteOrderMark.UTF_32LE;
        final ByteOrderMark utf32be = ByteOrderMark.UTF_32BE;

        // Act & Assert: Verify that the instances are not equal to each other.
        
        // Test for inequality
        assertFalse("UTF-32LE should not be equal to UTF-32BE", utf32le.equals(utf32be));

        // Test for symmetry (b.equals(a) should also be false)
        assertFalse("Symmetry check: UTF-32BE should not be equal to UTF-32LE", utf32be.equals(utf32le));
    }
}