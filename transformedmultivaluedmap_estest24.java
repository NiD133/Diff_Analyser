package org.apache.commons.collections4.multimap;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.NOPTransformer;
import org.junit.Test;

/**
 * Unit tests for {@link TransformedMultiValuedMap}.
 */
public class TransformedMultiValuedMapTest {

    /**
     * Tests that calling putAll() with a null iterable for the values
     * throws a NullPointerException.
     */
    @Test(expected = NullPointerException.class)
    public void putAllWithNullIterableShouldThrowNullPointerException() {
        // Arrange
        final MultiValuedMap<Integer, Integer> baseMap = new ArrayListValuedLinkedHashMap<>();
        final Transformer<Integer, Integer> identityTransformer = NOPTransformer.nopTransformer();

        // The map under test, which transforms keys and values upon insertion.
        // Here, we use a no-op transformer that doesn't change the elements.
        final MultiValuedMap<Integer, Integer> transformedMap =
                TransformedMultiValuedMap.transformedMap(baseMap, identityTransformer, identityTransformer);

        final Integer key = 1;
        final Iterable<Integer> nullValues = null;

        // Act: Attempt to add a null collection of values for a given key.
        // Assert: The @Test(expected) annotation asserts that a NullPointerException is thrown.
        transformedMap.putAll(key, nullValues);
    }
}