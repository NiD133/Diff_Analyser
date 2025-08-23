package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * This test case verifies the behavior of the {@link CharSequenceUtils#lastIndexOf(CharSequence, int, int)} method.
 */
public class CharSequenceUtilsTest {

    /**
     * Tests that lastIndexOf returns -1 when the character to be found does not exist in the CharSequence.
     * The search character used is an invalid Unicode code point to ensure robustness.
     */
    @Test
    public void lastIndexOf_withNonExistentCharacter_shouldReturnNotFound() {
        // Arrange
        final CharSequence charSequence = "-1.0";
        final int NON_EXISTENT_CHAR = -778; // Using an invalid code point as the search character.
        final int START_INDEX = 4;
        final int NOT_FOUND = -1;

        // Act
        final int actualIndex = CharSequenceUtils.lastIndexOf(charSequence, NON_EXISTENT_CHAR, START_INDEX);

        // Assert
        assertEquals(NOT_FOUND, actualIndex);
    }
}