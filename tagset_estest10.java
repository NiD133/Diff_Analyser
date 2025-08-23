package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test suite for the {@link TagSet} class.
 */
public class TagSetTest {

    /**
     * Verifies that calling {@link TagSet#valueOf(String, String, ParseSettings)} with different
     * case-sensitivity settings results in the creation of two distinct {@link Tag} objects.
     * This is because the internal lookup key in the TagSet depends on whether the tag name
     * is normalized (lowercased) or preserved as-is.
     */
    @Test
    public void valueOfWithDifferentCaseSettingsCreatesDistinctTags() {
        // Arrange
        TagSet tagSet = new TagSet();
        String tagName = "MixedCaseTag";
        String namespace = "custom";

        // Settings that normalize tag names to lowercase (like for HTML).
        ParseSettings caseInsensitiveSettings = ParseSettings.htmlDefault;

        // Act
        // 1. Get a tag with case-insensitive settings. The TagSet will store it
        //    under a lowercase key: "mixedcasetag".
        Tag caseInsensitiveTag = tagSet.valueOf(tagName, namespace, caseInsensitiveSettings);

        // 2. Get a tag with the same name but with default case-sensitive settings.
        //    The TagSet will look for it under the original key: "MixedCaseTag".
        //    Since this key doesn't exist, a new Tag object is created.
        Tag caseSensitiveTag = tagSet.valueOf(tagName, namespace); // Defaults to case-sensitive

        // Assert
        // The two tags should be different instances because they were stored and retrieved
        // using different keys ("mixedcasetag" vs "MixedCaseTag").
        assertNotSame(
            "Two distinct Tag objects should be created due to different case-sensitivity",
            caseInsensitiveTag,
            caseSensitiveTag
        );

        // They should also not be considered equal, as they were created with different settings.
        assertNotEquals(caseInsensitiveTag, caseSensitiveTag);

        // However, both should have the same normalized (lowercase) name.
        assertEquals("mixedcasetag", caseInsensitiveTag.normalName());
        assertEquals("mixedcasetag", caseSensitiveTag.normalName());
    }
}