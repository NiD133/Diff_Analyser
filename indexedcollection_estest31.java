package org.apache.commons.collections4.collection;

import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.CloneTransformer;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Contains tests for {@link IndexedCollection}.
 */
public class IndexedCollectionTest {

    /**
     * Tests that creating an IndexedCollection with a CloneTransformer fails
     * when the elements in the source collection are not publicly cloneable.
     * The standard {@link Object} class has a protected clone method, not a public one,
     * which does not satisfy the requirements of the CloneTransformer.
     */
    @Test
    public void nonUniqueIndexedCollectionWithCloneTransformerShouldThrowExceptionForNonCloneableObject() {
        // Arrange: Create a collection with an object that is not publicly cloneable.
        final List<Object> sourceCollection = Collections.singletonList(new Object());
        final Transformer<Object, Object> cloneTransformer = CloneTransformer.cloneTransformer();

        // Act & Assert: Attempting to create the IndexedCollection should fail.
        try {
            // The constructor calls reindex(), which applies the transformer to each element.
            // The CloneTransformer will fail because java.lang.Object is not publicly cloneable.
            IndexedCollection.nonUniqueIndexedCollection(sourceCollection, cloneTransformer);
            fail("Expected an IllegalArgumentException because the object is not cloneable.");
        } catch (final IllegalArgumentException e) {
            // Verify that the exception is the one we expect from the underlying PrototypeFactory.
            final String expectedMessage = "The prototype must be cloneable via a public clone method";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}