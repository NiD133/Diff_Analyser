package org.jfree.data;

import org.junit.Test;

/**
 * Unit tests for the {@link ComparableObjectItem} class.
 */
public class ComparableObjectItemTest {

    /**
     * Verifies that the compareTo() method throws a NullPointerException when
     * the argument is null, which is the expected behavior according to the
     * java.lang.Comparable contract.
     */
    @Test(expected = NullPointerException.class)
    public void compareTo_withNullArgument_shouldThrowNullPointerException() {
        // Arrange: Create an instance of the class under test.
        ComparableObjectItem item = new ComparableObjectItem("anyComparable", "anyObject");

        // Act: Call the method with a null argument.
        // The @Test(expected) annotation will automatically handle the assertion.
        item.compareTo(null);
    }
}