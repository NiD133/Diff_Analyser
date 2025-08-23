package org.apache.commons.io;

import org.junit.Test;

/**
 * Tests for the {@link RandomAccessFileMode#create(String)} method.
 */
public class RandomAccessFileModeTest {

    /**
     * Tests that {@link RandomAccessFileMode#create(String)} throws a NullPointerException
     * when the provided file path is null. The specific mode used (e.g., READ_ONLY)
     * should not affect this behavior.
     */
    @Test(expected = NullPointerException.class)
    public void createWithStringShouldThrowNullPointerExceptionForNullPath() {
        // Arrange
        final RandomAccessFileMode mode = RandomAccessFileMode.READ_ONLY;

        // Act: Call the method under test with a null argument.
        // Assert: The @Test(expected) annotation asserts that a NullPointerException is thrown.
        mode.create((String) null);
    }
}