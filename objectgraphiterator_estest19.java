package org.apache.commons.collections4.iterators;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link ObjectGraphIterator}.
 */
public class ObjectGraphIteratorTest {

    /**
     * Tests that an ObjectGraphIterator initialized with a single, non-iterator root object
     * and a null transformer will return that root object as its only element.
     *
     * A null transformer should result in a simple iteration over the initial root object.
     */
    @Test
    public void testIteratorWithSingleRootAndNullTransformerReturnsRoot() {
        // Arrange: Define a root object and create an iterator with a null transformer.
        final Integer rootObject = -1265;
        final ObjectGraphIterator<Integer> iterator = new ObjectGraphIterator<>(rootObject, null);

        // Assert: Before iterating, hasNext() should be true.
        assertTrue("Iterator should have an element before the first call to next()", iterator.hasNext());

        // Act: Retrieve the next element from the iterator.
        final Integer result = iterator.next();

        // Assert: The retrieved element should be the original root object,
        // and the iterator should now be exhausted.
        assertEquals("The first element returned should be the root object", rootObject, result);
        assertFalse("Iterator should be exhausted after returning the single root object", iterator.hasNext());
    }
}