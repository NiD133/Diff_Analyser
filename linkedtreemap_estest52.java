package com.google.gson.internal;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This class contains a test for the {@link LinkedTreeMap#removeInternal(LinkedTreeMap.Node, boolean)} method.
 */
public class LinkedTreeMapInternalRemoveTest {

    /**
     * Tests that {@link LinkedTreeMap#removeInternal(LinkedTreeMap.Node, boolean)} throws an
     * {@link AssertionError} when trying to remove a node whose parent-child link is inconsistent.
     *
     * <p>The {@code removeInternal} method relies on the tree's internal consistency. It assumes
     * that if a node has a parent, that parent node must in turn have a reference to the node as
     * either its left or right child.
     *
     * <p>This test manually constructs a node that violates this assumption: the node has a parent
     * reference, but the parent does not have a corresponding child reference. Calling
     * {@code removeInternal} on this node is expected to trigger an internal assertion check and
     * fail.
     */
    @Test
    public void removeInternal_whenNodeIsNotChildOfItsParent_throwsAssertionError() {
        // Arrange: Create a map and a node with an inconsistent parent-child relationship.
        LinkedTreeMap<String, Integer> map = new LinkedTreeMap<>();

        // Create a standalone node to act as a "fake" parent. This node is not part of the map's tree.
        LinkedTreeMap.Node<String, Integer> fakeParent = new LinkedTreeMap.Node<>(true);

        // Create the node to be removed. We manually link it to `fakeParent`.
        // Crucially, `fakeParent` does not have a corresponding `left` or `right` child
        // reference pointing back to this node. This creates the inconsistent state.
        LinkedTreeMap.Node<String, Integer> nodeToRemove =
            new LinkedTreeMap.Node<>(true, fakeParent, "some-key", fakeParent, fakeParent);

        // Act & Assert: Attempting to remove the node with the broken link should throw an AssertionError.
        try {
            map.removeInternal(nodeToRemove, true);
            fail("Expected an AssertionError due to the inconsistent parent-child link.");
        } catch (AssertionError e) {
            // This is the expected outcome.
            // The original test's comment indicated the exception has no message.
            assertNull("The AssertionError should not have a message.", e.getMessage());
        }
    }
}