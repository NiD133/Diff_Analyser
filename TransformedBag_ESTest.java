package org.apache.commons.collections4.bag;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.apache.commons.collections4.functors.ExceptionTransformer;
import org.junit.Test;

/**
 * A more understandable version of the test suite for {@link TransformedBag}.
 *
 * This suite focuses on clarity and demonstrating the core behaviors of the class,
 * especially the crucial detail that the transformer is only applied when adding elements.
 * Methods like getCount(), remove(), and contains() require the already-transformed object.
 */
public class TransformedBagTest {

    private final Transformer<String, String> TO_UPPER_CASE = s -> (s == null) ? null : s.toUpperCase();

    // --- Core Behavior Demonstration ---

    /**
     * This test demonstrates the most important behavior of TransformedBag:
     * 1. The transformer is ONLY applied when adding elements.
     * 2. Accessor methods (getCount, contains) and remove methods must be called with
     *    the post-transformation object.
     */
    @Test
    public void add_shouldTransformElementWhileAccessorsExpectTransformedElement() {
        Bag<String> originalBag = new HashBag<>();
        Bag<String> transformedBag = TransformedBag.transformingBag(originalBag, TO_UPPER_CASE);

        // Add "a" and "b", which are transformed to "A" and "B" before being stored.
        transformedBag.add("a");
        transformedBag.add("b", 2);

        // The bag now contains the TRANSFORMED elements.
        assertEquals(3, transformedBag.size());
        assertTrue(originalBag.contains("A"));
        assertTrue(originalBag.contains("B"));

        // Accessor methods must use the TRANSFORMED value.
        assertTrue(transformedBag.contains("A"));
        assertEquals(1, transformedBag.getCount("A"));
        assertEquals(2, transformedBag.getCount("B"));

        // Accessor methods will not find the pre-transformed value.
        assertFalse(transformedBag.contains("a"));
        assertEquals(0, transformedBag.getCount("a"));

        // Remove methods must also use the TRANSFORMED value.
        assertFalse("Should not remove pre-transformed value", transformedBag.remove("b"));
        assertTrue("Should remove post-transformed value", transformedBag.remove("B"));
        assertEquals(1, transformedBag.getCount("B"));
    }

    // --- Static Factory Method Tests ---

    @Test
    public void transformingBag_shouldNotTransformExistingElements() {
        Bag<String> bag = new HashBag<>(Arrays.asList("One", "Two"));
        Bag<String> transformedBag = TransformedBag.transformingBag(bag, TO_UPPER_CASE);

        // Existing elements are not transformed.
        assertTrue(transformedBag.contains("One"));
        assertFalse(transformedBag.contains("ONE"));

        // New elements are transformed.
        transformedBag.add("three");
        assertTrue(transformedBag.contains("THREE"));
        assertFalse(transformedBag.contains("three"));
    }

    @Test
    public void transformedBag_shouldTransformExistingElements() {
        Bag<String> bag = new HashBag<>(Arrays.asList("One", "Two", "Two"));
        Bag<String> transformedBag = TransformedBag.transformedBag(bag, TO_UPPER_CASE);

        // Existing elements are transformed in place.
        assertFalse(transformedBag.contains("One"));
        assertTrue(transformedBag.contains("ONE"));
        assertEquals(1, transformedBag.getCount("ONE"));
        assertEquals(2, transformedBag.getCount("TWO"));
    }

    @Test(expected = NullPointerException.class)
    public void transformingBag_shouldThrowExceptionForNullBag() {
        TransformedBag.transformingBag(null, TO_UPPER_CASE);
    }

    @Test(expected = NullPointerException.class)
    public void transformingBag_shouldThrowExceptionForNullTransformer() {
        TransformedBag.transformingBag(new HashBag<>(), null);
    }

    @Test(expected = NullPointerException.class)
    public void transformedBag_shouldThrowExceptionForNullBag() {
        TransformedBag.transformedBag(null, TO_UPPER_CASE);
    }

    @Test(expected = NullPointerException.class)
    public void transformedBag_shouldThrowExceptionForNullTransformer() {
        TransformedBag.transformedBag(new HashBag<>(), null);
    }

