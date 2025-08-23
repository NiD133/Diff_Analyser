package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for the {@link Elements#nextAll(String)} method.
 */
public class ElementsNextAllTest {

    /**
     * Verifies that calling nextAll() with a null query on an empty Elements collection
     * returns a new, empty collection rather than the original instance or null.
     */
    @Test
    public void nextAllWithNullQueryOnEmptyElementsShouldReturnNewEmptyList() {
        // Arrange: Create an empty Elements collection.
        Elements emptyElements = new Elements();

        // Act: Call the method under test with a null selector, which is an edge case.
        Elements result = emptyElements.nextAll((String) null);

        // Assert: The method should return a new, empty list, not the original instance.
        // This ensures the method is non-destructive and safe to call even with edge case inputs.
        assertNotNull("The result should not be null.", result);
        assertNotSame("A new Elements instance should be returned.", emptyElements, result);
        assertTrue("The resulting list should be empty.", result.isEmpty());
    }
}