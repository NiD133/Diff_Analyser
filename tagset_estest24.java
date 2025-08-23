package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.assertNull;

/**
 * Tests for the {@link TagSet} class, focusing on tag retrieval.
 */
public class TagSetTest {

    /**
     * Verifies that TagSet.get() returns null when asked for a tag that does not exist
     * in the set.
     */
    @Test
    public void getReturnsNullForNonExistentTag() {
        // Arrange: Get the default HTML TagSet and define a tag name and namespace
        // that are not expected to be present.
        TagSet htmlTagSet = TagSet.Html();
        String unknownTagName = "nonexistent-tag";
        String customNamespace = "http://my.custom.namespace/";

        // Act: Attempt to retrieve the non-existent tag from the set.
        Tag foundTag = htmlTagSet.get(unknownTagName, customNamespace);

        // Assert: The result should be null, confirming the tag was not found.
        assertNull("Expected get() to return null for a non-existent tag.", foundTag);
    }
}