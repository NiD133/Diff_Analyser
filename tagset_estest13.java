package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for the {@link TagSet} class.
 */
public class TagSetTest {

    /**
     * Verifies that the valueOf method throws an IllegalArgumentException
     * when the provided tag name is an empty string.
     */
    @Test
    public void valueOfShouldThrowIllegalArgumentExceptionForEmptyTagName() {
        // Arrange
        TagSet htmlTagSet = TagSet.HtmlTagSet;
        ParseSettings settings = ParseSettings.preserveCase;
        String emptyTagName = "";
        String namespace = ""; // The namespace can also be empty for this test case

        // Act & Assert
        try {
            htmlTagSet.valueOf(emptyTagName, namespace, settings);
            fail("Expected an IllegalArgumentException to be thrown for an empty tag name, but it was not.");
        } catch (IllegalArgumentException e) {
            // Verify that the exception message is correct, confirming the validation logic.
            assertEquals("String must not be empty", e.getMessage());
        }
    }
}