package org.jsoup.parser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for the {@link TagSet#get(String, String)} method, focusing on input validation.
 */
@DisplayName("TagSet.get() Input Validation")
class TagSetGetValidationTest {

    private TagSet tagSet;

    @BeforeEach
    void setUp() {
        // Arrange: Create a fresh default HTML TagSet instance before each test.
        tagSet = TagSet.initHtmlDefault();
    }

    @Test
    @DisplayName("should throw IllegalArgumentException for a null tag name")
    void get_withNullTagName_shouldThrowIllegalArgumentException() {
        // Arrange
        final String validNamespace = "html";

        // Act & Assert: Verify that calling get() with a null tagName throws the expected exception.
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> tagSet.get(null, validNamespace)
        );

        // Assert: Check that the exception message is correct.
        assertEquals("Object must not be null", exception.getMessage());
    }

    @Test
    @DisplayName("should throw IllegalArgumentException for a null namespace")
    void get_withNullNamespace_shouldThrowIllegalArgumentException() {
        // Arrange
        final String validTagName = "div";

        // Act & Assert: Verify that calling get() with a null namespace throws the expected exception.
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> tagSet.get(validTagName, null)
        );

        // Assert: Check that the exception message is correct.
        assertEquals("Object must not be null", exception.getMessage());
    }
}