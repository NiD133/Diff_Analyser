package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test suite for the {@link TagSet} class.
 */
public class TagSetTest {

    /**
     * Verifies that a custom tag, once created and added to a TagSet, can be successfully retrieved.
     * This test confirms that the 'add' and 'get' methods work together as expected.
     */
    @Test
    public void addAndGet_whenTagIsAdded_retrievesSameInstance() {
        // Arrange
        TagSet tagSet = TagSet.Html(); // Start with a standard HTML tag set
        String tagName = "custom-tag";
        String namespace = "custom-ns";

        // Create a new tag instance. Note that tagSet.valueOf() also adds the tag to the set.
        // We explicitly call add() afterward to ensure it correctly handles pre-existing tags.
        Tag customTag = tagSet.valueOf(tagName, namespace, ParseSettings.preserveCase);

        // Act
        tagSet.add(customTag); // Explicitly add the tag to the set
        Tag retrievedTag = tagSet.get(tagName, namespace); // Attempt to retrieve it by name and namespace

        // Assert
        assertNotNull("The retrieved tag should not be null.", retrievedTag);
        assertSame("The retrieved tag should be the exact same instance that was added.", customTag, retrievedTag);
    }
}