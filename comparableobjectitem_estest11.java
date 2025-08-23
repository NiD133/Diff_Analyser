package org.jfree.data;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.util.Objects;

/**
 * A set of tests for the hashCode() method of the {@link ComparableObjectItem} class.
 */
public class ComparableObjectItemTest {

    /**
     * Verifies that the hashCode() method correctly handles a null object value.
     * <p>
     * The method should not throw a NullPointerException and should produce a
     * hash code that is consistent with its components (the comparable key and the null object).
     */
    @Test
    public void hashCode_withNullObject_returnsCorrectHashCode() {
        // Arrange: Create an item with a non-null comparable and a null object.
        String comparableKey = "TestKey";
        ComparableObjectItem itemWithNullObject = new ComparableObjectItem(comparableKey, null);

        // Act: Calculate the actual hash code from the item.
        int actualHashCode = itemWithNullObject.hashCode();

        // Assert: The hash code should match the expected value, calculated in a
        // standard way. This confirms the implementation is correct and robust.
        int expectedHashCode = Objects.hash(comparableKey, null);
        assertEquals(expectedHashCode, actualHashCode);
    }
}