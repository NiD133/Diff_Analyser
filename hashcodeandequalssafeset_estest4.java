package org.mockito.internal.util.collections;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link HashCodeAndEqualsSafeSet}.
 */
public class HashCodeAndEqualsSafeSetTest {

    @Test
    public void size_shouldReturnOne_afterAddingOneElement() {
        // Arrange
        HashCodeAndEqualsSafeSet safeSet = new HashCodeAndEqualsSafeSet();
        Object element = new Object();

        // Act
        safeSet.add(element);

        // Assert
        assertEquals(1, safeSet.size());
    }
}