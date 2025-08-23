package com.google.gson;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the static helper methods in {@link FieldNamingPolicy}.
 */
public class FieldNamingPolicyTest {

    /**
     * This test verifies that the {@code upperCaseFirstLetter} method correctly
     * capitalizes the first actual letter in a string, even when the string
     * starts with non-alphabetic characters.
     */
    @Test
    public void upperCaseFirstLetter_withLeadingNonLetters_capitalizesFirstLetterOnly() {
        // Arrange
        String input = "\"1+ejk5p_l;*";
        String expectedOutput = "\"1+Ejk5p_l;*";

        // Act
        String result = FieldNamingPolicy.upperCaseFirstLetter(input);

        // Assert
        assertEquals(expectedOutput, result);
    }
}