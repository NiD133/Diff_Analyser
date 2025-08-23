package org.apache.commons.io;

import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

/**
 * Unit tests for {@link ByteOrderMark}.
 */
public class ByteOrderMarkTest {

    /**
     * Tests that ByteOrderMark.equals() returns false when comparing two
     * different BOM instances.
     */
    @Test
    public void testEqualsWithDifferentBoms() {
        // Arrange: Define two different ByteOrderMark constants.
        final ByteOrderMark utf16le = ByteOrderMark.UTF_16LE;
        final ByteOrderMark utf16be = ByteOrderMark.UTF_16BE;

        // Act & Assert: Verify that the two instances are not equal.
        // The equals() method should be symmetric (a.equals(b) == b.equals(a)),
        // so a single check is sufficient.
        assertNotEquals("UTF_16BE should not be equal to UTF_16LE", utf16be, utf16le);
    }
}