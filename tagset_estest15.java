package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for the {@link TagSet} class.
 */
public class TagSetTest {

    /**
     * Verifies that the internal valueOf method throws an IllegalArgumentException
     * when the provided tag name is an empty string, as this is an invalid state.
     */
    @Test
    public void valueOfThrowsIllegalArgumentExceptionForEmptyTagName() {
        // Arrange
        TagSet tagSet = TagSet.initHtmlDefault();
        String emptyTagName = "";
        String anyNormalName = "p";
        String anyNamespace = Parser.NamespaceHtml;
        boolean preserveCase = true;

        // Act & Assert
        try {
            // This is a private-access method called via a public one, but the test targets its validation.
            tagSet.valueOf(emptyTagName, anyNormalName, anyNamespace, preserveCase);
            fail("Expected an IllegalArgumentException to be thrown for an empty tag name.");
        } catch (IllegalArgumentException e) {
            // Verify that the exception message is correct, confirming the validation source.
            assertEquals("String must not be empty", e.getMessage());
        }
    }
}