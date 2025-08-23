package com.google.gson;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test for the static helper methods in {@link FieldNamingPolicy}.
 */
public class FieldNamingPolicyTest {

    @Test
    public void upperCaseFirstLetter_whenFirstLetterIsAlreadyUpperCase_returnsOriginalString() {
        // Arrange
        String input = "AlreadyUpperCase";

        // Act
        String result = FieldNamingPolicy.upperCaseFirstLetter(input);

        // Assert
        assertEquals("The string should remain unchanged", input, result);
    }
}