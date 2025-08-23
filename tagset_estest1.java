package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the {@link TagSet} class.
 */
public class TagSetTest {

    @Test
    public void newTagSetCreatedFromSourceIsEqualToSource() {
        // Arrange: Create the original source TagSet from the HTML defaults.
        TagSet sourceTagSet = TagSet.Html();

        // Act: Create a new TagSet using the original as its source.
        TagSet derivedTagSet = new TagSet(sourceTagSet);

        // Assert: The new TagSet should be considered equal to its source.
        // Using assertEquals is more idiomatic and provides a better failure message.
        assertEquals(sourceTagSet, derivedTagSet);

        // For completeness, also verify the original test's assertion.
        assertTrue(derivedTagSet.equals(sourceTagSet));
    }
}