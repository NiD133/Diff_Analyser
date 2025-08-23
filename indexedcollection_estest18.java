package org.apache.commons.collections4.collection;

import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.AnyPredicate;
import org.junit.Test;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Contains tests for {@link IndexedCollection}.
 */
public class IndexedCollectionTest {

    /**
     * Tests that an exception thrown by the filter predicate during a call to removeIf
     * is propagated correctly.
     * <p>
     * This test uses an {@link AnyPredicate} constructed with a null array, which is
     * known to throw a NullPointerException when its test method is invoked.
     * </p>
     */
    @Test(expected = NullPointerException.class)
    public void removeIfShouldPropagateNPEFromPredicate() {
        // Arrange
        Collection<String> baseCollection = new LinkedList<>();
        baseCollection.add("some element"); // Add an element to ensure the predicate is called.

        // The transformer is required by the constructor but is irrelevant to this test's logic.
        final Transformer<String, Integer> keyTransformer = String::length;
        IndexedCollection<Integer, String> indexedCollection =
                IndexedCollection.uniqueIndexedCollection(baseCollection, keyTransformer);

        // Create a predicate that will throw a NullPointerException when used.
        final Predicate<String> faultyPredicate = new AnyPredicate<>((Predicate<? super String>[]) null);

        // Act & Assert
        // This call should throw a NullPointerException because the predicate's test() method will be called.
        indexedCollection.removeIf(faultyPredicate);
    }
}