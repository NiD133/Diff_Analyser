package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit tests for {@link org.apache.commons.lang3.CharSequenceUtils}.
 */
public class CharSequenceUtilsTest {

    /**
     * Tests that subSequence() returns the full sequence when the start index is 0.
     * This is equivalent to the behavior of String.substring(0).
     */
    @Test
    public void subSequenceShouldReturnFullSequenceWhenStartIsZero() {
        // Arrange
        final String originalContent = "', is neither of type Map.Entry nor an Array";
        // Use a StringBuilder to test with a non-String CharSequence implementation.
        final CharSequence inputSequence = new StringBuilder(originalContent);

        // Act
        final CharSequence result = CharSequenceUtils.subSequence(inputSequence, 0);

        // Assert
        // The resulting CharSequence should be equal to the original string content.
        assertEquals(originalContent, result.toString());
    }
}