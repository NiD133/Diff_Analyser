package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for {@link CharSequenceUtils}.
 */
public class CharSequenceUtilsTest {

    /**
     * Tests that CharSequenceUtils.subSequence returns an empty CharSequence
     * when the input is an empty CharSequence and the start index is 0.
     */
    @Test
    public void subSequence_withEmptyInput_shouldReturnEmptyCharSequence() {
        // Arrange: Create an empty CharSequence input.
        // Using StringBuilder to test a non-String CharSequence implementation.
        final CharSequence emptyInput = new StringBuilder();

        // Act: Call the method under test.
        final CharSequence result = CharSequenceUtils.subSequence(emptyInput, 0);

        // Assert: The result should be a non-null, empty CharSequence.
        assertNotNull("The result of subSequence should not be null.", result);
        assertEquals("The subsequence of an empty input should be an empty string.",
                     "", result.toString());
    }
}