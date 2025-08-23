package org.apache.commons.lang3;

import static org.junit.Assert.assertArrayEquals;
import org.junit.Test;

/**
 * Unit tests for {@link org.apache.commons.lang3.CharSequenceUtils}.
 */
public class CharSequenceUtilsTest {

    @Test
    public void testToCharArray_withEmptyCharSequence_returnsEmptyArray() {
        // Arrange
        // An empty StringBuilder is used as a representative empty CharSequence.
        final CharSequence emptyInput = new StringBuilder("");
        final char[] expectedArray = {};

        // Act
        final char[] resultArray = CharSequenceUtils.toCharArray(emptyInput);

        // Assert
        assertArrayEquals("toCharArray should return an empty array for an empty CharSequence",
                expectedArray, resultArray);
    }
}