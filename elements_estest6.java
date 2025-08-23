package org.jsoup.select;

import org.jsoup.select.NodeVisitor;
import org.junit.Test;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

/**
 * Test suite for the {@link Elements} class.
 * This specific test focuses on the traverse() method.
 */
public class Elements_ESTestTest6 { // Original class name kept for context

    /**
     * Verifies that calling traverse() on an empty Elements collection
     * does not interact with the visitor and returns the same empty collection instance.
     */
    @Test
    public void traverseOnEmptyElementsShouldDoNothingAndReturnSameInstance() {
        // Arrange: Create an empty Elements collection and a mock NodeVisitor.
        Elements emptyElements = new Elements();
        NodeVisitor mockVisitor = mock(NodeVisitor.class);

        // Act: Call the traverse method on the empty collection.
        Elements resultElements = emptyElements.traverse(mockVisitor);

        // Assert: Verify the behavior is correct.
        // 1. The method should return the same instance to allow for method chaining.
        assertSame("traverse() should return the same instance for chaining.", emptyElements, resultElements);
        
        // 2. The collection should remain empty.
        assertTrue("The Elements collection should remain empty.", resultElements.isEmpty());

        // 3. The visitor's methods should never be called, as there are no nodes to visit.
        verifyNoInteractions(mockVisitor);
    }
}