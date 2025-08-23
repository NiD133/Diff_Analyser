package org.mockito.internal.util.collections;

import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link HashCodeAndEqualsSafeSet}.
 */
public class HashCodeAndEqualsSafeSetTest {

    @Test
    public void addAllShouldHandleCircularReferencesWithoutError() {
        // Arrange: Create a list that contains a wrapper of itself.
        // This structure would cause a StackOverflowError in a standard HashSet
        // if its hashCode() or equals() method were called, due to infinite recursion.
        List<Object> listWithCircularReference = new LinkedList<>();
        Object wrapper = HashCodeAndEqualsMockWrapper.of(listWithCircularReference);
        listWithCircularReference.add(wrapper);

        HashCodeAndEqualsSafeSet safeSet = new HashCodeAndEqualsSafeSet();

        // Act: Add all elements from the self-referencing list to the safeSet.
        // The set is expected to handle this gracefully by using identity-based
        // equality and hashing, thus avoiding the recursion.
        boolean wasModified = safeSet.addAll(listWithCircularReference);

        // Assert: The operation should succeed, and the set should contain the element.
        assertTrue("The set should report that it was modified.", wasModified);
        assertEquals("The set should contain exactly one element.", 1, safeSet.size());
        assertTrue("The set should contain the wrapper from the original list.", safeSet.contains(wrapper));
    }
}