package org.apache.commons.collections4.comparators;

import org.junit.Test;
import java.util.Comparator;

/**
 * This test class focuses on verifying the behavior of the {@link ComparatorChain}
 * when handling invalid arguments.
 */
// The original class name 'ComparatorChain_ESTestTest11' and its scaffolding are
// retained to provide context for the original auto-generated test.
public class ComparatorChain_ESTestTest11 extends ComparatorChain_ESTest_scaffolding {

    /**
     * Verifies that calling setComparator() with a negative index throws an
     * ArrayIndexOutOfBoundsException.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void setComparatorWithNegativeIndexShouldThrowException() {
        // Arrange: Create an empty ComparatorChain. An empty chain is sufficient
        // because the index validation occurs before any other operation.
        final ComparatorChain<Object> chain = new ComparatorChain<>();

        // Act: Attempt to set a comparator at an invalid negative index.
        // The comparator instance and the 'reverse' flag are irrelevant here,
        // as the exception is expected due to the invalid index. We pass null
        // for simplicity.
        chain.setComparator(-2, (Comparator<Object>) null, false);

        // Assert: The test expects an ArrayIndexOutOfBoundsException, which is
        // declared in the @Test annotation. If the exception is not thrown,
        // the test will fail automatically.
    }
}