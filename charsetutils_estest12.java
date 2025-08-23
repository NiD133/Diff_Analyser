package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit tests for {@link org.apache.commons.lang3.CharSetUtils}.
 */
public class CharSetUtilsTest {

    /**
     * Tests that CharSetUtils.keep() returns an empty string when the input
     * string is empty.
     * <p>
     * This test case verifies the behavior documented in the Javadoc:
     * {@code CharSetUtils.keep("", *) = ""}
     * </p>
     */
    @Test
    public void keep_shouldReturnEmptyString_whenInputStringIsEmpty() {
        // Arrange
        final String input = "";
        // The content of the character set should not matter when the input string is empty.
        // This test uses an array of nulls, similar to the original generated test.
        final String[] characterSet = new String[4];
        final String expected = "";

        // Act
        final String result = CharSetUtils.keep(input, characterSet);

        // Assert
        assertEquals(expected, result);
    }
}