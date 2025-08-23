package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test suite for the {@link TagSet} class.
 */
public class TagSetTest {

    @Test
    public void getShouldRetrieveTagCreatedByValueOf() {
        // Arrange
        TagSet tagSet = TagSet.Html();
        String tagName = "MyCustomTag"; // A mixed-case name to test case preservation
        String normalName = "mycustomtag"; // A pre-normalized (lowercase) name
        String namespace = "customNS";
        boolean preserveCase = true;

        // Act: Create a new tag via valueOf and then retrieve it using get.
        // The valueOf method adds the tag to the internal set.
        tagSet.valueOf(tagName, normalName, namespace, preserveCase);
        Tag retrievedTag = tagSet.get(tagName, namespace);

        // Assert
        assertNotNull("The tag should have been created and be retrievable.", retrievedTag);
        assertEquals("The retrieved tag should have the pre-supplied normal name.",
                     normalName, retrievedTag.normalName());
        assertEquals("The original case-sensitive tag name should be preserved.",
                     tagName, retrievedTag.getName());
    }
}