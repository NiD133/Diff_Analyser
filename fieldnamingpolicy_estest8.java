package com.google.gson;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Tests for the helper methods in {@link FieldNamingPolicy}.
 */
public class FieldNamingPolicyTest {

    @Test
    public void upperCaseFirstLetter_withEmptyString_returnsEmptyString() {
        // Arrange
        String emptyString = "";

        // Act
        String result = FieldNamingPolicy.upperCaseFirstLetter(emptyString);

        // Assert
        assertEquals("The method should return an empty string when the input is empty.", "", result);
    }
}