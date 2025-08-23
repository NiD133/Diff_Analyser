package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test suite for the {@link TagSet} class, focusing on tag creation and retrieval.
 */
public class TagSetTest {

    /**
     * Verifies that when a tag is created with case-preserving settings, a subsequent
     * call to valueOf() with the same name retrieves the exact same tag instance,
     * with its original case intact.
     */
    @Test
    public void valueOf_retrievesPreviouslyCreatedTag_whenCaseIsPreserved() {
        // Arrange
        TagSet tagSet = new TagSet();
        ParseSettings preserveCaseSettings = ParseSettings.preserveCase;
        String tagName = "MiXeDcAsETaG";
        String namespace = Parser.NamespaceHtml;

        // Act: The first call to valueOf creates a new Tag and adds it to the set.
        Tag createdTag = tagSet.valueOf(tagName, namespace, preserveCaseSettings);

        // The second call with the same arguments should retrieve the existing Tag instance.
        Tag retrievedTag = tagSet.valueOf(tagName, namespace, preserveCaseSettings);

        // Assert
        // 1. Verify that the same object instance is returned, not a new one.
        assertSame("The same tag instance should be retrieved from the set.", createdTag, retrievedTag);

        // 2. Verify that the tag's properties reflect the case-preserving settings.
        assertEquals("The tag name should match the original case.", tagName, retrievedTag.getName());
        assertEquals("The normal name should also preserve the original case.", tagName, retrievedTag.normalName());
        assertFalse("A new custom tag should not be a block format by default.", retrievedTag.formatAsBlock());
    }
}