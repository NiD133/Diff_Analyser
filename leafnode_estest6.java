package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for the LeafNode abstract class.
 */
public class LeafNodeTest {

    /**
     * Verifies that a LeafNode reports having attributes after an attribute is set,
     * even when the attribute's value is null. This ensures the internal
     * attributes map is correctly initialized.
     */
    @Test
    public void hasAttributesIsTrueAfterSettingAttributeWithNullValue() {
        // Arrange: Create a concrete instance of LeafNode. CDataNode is a suitable choice.
        CDataNode leafNode = new CDataNode("Initial text content");
        String attributeKey = "data-is-present";

        // Act: Set an attribute with a null value. This is the core action under test.
        leafNode.attr(attributeKey, null);

        // Assert: Verify that the node now reports having attributes.
        boolean hasAttributes = leafNode.hasAttributes();
        assertTrue("A LeafNode should report having attributes after one is set, even with a null value.", hasAttributes);
    }
}