package org.jsoup.select;

import org.jsoup.nodes.Element;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test suite for the {@link Elements} class.
 */
public class Elements_ESTestTest152 extends Elements_ESTest_scaffolding {

    /**
     * Verifies that calling prev() on an empty Elements collection returns a new, empty collection
     * rather than the original instance. This confirms the method adheres to an immutable-style contract
     * where it doesn't return 'this' or modify the collection in place.
     */
    @Test
    public void prevOnEmptyElementsReturnsNewEmptyInstance() {
        // Arrange: Create an empty Elements collection.
        Elements emptyElements = new Elements();

        // Act: Call the prev() method to get the previous siblings.
        Elements previousSiblings = emptyElements.prev();

        // Assert: The result should be a new, empty Elements instance.
        assertNotNull("The result of prev() should not be null.", previousSiblings);
        assertNotSame("prev() must return a new instance, not the original one.", emptyElements, previousSiblings);
        assertTrue("Calling prev() on an empty collection should result in an empty collection.", previousSiblings.isEmpty());
    }
}