    @Test(expected = RuntimeException.class)
    public void transformedBag_shouldPropagateExceptionFromTransformer() {
        Bag<String> bag = new HashBag<>(Collections.singletonList("a"));
        Transformer<String, String> failingTransformer = ExceptionTransformer.exceptionTransformer();
        TransformedBag.transformedBag(bag, failingTransformer);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void transformedBag_shouldThrowExceptionOnUnmodifiableBag() {
        Bag<String> unmodifiable = UnmodifiableBag.unmodifiableBag(new HashBag<>(Collections.singletonList("a")));
        TransformedBag.transformedBag(unmodifiable, TO_UPPER_CASE);
    }

    // --- Method-Specific Tests ---

    @Test
    public void add_shouldReturnFalseForNegativeCount() {
        Bag<String> transformedBag = TransformedBag.transformingBag(new HashBag<>(), TO_UPPER_CASE);
        assertFalse(transformedBag.add("a", -1));
        assertTrue(transformedBag.isEmpty());
    }

    @Test(expected = RuntimeException.class)
    public void add_shouldPropagateExceptionFromTransformer() {
        Bag<Object> bag = TransformedBag.transformingBag(new HashBag<>(), ExceptionTransformer.exceptionTransformer());
        bag.add(new Object());
    }

    @Test(expected = IllegalArgumentException.class)
    public void add_shouldThrowExceptionWhenTransformedObjectIsRejectedByPredicate() {
        Predicate<String> predicate = s -> !s.contains("X");
        Bag<String> predicatedBag = PredicatedBag.predicatedBag(new HashBag<>(), predicate);
        Transformer<String, String> transformer = s -> s + "X";
        Bag<String> transformedBag = TransformedBag.transformingBag(predicatedBag, transformer);

        transformedBag.add("a"); // "a" becomes "aX", which the predicate rejects.
    }

    @Test(expected = NullPointerException.class)
    public void add_shouldThrowExceptionWhenUnderlyingBagRejectsNull() {
        Bag<String> treeBag = new TreeBag<>();
        Transformer<String, String> toNullTransformer = ConstantTransformer.nullTransformer();
        Bag<String> transformedBag = TransformedBag.transformingBag(treeBag, toNullTransformer);

        transformedBag.add("a"); // "a" becomes null, which TreeBag rejects.
    }

    @Test
    public void remove_shouldReturnFalseForZeroCount() {
        Bag<String> bag = new HashBag<>(Collections.singletonList("A"));
        Bag<String> transformedBag = TransformedBag.transformingBag(bag, TO_UPPER_CASE);
        assertFalse(transformedBag.remove("A", 0));
        assertEquals(1, transformedBag.getCount("A"));
    }

    @Test
    public void getCount_shouldReturnZeroForObjectNotInBag() {
        Bag<String> transformedBag = TransformedBag.transformingBag(new HashBag<>(), TO_UPPER_CASE);
        transformedBag.add("a"); // adds "A"
        assertEquals(0, transformedBag.getCount("B"));
    }

    @Test
    public void uniqueSet_shouldReturnSetOfUnderlyingUniqueElements() {
        Bag<String> bag = new HashBag<>();
        Bag<String> transformedBag = TransformedBag.transformingBag(bag, TO_UPPER_CASE);
        transformedBag.add("a");
        transformedBag.add("b");
        transformedBag.add("a"); // adds another "A"

        assertEquals(2, transformedBag.uniqueSet().size());
        assertTrue(transformedBag.uniqueSet().contains("A"));
        assertTrue(transformedBag.uniqueSet().contains("B"));
    }

    @Test
    public void getBag_shouldReturnTheDecoratedBag() {
        Bag<String> bag = new HashBag<>();
        TransformedBag<String> transformedBag = (TransformedBag<String>) TransformedBag.transformingBag(bag, TO_UPPER_CASE);
        assertSame(bag, transformedBag.getBag());
    }

    // --- equals() and hashCode() Tests ---

    @Test
    public void equals_shouldBeTrueForSelf() {
        Bag<String> transformedBag = TransformedBag.transformingBag(new HashBag<>(), TO_UPPER_CASE);
        assertEquals(transformedBag, transformedBag);
    }

    @Test
    public void equals_shouldBeFalseForDifferentClass() {
        Bag<String> transformedBag = TransformedBag.transformingBag(new HashBag<>(), TO_UPPER_CASE);
        assertNotEquals(transformedBag, new Object());
    }

    @Test
    public void equals_shouldBeBasedOnUnderlyingBag() {
        Bag<String> bag1 = new HashBag<>(Arrays.asList("A", "B"));
        Bag<String> transformedBag1 = TransformedBag.transformingBag(bag1, TO_UPPER_CASE);

        Bag<String> bag2 = new HashBag<>(Arrays.asList("A", "B"));
        Bag<String> transformedBag2 = TransformedBag.transformingBag(bag2, TO_UPPER_CASE);

        Bag<String> bag3 = new HashBag<>(Arrays.asList("X", "Y"));
        Bag<String> transformedBag3 = TransformedBag.transformingBag(bag3, TO_UPPER_CASE);

        assertEquals(transformedBag1, transformedBag2);
        assertNotEquals(transformedBag1, transformedBag3);
    }

    @Test
    public void hashCode_shouldBeBasedOnUnderlyingBag() {
        Bag<String> bag1 = new HashBag<>(Arrays.asList("A", "B"));
        Bag<String> transformedBag1 = TransformedBag.transformingBag(bag1, TO_UPPER_CASE);

        Bag<String> bag2 = new HashBag<>(Arrays.asList("A", "B"));
        Bag<String> transformedBag2 = TransformedBag.transformingBag(bag2, TO_UPPER_CASE);

        assertEquals(transformedBag1.hashCode(), transformedBag2.hashCode());
    }
}