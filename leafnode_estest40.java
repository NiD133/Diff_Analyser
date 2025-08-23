package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.internal.QuietAppendable;

// The original test class name is kept, as the task is to improve the test method.
public class LeafNode_ESTestTest40 extends LeafNode_ESTest_scaffolding {

    /**
     * Verifies that calling outerHtmlTail() with a null Appendable is handled gracefully,
     * without throwing a NullPointerException or causing unintended side effects.
     */
    @Test(timeout = 4000)
    public void outerHtmlTailWithNullAppendableShouldNotThrowException() {
        // Arrange: Create a CDataNode and default output settings.
        CDataNode cdataNode = new CDataNode("Sample CDATA content");
        Document.OutputSettings outputSettings = new Document.OutputSettings();

        // A newly created node without a parent should have a sibling index of 0.
        // We verify this initial state to ensure our assertion later is meaningful.
        int initialSiblingIndex = cdataNode.siblingIndex();
        assertEquals(0, initialSiblingIndex);

        // Act: Call the method under test with a null appendable.
        // The primary purpose of this test is to ensure this call does not throw an exception.
        try {
            cdataNode.outerHtmlTail((QuietAppendable) null, outputSettings);
        } catch (Exception e) {
            fail("Method should not throw any exception for a null appendable, but threw: " + e);
        }

        // Assert: Verify that the node's state (e.g., sibling index) remains unchanged.
        // This confirms the method call had no unexpected side effects.
        assertEquals("The sibling index should not be modified by the call",
            initialSiblingIndex, cdataNode.siblingIndex());
    }
}