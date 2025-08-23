package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.assertSame;

/**
 * This test class contains tests for the {@link Elements} class.
 * The original test was auto-generated and has been improved for clarity.
 */
public class Elements_ESTestTest128 extends Elements_ESTest_scaffolding {

    /**
     * Verifies that calling the tagName() method on an empty Elements collection
     * returns the same instance. This ensures that method chaining is preserved
     * even when there are no elements to modify.
     */
    @Test
    public void tagNameOnEmptyElementsReturnsSameInstanceForChaining() {
        // Arrange: Create an empty collection of elements.
        Elements emptyElements = new Elements();

        // Act: Attempt to change the tag name of the elements in the empty collection.
        Elements result = emptyElements.tagName("p");

        // Assert: The method should return the original instance to allow for fluent chaining.
        assertSame("Expected the same Elements instance to be returned for chaining.", emptyElements, result);
    }
}