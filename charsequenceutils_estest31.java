package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

/**
 * Tests for {@link CharSequenceUtils}.
 */
public class CharSequenceUtilsTest {

    @Test
    public void toCharArray_shouldConvertStringBuilderToCharArray() {
        // Arrange
        String originalString = "This is a test for StringBuilder";
        StringBuilder inputSequence = new StringBuilder(originalString);
        char[] expectedArray = originalString.toCharArray();

        // Act
        char[] actualArray = CharSequenceUtils.toCharArray(inputSequence);

        // Assert
        assertArrayEquals(expectedArray, actualArray);
    }
}