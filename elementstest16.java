package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test suite for the {@link Elements} class.
 */
public class ElementsTest {

    /**
     * Verifies that calling removeClass() on an empty Elements collection is a safe operation
     * that returns the original instance.
     *
     * This test checks a specific edge case: invoking the method with an empty string argument
     * on an empty list. The expected behavior is that the operation does nothing and maintains
     * the fluent API by returning the same object.
     */
    @Test
    public void removeClassWithEmptyStringOnEmptyElementsShouldReturnSameInstance() {
        // Arrange: Create an empty Elements collection.
        Elements elements = new Elements();

        // Act: Attempt to remove an empty class string from the empty collection.
        Elements result = elements.removeClass("");

        // Assert: Verify that the collection remains empty and that the returned object
        // is the same instance, confirming the fluent interface contract.
        assertTrue("The elements collection should remain empty.", elements.isEmpty());
        assertSame("The method should return the same instance for chaining.", elements, result);
    }
}