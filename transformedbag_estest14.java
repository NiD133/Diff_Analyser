package org.apache.commons.collections4.bag;

import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.apache.commons.collections4.functors.SwitchTransformer;
import org.apache.commons.collections4.functors.TruePredicate;
import org.junit.Test;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for TransformedBag.
 * This test focuses on the interaction with a misconfigured SwitchTransformer.
 */
public class TransformedBagTest {

    /**
     * Tests that transformedBag() throws an exception when the provided SwitchTransformer
     * has a predicate array that is longer than its transformer array.
     *
     * The transformedBag() method iterates over existing elements. When the SwitchTransformer
     * finds a matching predicate, it attempts to access the transformer at the same index.
     * If that index is out of bounds for the transformer array, an exception is expected.
     */
    @Test
    public void transformedBagWithMismatchedSwitchTransformerThrowsException() {
        // Arrange
        // 1. Create a source bag with an element to ensure the transformation is triggered.
        final Bag<Integer> sourceBag = new HashBag<>();
        sourceBag.add(100);

        // 2. Set up a SwitchTransformer where the number of predicates (1) is greater than
        //    the number of transformers (0). This is the specific misconfiguration under test.
        final Predicate<?>[] predicates = { TruePredicate.truePredicate() }; // This predicate will always match.
        final Transformer<?, ?>[] transformers = {}; // The transformer array is empty.
        final Transformer<Integer, Integer> defaultTransformer = ConstantTransformer.nullTransformer();

        final Transformer<? super Integer, ? extends Integer> mismatchedSwitchTransformer =
                new SwitchTransformer<>(predicates, transformers, defaultTransformer);

        // Act & Assert
        // Verify that calling transformedBag() with this setup throws an exception.
        final ArrayIndexOutOfBoundsException exception = assertThrows(
            ArrayIndexOutOfBoundsException.class,
            () -> TransformedBag.transformedBag(sourceBag, mismatchedSwitchTransformer)
        );

        // The exception message "0" confirms the out-of-bounds access was at the first index.
        assertEquals("0", exception.getMessage());
    }
}