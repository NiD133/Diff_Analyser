package org.jsoup.parser;

import org.junit.Test;

/**
 * Tests for the {@link TagSet} class, focusing on exception handling.
 */
public class TagSetTest {

    /**
     * Verifies that calling valueOf() with a null ParseSettings object
     * throws a NullPointerException, as the settings are required to
     * determine tag name case sensitivity.
     */
    @Test(expected = NullPointerException.class)
    public void valueOfWithNullParseSettingsThrowsNullPointerException() {
        // Arrange
        TagSet tagSet = new TagSet();
        String tagName = "div";
        String namespace = Parser.NamespaceHtml;

        // Act & Assert
        // This call is expected to throw a NullPointerException because the settings argument is null.
        tagSet.valueOf(tagName, namespace, null);
    }
}