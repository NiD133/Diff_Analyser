package org.apache.commons.collections4.collection;

import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.apache.commons.collections4.functors.TransformerPredicate;
import org.junit.Test;

import java.util.LinkedList;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * This test class contains an improved version of a test for IndexedCollection.
 * The original test was auto-generated and lacked clarity.
 */
public class IndexedCollection_ESTestTest16 { // Retaining original class name for context

    /**
     * Tests that IndexedCollection.removeIf() throws a RuntimeException when
     * the provided Predicate is a TransformerPredicate whose transformer
     * incorrectly returns null instead of a Boolean.
     */
    @Test
    public void removeIfShouldThrowExceptionWhenPredicateTransformerReturnsNull() {
        // Arrange
        // 1. Create a transformer that is incorrectly configured to always return null.
        //    TransformerPredicate requires its transformer to return a non-null Boolean.
        final Transformer<Object, Boolean> nullReturningTransformer = ConstantTransformer.nullTransformer();
        final Predicate<Object> faultyPredicate = TransformerPredicate.transformerPredicate(nullReturningTransformer);

        // 2. Create a simple IndexedCollection with one element to ensure the predicate is executed.
        final Collection<Object> baseCollection = new LinkedList<>();
        baseCollection.add("test element");

        // The key transformer is not relevant to this test's logic.
        final Transformer<Object, Object> keyTransformer = input -> input;
        final IndexedCollection<Object, Object> indexedCollection =
                IndexedCollection.nonUniqueIndexedCollection(baseCollection, keyTransformer);

        // Act & Assert
        try {
            indexedCollection.removeIf(faultyPredicate);
            fail("Expected a RuntimeException because the predicate's transformer returned null.");
        } catch (final RuntimeException e) {
            // 3. Verify that the correct exception is thrown from TransformerPredicate.
            final String expectedMessage = "Transformer must return an instanceof Boolean, it was a null object";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}