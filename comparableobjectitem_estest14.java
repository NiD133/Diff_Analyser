package org.jfree.data;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * A test suite for the equals() method in the {@link ComparableObjectItem} class.
 */
public class ComparableObjectItemEqualsTest {

    /**
     * Verifies that two ComparableObjectItem instances are not considered equal
     * if their 'comparable' members are different, even if their 'object'
     * members are the same.
     */
    @Test
    public void equals_shouldReturnFalse_whenComparableMembersAreDifferent() {
        // Arrange: Create two items with different comparable keys but the same object value.
        ComparableObjectItem item1 = new ComparableObjectItem("KeyA", "Value");
        ComparableObjectItem item2 = new ComparableObjectItem("KeyB", "Value");

        // Act: Compare the two items for equality.
        boolean areEqual = item1.equals(item2);

        // Assert: The items should not be equal because their keys differ.
        assertFalse(areEqual);
    }
}