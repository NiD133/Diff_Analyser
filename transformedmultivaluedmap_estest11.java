package org.apache.commons.collections4.multimap;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.MapTransformer;
import org.apache.commons.collections4.functors.NOPTransformer;
import org.junit.Test;

/**
 * Contains tests for {@link TransformedMultiValuedMap} focusing on specific edge cases.
 */
public class TransformedMultiValuedMapTest {

    /**
     * Tests that the transformedMap() factory method throws a StackOverflowError
     * when given a map that contains itself as a key and a transformer that
     * performs lookups on that same map. This creates an infinite recursion
     * during the initial transformation of the map's contents.
     */
    @Test(expected = StackOverflowError.class)
    public void testTransformedMapWithRecursiveStructureThrowsStackOverflowError() {
        // Arrange: Create a map and a transformer that will cause infinite recursion.

        // 1. The map to be transformed. We use Object as the key type to allow
        // the map to contain itself.
        MultiValuedMap<Object, String> recursiveMap = new LinkedHashSetValuedLinkedHashMap<>();

        // 2. Create the recursive dependency: the map contains itself as a key.
        recursiveMap.put(recursiveMap, "some-value");

        // 3. Create a transformer that looks up keys in the recursiveMap.
        // When the factory method tries to transform the key 'recursiveMap', this
        // transformer will call recursiveMap.asMap().get(recursiveMap), which
        // leads to an infinite recursion via hashCode/equals method calls.
        Transformer<Object, Object> keyTransformer = MapTransformer.mapTransformer(recursiveMap.asMap());

        // The value transformer is not relevant to the error but is a required argument.
        Transformer<String, String> valueTransformer = NOPTransformer.nopTransformer();

        // Act: Attempt to create a transformed map from this recursive structure.
        // The transformedMap() method immediately transforms existing elements,
        // which will trigger the StackOverflowError.
        TransformedMultiValuedMap.transformedMap(recursiveMap, keyTransformer, valueTransformer);

        // Assert: The test passes if a StackOverflowError is thrown, as declared
        // in the @Test(expected=...) annotation. No further assertions are needed.
    }
}