package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Tests for the {@link TagSet} class, focusing on the equals() method contract.
 */
public class TagSetTest {

    @Test
    public void equals_shouldReturnFalse_whenComparedWithDifferentObjectType() {
        // Arrange: Create a TagSet instance and an instance of a different class (Object).
        TagSet tagSet = new TagSet();
        Object nonTagSetObject = new Object();

        // Act: Compare the TagSet instance with the other object.
        boolean areEqual = tagSet.equals(nonTagSetObject);

        // Assert: The result should be false, as they are of incompatible types.
        assertFalse("A TagSet instance should not be equal to an instance of a different class.", areEqual);
    }
}