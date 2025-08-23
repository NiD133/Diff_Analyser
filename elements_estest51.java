package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

// Note: The original test class name and inheritance are preserved as per the prompt.
// In a real-world scenario, this class would likely be part of a larger ElementsTest suite.
public class Elements_ESTestTest51 extends Elements_ESTest_scaffolding {

    /**
     * Verifies that calling append() on an empty Elements collection results in no change.
     * The method should be a no-op and return the same instance to allow for method chaining.
     */
    @Test
    public void appendShouldDoNothingWhenElementsCollectionIsEmpty() {
        // Arrange: Create an empty collection of elements.
        Elements emptyElements = new Elements();

        // Act: Attempt to append an HTML string. The content of the string is irrelevant
        // because the method should not perform any action on an empty collection.
        Elements result = emptyElements.append("<p>Some HTML</p>");

        // Assert: The collection must remain empty, and the method should return the
        // original instance.
        assertTrue("Appending to an empty Elements collection should not add any elements.", result.isEmpty());
        assertSame("The method should return the same instance for chaining.", emptyElements, result);
    }
}