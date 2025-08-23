package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

/**
 * This test class contains tests for the {@link Elements} class.
 * The original test was automatically generated and has been refactored for clarity.
 */
public class Elements_ESTestTest156 extends Elements_ESTest_scaffolding {

    /**
     * Verifies that calling {@link Elements#prevAll()} on an empty collection
     * returns a new, empty collection, rather than the original instance.
     */
    @Test
    public void prevAllOnEmptyElementsReturnsNewEmptyInstance() {
        // Arrange: Create an empty Elements collection.
        Elements emptyElements = new Elements();

        // Act: Call the method under test to get all previous sibling elements.
        Elements previousSiblings = emptyElements.prevAll();

        // Assert: The method should return a new instance that is also empty.
        assertNotSame("prevAll() should return a new instance, not the same one.", emptyElements, previousSiblings);
        assertTrue("The resulting collection of previous siblings should be empty.", previousSiblings.isEmpty());
    }
}