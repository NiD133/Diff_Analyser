package com.google.gson;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the static helper methods in {@link FieldNamingPolicy}.
 */
public class FieldNamingPolicyTest {

    /**
     * Verifies that {@code upperCaseFirstLetter} correctly capitalizes the first letter
     * of a given string, while leaving the remainder of the string unchanged.
     */
    @Test
    public void upperCaseFirstLetter_withLowerCaseStart_shouldCapitalizeFirstLetterOnly() {
        // Arrange
        String inputString = "someFieldNameInCamelCase";
        String expectedString = "SomeFieldNameInCamelCase";

        // Act
        String actualString = FieldNamingPolicy.upperCaseFirstLetter(inputString);

        // Assert
        assertEquals(expectedString, actualString);
    }
}