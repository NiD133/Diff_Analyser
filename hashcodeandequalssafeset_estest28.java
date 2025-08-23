package org.mockito.internal.util.collections;

import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for {@link HashCodeAndEqualsSafeSet}.
 */
public class HashCodeAndEqualsSafeSetTest {

    @Test
    public void retainAllOnEmptySetShouldReturnFalseAndNotModifySet() {
        // Arrange
        HashCodeAndEqualsSafeSet emptySet = new HashCodeAndEqualsSafeSet();
        List<Object> collectionToRetain = Collections.singletonList("any object");

        // Act
        // Calling retainAll on an empty set should not change it.
        boolean wasModified = emptySet.retainAll(collectionToRetain);

        // Assert
        // According to the Collection#retainAll contract, the method should return 'false'
        // if the collection was not modified.
        assertFalse("retainAll should return false when the set is not modified.", wasModified);
        assertTrue("The set should remain empty after the retainAll operation.", emptySet.isEmpty());
    }
}