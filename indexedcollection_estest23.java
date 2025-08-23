package org.apache.commons.collections4.collection;

import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.apache.commons.collections4.functors.SwitchTransformer;
import org.apache.commons.collections4.functors.TruePredicate;
import org.apache.commons.collections4.functors.UniquePredicate;
import org.junit.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

import static org.junit.Assert.fail;

/**
 * This class contains a test for the IndexedCollection class.
 * The original test was auto-generated and has been rewritten for clarity.
 */
public class IndexedCollectionRefactoredTest {

    /**
     * Tests that {@link IndexedCollection#removeAll(Collection)} throws an exception
     * if the provided key transformer is stateful and behaves inconsistently across calls.
     * <p>
     * This test configures a {@link SwitchTransformer} that successfully generates a key
     * when an element is first added. However, when {@code removeAll} is called for the
     * same element, the stateful nature of the predicates causes the transformer to select
     * an index that is out of bounds for its internal transformers array, leading to an
     * {@link ArrayIndexOutOfBoundsException}. This simulates a scenario where a key
     * transformer is not a pure function, which can break the internal state of the
     * IndexedCollection.
     */
    @Test
    public void removeAllShouldThrowExceptionWhenKeyTransformerIsUnstable() {
        // Arrange: Create a stateful key transformer that behaves differently on subsequent calls.

        // A UniquePredicate is stateful: it returns true the first time it sees an object,
        // and false for all subsequent calls with the same object.
        Predicate<String> firstCallPredicate = new UniquePredicate<>();
        Predicate<String> subsequentCallPredicate = TruePredicate.truePredicate();

        Predicate<? super String>[] predicates = new Predicate[]{
            firstCallPredicate,      // This will be true on the first call (during add).
            subsequentCallPredicate  // This will be true on the second call (during removeAll).
        };

        // A transformer to be used on the first successful call.
        Transformer<String, Integer> validTransformer = ConstantTransformer.constantTransformer(100);

        // The array of transformers is intentionally made too short. It has one element,
        // corresponding to the firstCallPredicate. There is no transformer at index 1
        // for the subsequentCallPredicate.
        Transformer<? super String, ?>[] transformers = new Transformer[]{validTransformer};

        // The SwitchTransformer will use the first predicate that returns true to select a transformer.
        // On add("element"): firstCallPredicate is true -> uses transformers[0].
        // On removeAll("element"): firstCallPredicate is false, subsequentCallPredicate is true -> tries to use transformers[1].
        Transformer<String, Integer> unstableKeyTransformer = new SwitchTransformer<>(predicates, transformers, null);

        Collection<String> backingCollection = new LinkedList<>();
        IndexedCollection<Integer, String> indexedCollection =
                IndexedCollection.nonUniqueIndexedCollection(backingCollection, unstableKeyTransformer);

        // Add an element. This works fine as it triggers the first predicate, which has a
        // corresponding transformer at index 0.
        final String element = "test_element";
        indexedCollection.add(element);

        final Collection<String> elementsToRemove = Collections.singletonList(element);

        // Act & Assert: Expect an exception when removeAll is called.
        try {
            indexedCollection.removeAll(elementsToRemove);
            fail("Expected an ArrayIndexOutOfBoundsException due to the unstable key transformer.");
        } catch (final ArrayIndexOutOfBoundsException e) {
            // This is the expected outcome. The SwitchTransformer attempted to access an
            // out-of-bounds index because the state of its predicates changed between the
            // add and removeAll calls.
        }
    }
}