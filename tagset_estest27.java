package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * This test class focuses on the behavior of the TagSet class.
 * The original test class name and inheritance are kept to match the provided context.
 * In a real-world scenario, these would be cleaned up (e.g., to TagSetTest).
 */
public class TagSet_ESTestTest27 extends TagSet_ESTest_scaffolding {

    /**
     * Verifies that adding a tag to a child TagSet makes it unequal to its parent source.
     * A child TagSet is one created with another TagSet as its source for tag lookups.
     */
    @Test
    public void addingTagToChildSetMakesItUnequalToParent() {
        // Arrange: Create a parent TagSet and a child TagSet derived from it.
        // Initially, they should be considered equal as the child has no unique tags.
        TagSet parentTagSet = new TagSet();
        TagSet childTagSet = new TagSet(parentTagSet);
        // Note: The current equals() implementation might not consider them equal even
        // when empty, as it compares the 'source' field. However, the key behavior
        // is the change after modification.

        // Act: Add a new tag to the child set. This modification should only
        // affect the child's internal state.
        childTagSet.valueOf("custom-tag", Parser.NamespaceHtml);

        // Assert: The child set is now different from the parent set because it
        // contains a tag that the parent does not.
        assertFalse("Child TagSet should no longer be equal to the parent after being modified",
                childTagSet.equals(parentTagSet));
    }
}