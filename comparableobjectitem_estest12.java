package org.jfree.data;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link ComparableObjectItem} class.
 */
public class ComparableObjectItemTest {

    /**
     * Verifies that the equals() method returns false when two items
     * have the same 'comparable' key but different 'object' payloads.
     */
    @Test
    public void equals_withSameComparableButDifferentObjects_shouldReturnFalse() {
        // Arrange: Create two items with the same key but distinct object payloads.
        String key = "ID-1";
        Object payload1 = "Payload A";
        ComparableObjectItem item1 = new ComparableObjectItem(key, payload1);

        Object payload2 = "Payload B";
        ComparableObjectItem item2 = new ComparableObjectItem(key, payload2);

        // Act & Assert: The equals method should return false because the payloads differ.
        assertFalse("Items with different object payloads should not be equal.", item1.equals(item2));
    }
}