package org.apache.commons.io;

import static org.junit.Assert.assertFalse;
import org.junit.Test;

/**
 * Tests for the {@link RandomAccessFileMode#implies(RandomAccessFileMode)} method.
 */
public class RandomAccessFileModeTest {

    /**
     * Tests that a mode with a lower access level does not imply a mode with a
     * higher access level.
     *
     * The access hierarchy is:
     * READ_ONLY < READ_WRITE < READ_WRITE_SYNC_CONTENT < READ_WRITE_SYNC_ALL
     */
    @Test
    public void testImpliesReturnsFalseWhenLowerAccessModeChecksAgainstHigherAccessMode() {
        // Arrange
        // READ_WRITE_SYNC_CONTENT has a lower access level than READ_WRITE_SYNC_ALL.
        RandomAccessFileMode lowerAccessMode = RandomAccessFileMode.READ_WRITE_SYNC_CONTENT;
        RandomAccessFileMode higherAccessMode = RandomAccessFileMode.READ_WRITE_SYNC_ALL;

        // Act
        // Check if the lower access mode (SYNC_CONTENT) implies the higher one (SYNC_ALL).
        boolean result = lowerAccessMode.implies(higherAccessMode);

        // Assert
        assertFalse("A mode with fewer permissions should not imply a mode with more permissions.", result);
    }
}