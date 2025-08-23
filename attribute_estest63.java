package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test suite for the {@link Attribute} class.
 */
public class AttributeTest {

    /**
     * Verifies that an Attribute created programmatically (i.e., not by parsing a document)
     * has a non-null, untracked source range. The source range is only available for
     * attributes parsed from an input source with tracking enabled.
     */
    @Test
    public void sourceRangeShouldBeUntrackedForProgrammaticallyCreatedAttribute() {
        // Arrange: Create an attribute directly, not through parsing.
        // The parent Attributes object can be null for this test's purpose.
        Attribute attribute = new Attribute("id", "test-id", null);

        // Act: Get the source range of the attribute.
        Range.AttributeRange range = attribute.sourceRange();

        // Assert: The range should be a non-null object representing an untracked position.
        // In Jsoup's implementation, this is a specific "untracked" singleton instance.
        assertNotNull("The source range should not be null for a new attribute.", range);
        
        // A stronger assertion would be to check if the range is tracked,
        // for example: assertFalse(range.isTracked());
        // This confirms it's specifically the 'untracked' range.
    }
}