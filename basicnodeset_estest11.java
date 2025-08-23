package org.apache.commons.jxpath;

import org.junit.Test;
import java.util.List;
import static org.junit.Assert.assertSame;

/**
 * Unit tests for the {@link BasicNodeSet} class.
 */
public class BasicNodeSetTest {

    /**
     * Tests that getPointers() returns a cached, identical list instance on subsequent calls.
     * <p>
     * The BasicNodeSet is expected to create an unmodifiable view of its internal pointers
     * list on the first call to getPointers() and return that same cached instance on all
     * subsequent calls until the underlying set is modified.
     */
    @Test
    public void getPointersShouldReturnSameInstanceOnSubsequentCalls() {
        // Arrange: Create a new BasicNodeSet.
        BasicNodeSet nodeSet = new BasicNodeSet();

        // Act: Call getPointers() twice to retrieve the list of pointers.
        List<Pointer> firstCallResult = nodeSet.getPointers();
        List<Pointer> secondCallResult = nodeSet.getPointers();

        // Assert: Verify that both calls returned the exact same list instance,
        // confirming the caching behavior of the method.
        assertSame("Expected subsequent calls to getPointers() to return the same cached list instance",
                firstCallResult, secondCallResult);
    }
}