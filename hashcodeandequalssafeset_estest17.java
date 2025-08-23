package org.mockito.internal.util.collections;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for {@link HashCodeAndEqualsSafeSet}.
 * Note: The original test was auto-generated. This version has been refactored
 * for human readability and maintainability.
 */
public class HashCodeAndEqualsSafeSetTest {

    @Test
    public void add_shouldReturnFalse_whenAddingDuplicateElement() {
        // Arrange
        HashCodeAndEqualsSafeSet safeSet = new HashCodeAndEqualsSafeSet();
        Object element = new Object();

        // Act
        // The first attempt to add a new element should succeed.
        boolean firstAddResult = safeSet.add(element);

        // The second attempt to add the same element should fail, as Sets do not allow duplicates.
        boolean secondAddResult = safeSet.add(element);

        // Assert
        assertTrue("First add() of a new element should return true.", firstAddResult);
        assertFalse("Second add() of the same element should return false.", secondAddResult);
    }
}