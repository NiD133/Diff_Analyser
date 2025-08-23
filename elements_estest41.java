package org.jsoup.select;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.junit.Test;

import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for the {@link Elements#filter(NodeFilter)} method.
 */
public class ElementsFilterTest {

    /**
     * Verifies that the filter() method is robust against a NodeFilter
     * that returns null, which is not a valid FilterResult enum value.
     * <p>
     * This tests a defensive edge case. The method should complete without error
     * and return the original Elements instance for method chaining.
     */
    @Test
    public void filterShouldReturnSameInstanceWhenFilterCallbackReturnsNull() {
        // Arrange
        Elements elements = new Elements(new Element("p"));

        // Create a mock NodeFilter that returns null from its 'head' method.
        // This simulates an unconventional or faulty filter implementation.
        NodeFilter mockFilter = mock(NodeFilter.class);
        when(mockFilter.head(any(Node.class), anyInt())).thenReturn(null);

        // Act
        Elements filteredElements = elements.filter(mockFilter);

        // Assert
        // The filter method should be a no-op and return the same instance.
        assertSame("Expected the filter method to return the same instance", elements, filteredElements);
    }
}