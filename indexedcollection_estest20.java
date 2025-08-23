package org.apache.commons.collections4.collection;

import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Contains tests for {@link IndexedCollection} focusing on exception handling with incompatible types.
 */
public class IndexedCollectionTest {

    /**
     * Tests that calling removeAll() with a collection containing elements of an incompatible
     * type throws a ClassCastException. The method is expected to fail when it internally
     * tries to cast an incompatible object to the collection's generic type.
     */
    @Test(expected = ClassCastException.class)
    public void removeAllShouldThrowClassCastExceptionForIncompatibleCollectionType() {
        // Arrange
        // Create an IndexedCollection designed to hold only Integer objects.
        Transformer<Integer, Integer> integerKeyTransformer = ConstantTransformer.nullTransformer();
        IndexedCollection<Integer, Integer> integerCollection =
                IndexedCollection.nonUniqueIndexedCollection(new ArrayList<>(), integerKeyTransformer);

        // Create a collection containing an element of an incompatible type (a String).
        Collection<Object> incompatibleTypeCollection = Collections.singletonList("not an integer");

        // Act & Assert
        // Attempting to remove a collection of Strings from a collection of Integers
        // should trigger a ClassCastException.
        integerCollection.removeAll(incompatibleTypeCollection);
    }
}