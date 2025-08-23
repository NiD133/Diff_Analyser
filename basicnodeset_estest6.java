package org.apache.commons.jxpath;

import org.junit.Test;

/**
 * Unit tests for the {@link BasicNodeSet} class.
 */
public class BasicNodeSetTest {

    @Test(expected = NullPointerException.class)
    public void getNodesShouldThrowNullPointerExceptionWhenSetContainsNullPointer() {
        // Arrange: Create a BasicNodeSet and add a null pointer to it.
        // The getNodes() method is not designed to handle null pointers in its internal list.
        BasicNodeSet nodeSet = new BasicNodeSet();
        nodeSet.add((Pointer) null);

        // Act: Attempt to retrieve the nodes.
        // This action is expected to throw a NullPointerException.
        nodeSet.getNodes();

        // Assert: The test framework implicitly asserts that a NullPointerException was thrown.
        // If no exception or a different one is thrown, the test will fail.
    }
}