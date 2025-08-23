package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the LeafNode abstract class, focusing on attribute handling.
 */
public class LeafNodeTest {

    /**
     * Verifies that attempting to retrieve a non-existent attribute from a LeafNode
     * returns an empty string, as LeafNodes do not have attributes by default.
     */
    @Test
    public void attrShouldReturnEmptyStringForNonExistentAttribute() {
        // Arrange: Create a CDataNode, which is a concrete implementation of LeafNode.
        // By default, it has no attributes.
        LeafNode node = new CDataNode("This is some CData content.");
        String nonExistentAttributeKey = "id";

        // Act: Attempt to get the value of an attribute that has not been set.
        String attributeValue = node.attr(nonExistentAttributeKey);

        // Assert: The result should be an empty string, not null, as per Jsoup's API contract.
        assertEquals("", attributeValue);
    }
}