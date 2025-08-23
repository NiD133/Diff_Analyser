package org.apache.commons.collections4.multimap;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.AnyPredicate;
import org.apache.commons.collections4.functors.IfTransformer;
import org.apache.commons.collections4.functors.NOPTransformer;
import org.apache.commons.collections4.functors.NullPredicate;
import org.junit.Test;

import java.lang.reflect.Array;

import static org.evosuite.runtime.EvoAssertions.verifyException;
import static org.junit.Assert.fail;

public class TransformedMultiValuedMap_ESTestTest36 extends TransformedMultiValuedMap_ESTest_scaffolding {

    /**
     * Tests that an exception thrown by the value transformer during a 'put' operation
     * is correctly propagated to the caller.
     */
    @Test
    public void testPutWithFailingValueTransformerPropagatesException() {
        // Arrange: Create a value transformer that is designed to fail.
        // The AnyPredicate is constructed with an array containing a null predicate.
        // When 'evaluate' is called on it, it will attempt to dereference the null
        // element, causing a NullPointerException.
        @SuppressWarnings("unchecked")
        final Predicate<Object>[] predicatesWithNull = (Predicate<Object>[]) Array.newInstance(Predicate.class, 1);
        final Predicate<Object> predicateThatThrowsNPE = new AnyPredicate<>(predicatesWithNull);

        // The IfTransformer will use the failing predicate. The true/false transformers
        // are irrelevant as the predicate itself will throw the exception.
        final Transformer<Predicate<Object>, Predicate<Object>> failingValueTransformer =
                new IfTransformer<>(predicateThatThrowsNPE, NOPTransformer.nopTransformer(), NOPTransformer.nopTransformer());

        // Create the map-under-test, decorating a standard map with the failing transformer.
        // The key transformer is null, meaning keys will not be transformed.
        final MultiValuedMap<Integer, Predicate<Object>> baseMap = new LinkedHashSetValuedLinkedHashMap<>();
        final MultiValuedMap<Integer, Predicate<Object>> transformedMap =
                TransformedMultiValuedMap.transformingMap(baseMap, null, failingValueTransformer);

        // The actual key and value passed to put() do not matter, as the transformer
        // will fail regardless of the input.
        final Integer anyKey = 123;
        final Predicate<Object> anyValue = NullPredicate.nullPredicate();

        // Act & Assert
        try {
            transformedMap.put(anyKey, anyValue);
            fail("A NullPointerException should have been thrown by the value transformer.");
        } catch (final NullPointerException e) {
            // This custom assertion from the test scaffolding verifies that the exception
            // originated from the expected component (AnyPredicate), confirming that
            // TransformedMultiValuedMap correctly propagated the failure.
            verifyException("org.apache.commons.collections4.functors.AnyPredicate", e);
        }
    }
}