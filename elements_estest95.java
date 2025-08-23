package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Test suite for the {@link Elements#remove(Object)} method.
 */
public class ElementsTest {

    /**
     * Verifies that calling remove(Object) on an empty Elements collection
     * returns false, as the collection remains unchanged.
     */
    @Test
    public void removeObjectFromEmptyListReturnsFalse() {
        // Arrange: Create an empty Elements collection.
        Elements elements = new Elements();
        
        // An object that is guaranteed not to be in the collection.
        // The original test used the collection itself, which is a valid choice.
        Object objectToRemove = new Object();

        // Act: Attempt to remove the object from the empty collection.
        boolean wasRemoved = elements.remove(objectToRemove);

        // Assert: The method should return false, indicating the collection was not modified.
        assertFalse("Removing an object from an empty list should return false.", wasRemoved);
    }
}