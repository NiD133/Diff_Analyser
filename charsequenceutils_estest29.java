package org.apache.commons.lang3;

import static org.junit.Assert.assertArrayEquals;
import org.junit.Test;

/**
 * Unit tests for {@link CharSequenceUtils}.
 * This class focuses on testing the {@code toCharArray} method.
 */
public class CharSequenceUtilsTest {

    /**
     * An empty char array constant to use for expected results, improving readability.
     */
    private static final char[] EMPTY_CHAR_ARRAY = new char[0];

    /**
     * Tests that toCharArray correctly converts a standard String into a character array.
     * This is the most common use case.
     */
    @Test
    public void testToCharArray_withString() {
        // Arrange
        final String input = "Apache Commons";
        final char[] expected = {'A', 'p', 'a', 'c', 'h', 'e', ' ', 'C', 'o', 'm', 'm', 'o', 'n', 's'};

        // Act
        final char[] actual = CharSequenceUtils.toCharArray(input);

        // Assert
        assertArrayEquals(expected, actual);
    }

    /**
     * Tests that toCharArray correctly converts a non-String CharSequence, like a StringBuilder.
     */
    @Test
    public void testToCharArray_withStringBuilder() {
        // Arrange
        final CharSequence input = new StringBuilder("Lang");
        final char[] expected = {'L', 'a', 'n', 'g'};

        // Act
        final char[] actual = CharSequenceUtils.toCharArray(input);

        // Assert
        assertArrayEquals(expected, actual);
    }

    /**
     * Tests the null-safe behavior of toCharArray, which should return an empty array
     * for a null input, as documented.
     */
    @Test
    public void testToCharArray_withNullInput() {
        // Arrange
        final CharSequence input = null;

        // Act
        final char[] actual = CharSequenceUtils.toCharArray(input);

        // Assert
        assertArrayEquals(EMPTY_CHAR_ARRAY, actual);
    }

    /**
     * Tests that toCharArray returns an empty array when the input CharSequence is empty.
     */
    @Test
    public void testToCharArray_withEmptyInput() {
        // Arrange
        final CharSequence input = "";

        // Act
        final char[] actual = CharSequenceUtils.toCharArray(input);

        // Assert
        assertArrayEquals(EMPTY_CHAR_ARRAY, actual);
    }
}