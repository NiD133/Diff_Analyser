package org.apache.commons.collections4.collection;

import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.apache.commons.collections4.functors.ExceptionTransformer;
import org.apache.commons.collections4.functors.NotNullPredicate;
import org.apache.commons.collections4.functors.NullPredicate;
import org.apache.commons.collections4.functors.SwitchTransformer;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.LinkedList;

import static org.junit.Assert.fail;

/**
 * Contains tests for {@link IndexedCollection}.
 * This test class focuses on exception propagation behavior.
 */
public class IndexedCollectionTest {

    /**
     * Tests that an exception thrown by the key transformer during an `add` operation
     * is correctly propagated to the caller.
     */
    @Test
    public void addShouldPropagateExceptionFromKeyTransformer() {
        // --- Arrange ---

        // 1. Create a SwitchTransformer that is intentionally misconfigured to throw
        //    an ArrayIndexOutOfBoundsException. It has two predicates but only one
        //    corresponding transformer.
        final Predicate<Object> isNullPredicate = NullPredicate.nullPredicate();
        final Predicate<Object> isNotNullPredicate = NotNullPredicate.notNullPredicate();

        @SuppressWarnings("unchecked")
        final Predicate<Object>[] predicates = (Predicate<Object>[]) Array.newInstance(Predicate.class, 2);
        predicates[0] = isNullPredicate;
        predicates[1] = isNotNullPredicate;

        // Only one transformer is provided for the two predicates.
        @SuppressWarnings("unchecked")
        final Transformer<Object, ?>[] transformers = (Transformer<Object, ?>[]) Array.newInstance(Transformer.class, 1);

        final Transformer<Object, Object> defaultTransformer = ConstantTransformer.nullTransformer();

        // This keyTransformer will fail when it encounters a non-null object, as it will
        // match the second predicate but there is no transformer at index 1.
        final Transformer<Transformer<Integer, Integer>, Object> keyTransformer =
                new SwitchTransformer<>(predicates, transformers, defaultTransformer);

        // 2. Create the IndexedCollection with the faulty key transformer.
        final Collection<Transformer<Integer, Integer>> backingList = new LinkedList<>();
        final IndexedCollection<Object, Transformer<Integer, Integer>> indexedCollection =
                IndexedCollection.uniqueIndexedCollection(backingList, keyTransformer);

        // 3. Create a non-null element to add.
        final Transformer<Integer, Integer> elementToAdd = ExceptionTransformer.exceptionTransformer();

        // --- Act & Assert ---
        try {
            indexedCollection.add(elementToAdd);
            fail("Expected an ArrayIndexOutOfBoundsException to be thrown");
        } catch (final ArrayIndexOutOfBoundsException e) {
            // This exception is expected.
            // It confirms that the exception from the underlying SwitchTransformer
            // was propagated correctly by the IndexedCollection.
        }
    }
}