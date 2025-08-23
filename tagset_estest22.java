package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Test suite for verifying the contents and behavior of the default HTML {@link TagSet}.
 */
public class TagSetTest {

    /**
     * Verifies that Tag.isKnownTag() returns false for a tag name that is not a standard, known HTML tag.
     * This method indirectly tests the contents of the default HTML TagSet, which is the source of truth
     * for known tags.
     */
    @Test
    public void isKnownTagShouldReturnFalseForUnknownTagName() {
        // Arrange: Define a tag name that does not exist in the standard HTML tag set.
        // The mixed-case name "cxpPE" is an arbitrary, non-standard tag.
        String unknownTagName = "cxpPE";

        // Act: Check if the tag is recognized as a known HTML tag.
        boolean isKnown = Tag.isKnownTag(unknownTagName);

        // Assert: The result should be false, as the tag is not part of the standard set.
        assertFalse("A non-standard tag name should not be identified as a known tag.", isKnown);
    }
}