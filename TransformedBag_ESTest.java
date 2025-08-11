package org.apache.commons.collections4.bag;

import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.bag.HashBag;
import org.apache.commons.collections4.bag.TransformedBag;
import org.apache.commons.collections4.bag.UnmodifiableBag;
import org.junit.Test;

import java.util.Locale;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Focused and readable tests for TransformedBag.
 * 
 * These tests exercise the key behaviors documented on TransformedBag:
 * - transformingBag: existing contents are NOT transformed; future adds are transformed
 * - transformedBag: existing contents ARE transformed immediately
 * - add/remove/getCount contract with transformed values
 * - basic error handling and delegation to the wrapped bag
 */
public class TransformedBagTest {

    private static Transformer<String, String> upper() {
        return new Transformer<String, String>() {
            @Override
            public String transform(final String input) {
                return input == null ? null : input.toUpperCase(Locale.ROOT);
            }
        };
    }

    // transformingBag: existing contents NOT transformed
    @Test
    public void transformingBag_doesNotChangeExistingElements() {
        // Arrange
        final HashBag<String> raw = new HashBag<>();
        raw.add("a", 2);
        raw.add("b", 1);

        // Act
        final Bag<String> bag = TransformedBag.transformingBag(raw, upper());

        // Assert (existing values are left as-is)
        assertEquals(2, bag.getCount("a"));
        assertEquals(1, bag.getCount("b"));
        assertEquals(0, bag.getCount("A"));
        assertEquals(0, bag.getCount("B"));
    }

    // transformingBag: future adds ARE transformed
    @Test
    public void transformingBag_transformsOnAdd_andQueriesUseTransformedForm() {
        // Arrange
        final HashBag<String> raw = new HashBag<>();
        final Bag<String> bag = TransformedBag.transformingBag(raw, upper());

        // Act
        bag.add("c", 2); // will be stored as "C" twice

        // Assert
        assertEquals(2, bag.getCount("C"));
        assertEquals(0, bag.getCount("c")); // must query using transformed form
        assertTrue(bag.remove("C", 1));
        assertEquals(1, bag.getCount("C"));
        assertFalse(bag.remove("c", 1)); // not present under untransformed key
    }

    // transformedBag: existing contents ARE transformed
    @Test
    public void transformedBag_transformsExistingContentsImmediately() {
        // Arrange
        final HashBag<String> raw = new HashBag<>();
        raw.add("a");

        // Act
        final Bag<String> bag = TransformedBag.transformedBag(raw, upper());

        // Assert (the original "a" is transformed to "A")
        assertEquals(1, bag.getCount("A"));
        assertEquals(0, bag.getCount("a"));
    }

    // transformedBag: add with nCopies applies transformation once per copy
    @Test
    public void transformedBag_addWithCopies() {
        // Arrange
        final Bag<String> bag = TransformedBag.transformedBag(new HashBag<>(), upper());

        // Act
        bag.add("x", 3);

        // Assert
        assertEquals(3, bag.getCount("X"));
        assertEquals(0, bag.getCount("x"));
    }

    // Both factories validate arguments
    @Test(expected = NullPointerException.class)
    public void transformedBag_nullBag_throws() {
        TransformedBag.transformedBag(null, upper());
    }

    @Test(expected = NullPointerException.class)
    public void transformedBag_nullTransformer_throws() {
        TransformedBag.transformedBag(new HashBag<String>(), null);
    }

    @Test(expected = NullPointerException.class)
    public void transformingBag_nullBag_throws() {
        TransformedBag.transformingBag(null, upper());
    }

    @Test(expected = NullPointerException.class)
    public void transformingBag_nullTransformer_throws() {
        TransformedBag.transformingBag(new HashBag<String>(), null);
    }

    // Add to an unmodifiable bag still respects unmodifiable semantics
    @Test(expected = UnsupportedOperationException.class)
    public void transformedBag_addOnUnmodifiableBag_throws() {
        // Arrange
        final Bag<String> unmodifiable = UnmodifiableBag.unmodifiableBag(new HashBag<String>());

        // Act
        final Bag<String> bag = TransformedBag.transformedBag(unmodifiable, upper());
        bag.add("x", 1); // still unmodifiable
    }

    // Null values are passed through the transformer (which returns null here)
    @Test
    public void transformingBag_allowsNulls() {
        // Arrange
        final Bag<String> bag = TransformedBag.transformingBag(new HashBag<String>(), upper());

        // Act
        bag.add(null, 2);

        // Assert
        assertEquals(2, bag.getCount(null));
        assertTrue(bag.remove(null, 1));
        assertEquals(1, bag.getCount(null));
    }

    // uniqueSet reflects the transformed view
    @Test
    public void transformingBag_uniqueSetReflectsTransformedAdds() {
        // Arrange
        final Bag<String> bag = TransformedBag.transformingBag(new HashBag<String>(), upper());

        // Act
        bag.add("a", 2); // stored as "A"

        // Assert
        final Set<String> unique = bag.uniqueSet();
        assertEquals(1, unique.size());
        assertTrue(unique.contains("A"));
        assertFalse(unique.contains("a"));
    }

    // Basic equals/hashCode sanity: reflexive and consistent for same instance
    @Test
    public void equalsAndHashCode_reflexiveAndConsistent() {
        // Arrange
        final Bag<String> bag = TransformedBag.transformingBag(new HashBag<String>(), upper());

        // Assert
        assertTrue(bag.equals(bag));
        assertEquals(bag.hashCode(), bag.hashCode());
    }
}