package com.google.gson;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Tests for the static helper method {@link FieldNamingPolicy#separateCamelCase(String, char)}.
 */
public class FieldNamingPolicyTest {

    /**
     * Verifies that separateCamelCase correctly handles acronyms (consecutive uppercase letters)
     * by inserting a separator before each capital letter.
     */
    @Test
    public void separateCamelCaseWithAcronym_shouldSeparateEachCapitalLetter() {
        // Arrange
        String camelCaseWithAcronym = "MyHTTPRequest";
        char separator = '_';
        String expectedOutput = "My_H_T_T_P_Request";

        // Act
        String actualOutput = FieldNamingPolicy.separateCamelCase(camelCaseWithAcronym, separator);

        // Assert
        assertEquals(expectedOutput, actualOutput);
    }
}