package org.mockito.internal.util.collections;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test suite for {@link HashCodeAndEqualsSafeSet}.
 * Note: The original test class name 'HashCodeAndEqualsSafeSet_ESTestTest8' 
 * has been simplified for clarity.
 */
public class HashCodeAndEqualsSafeSetTest {

    @Test
    public void remove_shouldReturnTrueAndModifySet_whenElementIsPresent() {
        // Arrange: Create a set containing a specific element.
        Object elementToRemove = new Object();
        HashCodeAndEqualsSafeSet safeSet = HashCodeAndEqualsSafeSet.of(elementToRemove);
        
        // Pre-condition check to ensure the initial state is correct.
        assertEquals("Precondition: Set should contain one element before removal.", 1, safeSet.size());

        // Act: Attempt to remove the element from the set.
        boolean wasRemoved = safeSet.remove(elementToRemove);

        // Assert: Verify that the removal was successful and the set's state was updated correctly.
        assertTrue("remove() should return true for an element that exists in the set.", wasRemoved);
        assertTrue("The set should be empty after its only element is removed.", safeSet.isEmpty());
        assertFalse("The element should no longer be present in the set after removal.", safeSet.contains(elementToRemove));
    }
}