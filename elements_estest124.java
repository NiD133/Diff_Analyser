package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for the {@link Elements} class.
 */
public class ElementsTest {

    /**
     * Verifies that calling the {@code before(html)} method on an empty Elements
     * collection has no effect and returns the same instance.
     */
    @Test
    public void beforeShouldHaveNoEffectWhenCalledOnEmptyElements() {
        // Arrange: Create an empty Elements collection.
        Elements emptyElements = new Elements();
        String htmlToInsert = "<div>This HTML should not be inserted</div>";

        // Act: Attempt to insert HTML before the elements in the empty collection.
        Elements result = emptyElements.before(htmlToInsert);

        // Assert: The collection should remain empty, and the method should return the
        // original instance to allow for method chaining.
        assertTrue("The Elements collection should remain empty.", result.isEmpty());
        assertSame("The method should return the same instance for chaining.", emptyElements, result);
    }
}