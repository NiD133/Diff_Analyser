package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the {@link TagSet} class.
 */
public class TagSetTest {

    /**
     * Verifies that when a tag is requested multiple times from a TagSet, the same
     * cached instance is returned, and any modifications to that instance persist.
     */
    @Test
    public void valueOf_forExistingTag_returnsCachedInstanceWithModifications() {
        // Arrange
        TagSet tagSet = new TagSet();
        String tagName = "custom-tag";
        String namespace = "custom-ns";
        ParseSettings settings = ParseSettings.preserveCase;

        // Act:
        // 1. Get a tag for the first time. This creates and caches it in the TagSet.
        Tag initialTag = tagSet.valueOf(tagName, namespace);

        // 2. Modify the state of the cached tag instance.
        // Note: set() is a package-private method. This test can call it because it resides
        // in the same package. We set the PreserveWhitespace option to test if the
        // modification persists on subsequent retrievals.
        initialTag.set(Tag.PreserveWhitespace);

        // 3. Request the same tag again using a different `valueOf` overload.
        Tag retrievedTag = tagSet.valueOf(tagName, namespace, settings);

        // Assert
        // Verify that the TagSet returned the exact same instance, not a copy.
        assertSame(
            "The TagSet should return the cached instance of the Tag.",
            initialTag,
            retrievedTag);

        // Verify that the modification made to the initial instance is reflected.
        assertTrue(
            "The retrieved tag should reflect the 'preserveWhitespace' modification.",
            retrievedTag.preserveWhitespace());
    }
}