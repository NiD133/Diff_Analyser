package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.assertSame;

/**
 * Test suite for {@link TagSet}.
 */
public class TagSetTest {

    /**
     * Verifies that a custom, namespaced tag added to a TagSet can be retrieved
     * using the valueOf(tagName, namespace) method. The retrieved tag should be
     * the exact same instance as the one that was added.
     */
    @Test
    public void valueOfRetrievesPreviouslyAddedNamespacedTag() {
        // Arrange: Create a TagSet and a custom namespaced tag.
        TagSet tagSet = TagSet.initHtmlDefault();
        String tagName = "customTag";
        String namespace = "customNs";
        Tag namespacedTag = Tag.valueOf(tagName).namespace(namespace);

        // Act: Add the custom tag to the set, then try to retrieve it.
        tagSet.add(namespacedTag);
        Tag retrievedTag = tagSet.valueOf(tagName, namespace);

        // Assert: The retrieved tag should be the same instance as the one added.
        assertSame("Should retrieve the exact same tag instance that was added",
            namespacedTag, retrievedTag);
    }
}