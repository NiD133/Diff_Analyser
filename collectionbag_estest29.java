package org.apache.commons.collections4.bag;

import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.SortedBag;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.junit.Test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Test suite for {@link CollectionBag}.
 */
public class CollectionBagTest {

    /**
     * Tests that add() propagates exceptions from the decorated bag chain.
     * Specifically, this test verifies that a NullPointerException is thrown when
     * CollectionBag decorates a TransformedBag that transforms an element to null,
     * which is then passed to an underlying TreeBag that does not permit null elements.
     */
    @Test
    public void addThrowsNullPointerExceptionWhenTransformerProducesNullForUnderlyingBag() {
        // Arrange: Create a chain of decorated bags where an element added to the top
        // is transformed to null before being passed to an underlying bag that disallows nulls.

        // 1. The underlying bag (TreeBag) does not allow null elements.
        final SortedBag<Object> underlyingTreeBag = new TreeBag<>();

        // 2. The transformer converts any added element to null.
        final Transformer<Object, Object> nullTransformer = ConstantTransformer.nullTransformer();
        final SortedBag<Object> transformedBag =
            TransformedSortedBag.transformingSortedBag(underlyingTreeBag, nullTransformer);

        // 3. The CollectionBag under test decorates the transformed bag.
        final Bag<Object> collectionBag = new CollectionBag<>(transformedBag);

        // Act & Assert: Attempting to add any object should result in a NullPointerException
        // because the object is transformed to null, which the underlying TreeBag cannot handle.
        try {
            collectionBag.add("any object");
            fail("Expected a NullPointerException to be thrown");
        } catch (final NullPointerException e) {
            // This is the expected behavior.
            // The exception originates from the underlying TreeMap used by TreeBag,
            // which throws a NPE when trying to add a null key.
            assertNull("The NPE from TreeMap should have no message", e.getMessage());

            // Verify that the exception originates from the expected class (TreeMap)
            // to confirm the cause of the failure.
            boolean foundTreeMapInStackTrace = false;
            for (final StackTraceElement element : e.getStackTrace()) {
                if ("java.util.TreeMap".equals(element.getClassName())) {
                    foundTreeMapInStackTrace = true;
                    break;
                }
            }
            assertTrue("The NullPointerException should originate from the underlying TreeMap",
                       foundTreeMapInStackTrace);
        }
    }
}