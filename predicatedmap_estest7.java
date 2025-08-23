package org.apache.commons.collections4.map;

import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.apache.commons.collections4.functors.TransformerPredicate;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * This test class focuses on specific validation behaviors of the {@link PredicatedMap}.
 */
public class PredicatedMapTest {

    /**
     * Tests that the validate() method correctly propagates the RuntimeException
     * thrown by a key predicate whose underlying transformer returns null.
     * The TransformerPredicate expects a non-null Boolean from its transformer.
     */
    @Test
    public void validateShouldThrowExceptionWhenKeyPredicateTransformerReturnsNull() {
        // Arrange
        final Map<Integer, String> baseMap = new HashMap<>();

        // Create a Transformer that always returns null.
        final Transformer<Integer, Boolean> nullReturningTransformer = ConstantTransformer.nullTransformer();

        // Wrap the transformer in a predicate. This predicate will throw a RuntimeException
        // when evaluated because the transformer doesn't return a Boolean.
        final Predicate<Integer> keyPredicate = new TransformerPredicate<>(nullReturningTransformer);

        // Create the PredicatedMap with the faulty key predicate. The value predicate is null (no validation).
        final PredicatedMap<Integer, String> predicatedMap = new PredicatedMap<>(baseMap, keyPredicate, null);

        final Integer testKey = 123;
        final String anyValue = "anyValue";

        // Act & Assert
        try {
            predicatedMap.validate(testKey, anyValue);
            fail("A RuntimeException was expected but not thrown.");
        } catch (final RuntimeException e) {
            // Verify that the correct exception was thrown by the underlying predicate.
            final String expectedMessage = "Transformer must return an instanceof Boolean, it was a null object";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}