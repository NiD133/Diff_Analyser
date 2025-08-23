package org.jsoup.select;

import org.junit.Test;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

/**
 * Test suite for the {@link Elements} class.
 */
public class ElementsTest {

    /**
     * Verifies that applying a filter to an empty Elements collection
     * results in the same empty collection and does not interact with the filter.
     */
    @Test
    public void filterOnEmptyElementsReturnsSameEmptyInstance() {
        // Arrange: Create an empty Elements collection and a mock filter.
        Elements emptyElements = new Elements();
        NodeFilter mockFilter = mock(NodeFilter.class);

        // Act: Apply the filter to the empty collection.
        Elements result = emptyElements.filter(mockFilter);

        // Assert: The result should be the same empty collection, and the filter should never be called.
        assertTrue("The filtered collection should remain empty.", result.isEmpty());
        assertSame("The filter method should return the same instance for chaining.", emptyElements, result);
        verifyNoInteractions(mockFilter); // Explicitly verify the filter was never used.
    }
}