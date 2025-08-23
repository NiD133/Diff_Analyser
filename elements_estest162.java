package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for the {@link Elements} class.
 */
public class ElementsTest {

    /**
     * Verifies that calling the {@code clear()} method on an already empty
     * Elements collection does not cause an error and results in the
     * collection remaining empty.
     */
    @Test
    public void clearOnEmptyCollectionShouldRemainEmpty() {
        // Arrange: Create an empty Elements collection.
        Elements elements = new Elements();

        // Act: Call the clear() method.
        elements.clear();

        // Assert: The collection should still be empty.
        assertTrue("The Elements collection should be empty after clear() is called.", elements.isEmpty());
    }
}