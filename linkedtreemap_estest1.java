package com.google.gson.internal;

import org.junit.Test;
import static org.junit.Assert.*;
// Other imports from the original file may be used by other tests or the scaffolding,
// but for this specific test, only the above are necessary.

public class LinkedTreeMap_ESTestTest1 extends LinkedTreeMap_ESTest_scaffolding {

    /**
     * Tests that the value of a newly created "header" node is null.
     * The Node(boolean) constructor is specifically for creating the list's header,
     * which does not hold a key or a value.
     */
    @Test
    public void getValue_onNewHeaderNode_returnsNull() {
        // Arrange: Create a header node. The generic types are simplified for readability
        // because they do not affect the outcome of this test.
        LinkedTreeMap.Node<String, Object> headerNode = new LinkedTreeMap.Node<>(true);

        // Act: Retrieve the value from the node.
        Object value = headerNode.getValue();

        // Assert: The value of a new header node should always be null.
        assertNull("A newly created header node should have a null value.", value);
    }
}