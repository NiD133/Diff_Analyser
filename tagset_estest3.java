package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.assertSame;

/**
 * Improved test for {@link TagSet}, focusing on its caching behavior.
 */
public class TagSetImprovedTest {

    /**
     * Verifies that calling {@link TagSet#valueOf(String, String, ParseSettings)} multiple times
     * with the same arguments returns the identical {@link Tag} instance. This confirms the
     * caching behavior of the TagSet, ensuring that it doesn't create duplicate objects for
     * the same tag.
     */
    @Test
    public void valueOf_whenTagExists_returnsCachedInstance() {
        // Arrange: Create a new TagSet and define the properties for a custom tag.
        TagSet tagSet = new TagSet();
        String tagName = "my-custom-tag";
        String namespace = "http://www.w3.org/1999/xhtml";
        ParseSettings settings = ParseSettings.preserveCase;

        // Act: Request the same tag from the TagSet twice.
        Tag firstInstance = tagSet.valueOf(tagName, namespace, settings);
        Tag secondInstance = tagSet.valueOf(tagName, namespace, settings);

        // Assert: Verify that both variables point to the exact same object instance.
        assertSame(
            "Expected the same Tag instance to be returned for subsequent calls with the same arguments.",
            firstInstance,
            secondInstance
        );
    }
}