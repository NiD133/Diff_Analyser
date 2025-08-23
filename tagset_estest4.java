package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test suite for {@link TagSet}.
 */
public class TagSetTest {

    /**
     * Tests that {@link TagSet#valueOf(String, String)} retrieves a pre-existing tag
     * that has a namespace prefix in its name, rather than creating a new one.
     */
    @Test
    public void valueOfRetrievesExistingTagWithNamespacePrefix() {
        // Arrange
        TagSet tagSet = TagSet.Html();
        String tagNameWithPrefix = "custom:tag";
        String namespace = "test-namespace";

        // Create a custom tag with a prefixed name and add it to the set
        Tag customTag = new Tag(tagNameWithPrefix, namespace);
        tagSet.add(customTag);

        // Act
        // Attempt to retrieve the same tag using valueOf
        Tag retrievedTag = tagSet.valueOf(tagNameWithPrefix, namespace);

        // Assert
        // 1. The exact same instance should be returned, confirming it was retrieved not created.
        assertSame("Should retrieve the exact same tag instance that was added.", customTag, retrievedTag);

        // 2. Verify that the prefix is correctly identified on the retrieved tag.
        assertEquals("The prefix of the tag name should be correctly parsed.", "custom", retrievedTag.prefix());
    }
}