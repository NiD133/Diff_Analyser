package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the {@link TagSet} class, focusing on adding and retrieving tags.
 */
public class TagSetTest {

    /**
     * Verifies that a custom tag, after being created and added to a TagSet,
     * can be retrieved successfully. It also confirms that the retrieved tag
     * is the same instance and that its "normal name" is correctly lower-cased,
     * while its original name is preserved.
     */
    @Test
    public void addAndGetCustomTag() {
        // Arrange
        TagSet tagSet = new TagSet();
        String mixedCaseTagName = "CustomTag";
        String namespace = "http://example.com/ns";

        // Use preserveCase settings to ensure the tag's original name is stored as-is.
        // This helps confirm that normalName() is independently lower-cased.
        ParseSettings preserveCaseSettings = ParseSettings.preserveCase;

        // Act
        // 1. Create a new tag instance using valueOf().
        Tag tagToAdd = tagSet.valueOf(mixedCaseTagName, namespace, preserveCaseSettings);
        
        // 2. Explicitly add the tag to the set.
        tagSet.add(tagToAdd);
        
        // 3. Retrieve the tag using its original, case-sensitive name.
        Tag retrievedTag = tagSet.get(mixedCaseTagName, namespace);

        // Assert
        assertNotNull("The retrieved tag should not be null.", retrievedTag);
        assertSame("Should retrieve the exact same tag instance that was added.", tagToAdd, retrievedTag);
        
        // Verify the properties of the retrieved tag
        assertEquals("The original tag name should be preserved.", "CustomTag", retrievedTag.getName());
        assertEquals("The normal name should be the lower-cased version of the tag name.", "customtag", retrievedTag.normalName());
        assertFalse("A new custom tag should not be a block-level element by default.", retrievedTag.formatAsBlock());
    }
}