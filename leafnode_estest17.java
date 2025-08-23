package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the abstract {@link LeafNode} class, using {@link TextNode} as a concrete implementation for testing.
 */
public class LeafNodeTest {

    @Test
    public void coreValueReturnsValueProvidedInConstructor() {
        // Arrange
        String expectedText = "Hello, Jsoup!";
        // We use TextNode as a concrete instance of the abstract LeafNode to test shared functionality.
        TextNode textNode = new TextNode(expectedText);

        // Act
        String actualText = textNode.coreValue();

        // Assert
        assertEquals(expectedText, actualText);
    }
}