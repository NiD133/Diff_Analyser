package org.apache.commons.collections4.collection;

import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.apache.commons.collections4.functors.UniquePredicate;
import org.junit.Test;

import java.util.LinkedList;

import static org.junit.Assert.fail;

/**
 * Contains tests for edge cases in {@link IndexedCollection}.
 */
public class IndexedCollectionTest {

    /**
     * Tests that calling removeIf with a predicate that computes hashCode on a self-referential
     * collection results in a StackOverflowError.
     *
     * This scenario occurs because some predicates, like UniquePredicate, internally use a Set,
     * which triggers hashCode() calls on the collection's elements. If an element is the
     * collection itself, this leads to infinite recursion.
     */
    @Test
    public void removeIfWithPredicateOnSelfReferentialCollectionCausesStackOverflow() {
        // Arrange
        // 1. Create a list that contains itself as an element, forming a recursive structure.
        LinkedList<Object> selfReferentialList = new LinkedList<>();
        selfReferentialList.add(selfReferentialList);

        // 2. Create an IndexedCollection that decorates this list.
        // The key transformer is required but not central to this specific test case.
        Transformer<Object, Object> keyTransformer = ConstantTransformer.nullTransformer();
        IndexedCollection<Object, Object> indexedCollection =
                IndexedCollection.nonUniqueIndexedCollection(selfReferentialList, keyTransformer);

        // 3. Use a predicate that will call hashCode() on the list's elements.
        // UniquePredicate uses a HashSet internally, which invokes hashCode().
        UniquePredicate<Object> predicate = new UniquePredicate<>();

        // Act & Assert
        try {
            // This call should trigger infinite recursion when predicate.test() calls
            // hashCode() on the self-referencing list.
            indexedCollection.removeIf(predicate);
            fail("Expected a StackOverflowError due to the predicate operating on a self-referential collection.");
        } catch (StackOverflowError e) {
            // The StackOverflowError is the expected outcome.
        }
    }
}