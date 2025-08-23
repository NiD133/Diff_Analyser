package org.apache.commons.collections4.collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Collection;
import org.apache.commons.collections4.Transformer;
import org.junit.Test;

// Note: The original test class name and structure from EvoSuite are preserved
// to show a direct improvement of the provided code. In a real-world scenario,
// the class might be renamed to "IndexedCollectionTest".
public class IndexedCollection_ESTestTest65 extends IndexedCollection_ESTest_scaffolding {

    /**
     * Tests that the factory method {@code uniqueIndexedCollection} throws a
     * NullPointerException when the provided collection is null.
     */
    @Test(timeout = 4000)
    public void uniqueIndexedCollection_shouldThrowNullPointerException_whenCollectionIsNull() {
        // Arrange: The transformer can also be null, as the check for the collection's
        // nullity is expected to happen first.
        final Transformer<Object, ?> nullTransformer = null;

        try {
            // Act: Attempt to create an IndexedCollection with a null collection.
            IndexedCollection.uniqueIndexedCollection((Collection<Object>) null, nullTransformer);

            // Assert: If the method completes without an exception, the test fails.
            fail("Expected a NullPointerException, but it was not thrown.");

        } catch (final NullPointerException e) {
            // Assert: Verify that the exception message is "collection". This confirms
            // that the exception originates from the intended null-check, which is
            // likely Objects.requireNonNull(coll, "collection").
            assertEquals("collection", e.getMessage());
        }
    }
}