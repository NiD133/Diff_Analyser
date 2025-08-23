package org.apache.commons.collections4.collection;

import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.IfTransformer;
import org.apache.commons.collections4.functors.NOPTransformer;
import org.apache.commons.collections4.functors.NonePredicate;
import org.junit.Test;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Contains test cases for IndexedCollection that involve complex or problematic configurations.
 * This class is separated for clarity from the original generated test suite.
 */
public class IndexedCollection_ESTestTest36 {

    /**
     * Tests that a StackOverflowError is thrown when contains() is called on an IndexedCollection
     * whose key transformer uses a predicate with a circular reference. This is an edge case
     * that tests the collection's behavior with a misconfigured, infinitely recursive transformer.
     */
    @Test(expected = StackOverflowError.class)
    public void testContainsWithCircularTransformerPredicateThrowsStackOverflowError() {
        // Arrange: Create a key transformer that contains a predicate with a circular reference.

        // 1. A predicate array that will hold the circular reference.
        @SuppressWarnings("unchecked")
        final Predicate<Object>[] predicates = new Predicate[1];

        // 2. A predicate that depends on the contents of the array.
        final Predicate<Object> circularPredicate = new NonePredicate<>(predicates);

        // 3. Create the circular reference by placing the predicate into its own dependency array.
        //    Calling circularPredicate.evaluate() will now cause infinite recursion.
        predicates[0] = circularPredicate;

        // 4. A transformer that will execute the problematic predicate.
        final Transformer<Object, Object> keyTransformer = IfTransformer.ifTransformer(
                circularPredicate,
                NOPTransformer.nopTransformer(),  // The transformer to use if true
                NOPTransformer.nopTransformer()); // The transformer to use if false

        // 5. The collection under test, configured with the recursive transformer.
        final Collection<Object> initialCollection = new LinkedList<>();
        final IndexedCollection<Object, Object> indexedCollection =
                IndexedCollection.nonUniqueIndexedCollection(initialCollection, keyTransformer);

        // Act: Call contains(), which triggers the key transformer, leading to a StackOverflowError.
        // The actual object passed to contains() does not matter.
        indexedCollection.contains("any object");

        // Assert: The test expects a StackOverflowError, which is handled by the
        // @Test(expected = ...) annotation.
    }
}