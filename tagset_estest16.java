package org.jsoup.parser;

import org.junit.Test;

/**
 * Tests for the {@link TagSet} class, focusing on input validation.
 */
public class TagSetTest {

    /**
     * Verifies that the valueOf(tagName, namespace) method throws an
     * IllegalArgumentException when the tagName is null.
     */
    @Test(expected = IllegalArgumentException.class)
    public void valueOfWithNullTagNameThrowsIllegalArgumentException() {
        // Arrange: Create a default HTML TagSet.
        TagSet tagSet = TagSet.initHtmlDefault();
        String aValidNamespace = "html";

        // Act: Call the method with a null tagName, which is expected to throw.
        tagSet.valueOf(null, aValidNamespace);

        // Assert: The test passes if an IllegalArgumentException is thrown,
        // which is handled by the @Test(expected=...) annotation.
    }
}