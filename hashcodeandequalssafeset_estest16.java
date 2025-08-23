package org.mockito.internal.util.collections;

import org.junit.Test;
import java.util.Collections;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link HashCodeAndEqualsSafeSet}.
 */
public class HashCodeAndEqualsSafeSetTest {

    /**
     * Verifies that calling addAll with an empty collection does not modify the set
     * and correctly returns false, adhering to the Collection#addAll contract.
     */
    @Test
    public void addAllWithEmptyCollectionShouldReturnFalse() {
        // Arrange: Create an empty set.
        HashCodeAndEqualsSafeSet safeSet = new HashCodeAndEqualsSafeSet();

        // Act: Attempt to add all elements from an empty collection.
        boolean wasSetModified = safeSet.addAll(Collections.emptyList());

        // Assert: The set should not have been modified, and the method should return false.
        assertFalse("addAll should return false when the collection to add is empty.", wasSetModified);
        assertTrue("The set should remain empty after the operation.", safeSet.isEmpty());
    }
}