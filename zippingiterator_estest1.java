package org.apache.commons.collections4.iterators;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

/**
 * Tests for {@link ZippingIterator}.
 */
public class ZippingIteratorTest {

    /**
     * Tests that a ZippingIterator can correctly wrap another ZippingIterator
     * and produce the expected sequence of elements.
     */
    @Test
    public void testZippingIteratorOfAnotherZippingIterator() {
        // --- Arrange ---
        // Create two simple source iterators with distinct, non-empty content.
        Iterator<String> iteratorA = Arrays.asList("A1", "A2").iterator();
        Iterator<String> iteratorB = Arrays.asList("B1").iterator();

        // Create an inner ZippingIterator. We expect it to interleave elements
        // from iteratorA and iteratorB, resulting in the sequence: "A1", "B1", "A2".
        ZippingIterator<String> innerZippingIterator = new ZippingIterator<>(iteratorA, iteratorB);

        // --- Act ---
        // Create an outer ZippingIterator that takes the inner one as its only source.
        // It should simply delegate to the inner iterator, producing the same sequence.
        ZippingIterator<String> outerZippingIterator = new ZippingIterator<>(innerZippingIterator);

        // --- Assert ---
        // 1. Verify that the outer iterator produces the correct interleaved sequence.
        List<String> actualElements = new ArrayList<>();
        outerZippingIterator.forEachRemaining(actualElements::add);

        List<String> expectedElements = Arrays.asList("A1", "B1", "A2");
        assertEquals("The iterator should produce the correctly zipped sequence",
                expectedElements, actualElements);

        // 2. The original test asserted that two different instances are not equal.
        // We can confirm they are different objects, which is a more precise check.
        assertNotSame("The outer and inner iterators should be distinct objects",
                innerZippingIterator, outerZippingIterator);
    }
}