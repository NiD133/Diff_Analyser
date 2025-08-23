package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link RandomAccessFileMode} enum.
 */
public class RandomAccessFileModeTest {

    /**
     * Tests that {@link RandomAccessFileMode#valueOf(String)} returns the correct enum constant
     * for a valid mode name.
     */
    @Test
    public void testValueOfReturnsCorrectEnumForValidName() {
        // Arrange
        final String modeName = "READ_WRITE";
        final RandomAccessFileMode expectedMode = RandomAccessFileMode.READ_WRITE;

        // Act
        final RandomAccessFileMode actualMode = RandomAccessFileMode.valueOf(modeName);

        // Assert
        assertEquals(expectedMode, actualMode);
    }
}