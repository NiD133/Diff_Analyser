package org.apache.commons.collections4.iterators;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;

// Note: The original test class structure and inheritance from a generated
// scaffolding class are preserved. Unused imports have been removed for clarity.
public class ObjectGraphIterator_ESTestTest25 extends ObjectGraphIterator_ESTest_scaffolding {

    /**
     * Tests the constructor that accepts another iterator as its root.
     * <p>
     * This test verifies that an ObjectGraphIterator initialized with another iterator
     * will correctly traverse the elements of that root iterator. It effectively tests
     * the "iterator of iterators" functionality.
     */
    @Test(timeout = 4000)
    public void testConstructorWithRootIteratorTraversesTheRootIterator() {
        // Arrange
        // 1. Create a simple "inner" iterator that will yield a single element.
        //    We use an ObjectGraphIterator for this, with a root object and a null transformer.
        final Integer expectedElement = 4;
        final Iterator<Integer> innerIterator = new ObjectGraphIterator<>(expectedElement, null);

        // 2. Create the "outer" ObjectGraphIterator, passing the inner iterator as its root.
        //    This invokes the constructor under test:
        //    public ObjectGraphIterator(final Iterator<? extends E> rootIterator)
        final ObjectGraphIterator<Object> outerIterator = new ObjectGraphIterator<>(innerIterator);

        // Act & Assert
        // The outer iterator should report that it has an element, which comes from the inner iterator.
        assertTrue("The outer iterator should have a next element.", outerIterator.hasNext());

        // Calling next() on the outer iterator should retrieve the element from the inner one.
        final Object actualElement = outerIterator.next();
        assertEquals("The element from next() should match the one from the inner iterator.",
                     expectedElement, actualElement);

        // After retrieving the only element, the outer iterator should be exhausted.
        assertFalse("The outer iterator should have no more elements.", outerIterator.hasNext());
    }
}