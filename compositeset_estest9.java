package org.apache.commons.collections4.set;

import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.functors.AnyPredicate;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

/**
 * Improved, understandable tests for the {@link CompositeSet} class.
 */
public class CompositeSetImprovedTest {

    /**
     * Tests that calling removeIf() with an AnyPredicate constructed with a null
     * predicate array throws a NullPointerException.
     * <p>
     * The exception originates from the predicate's internal logic when it attempts
     * to iterate over the null array, not from the CompositeSet itself.
     * </p>
     */
    @Test(expected = NullPointerException.class)
    public void removeIfWithPredicateConstructedWithNullArrayThrowsNPE() {
        // Arrange: Create a simple composite set. Its content is not critical for this test.
        final Set<String> innerSet = new HashSet<>();
        innerSet.add("element");
        final CompositeSet<String> compositeSet = new CompositeSet<>(innerSet);

        // Arrange: Create an AnyPredicate with a null array of predicates.
        // This is an invalid state for this specific predicate implementation.
        final Predicate<String> faultyPredicate = new AnyPredicate<>(null);

        // Act: Attempt to use the faulty predicate with the set's removeIf method.
        // This is expected to throw a NullPointerException.
        compositeSet.removeIf(faultyPredicate);
    }
}