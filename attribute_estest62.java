package org.jsoup.nodes;

import org.jsoup.nodes.Range.AttributeRange;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * Tests for the {@link Attribute} class.
 */
public class AttributeTest {

    /**
     * Verifies that an Attribute created programmatically (i.e., not from parsing source HTML)
     * returns the 'untracked' singleton for its source range.
     */
    @Test
    public void sourceRangeReturnsUntrackedForProgrammaticallyCreatedAttribute() {
        // Arrange: Create a new attribute directly.
        String key = "id";
        String value = "product-123";
        Attribute attribute = new Attribute(key, value);

        // Act: Get the source range for the attribute.
        AttributeRange range = attribute.sourceRange();

        // Assert: The range should be the special 'untracked' instance, as it wasn't parsed.
        // Also, confirm the attribute's state for completeness.
        assertSame("Source range should be untracked for a programmatically created attribute.",
            AttributeRange.untracked(), range);
        assertEquals(key, attribute.getKey());
        assertEquals(value, attribute.getValue());
    }
}