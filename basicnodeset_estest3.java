package org.apache.commons.jxpath;

import org.junit.Test;
import static org.junit.Assert.assertThrows;

/**
 * Contains tests for the {@link BasicNodeSet} class.
 */
// Note: The original class name 'BasicNodeSet_ESTestTest3' and its scaffolding
// parent are kept for context. A more conventional name would be 'BasicNodeSetTest'.
public class BasicNodeSet_ESTestTest3 extends BasicNodeSet_ESTest_scaffolding {

    /**
     * Verifies that getValues() throws a NullPointerException if a null Pointer
     * has been added to the set. This occurs because the method attempts to
     * dereference the null pointer to extract its value.
     */
    @Test(timeout = 4000)
    public void getValuesShouldThrowNPEWhenSetContainsNullPointer() {
        // Arrange: Create a node set and add a null Pointer.
        BasicNodeSet nodeSet = new BasicNodeSet();
        nodeSet.add((Pointer) null);

        // Act & Assert: Expect a NullPointerException when getValues() is called.
        assertThrows(NullPointerException.class, () -> {
            nodeSet.getValues();
        });
    }
}