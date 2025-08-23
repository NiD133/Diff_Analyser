package org.jfree.data;

import org.junit.Test;

/**
 * Unit tests for the {@link ComparableObjectItem} class.
 */
public class ComparableObjectItemTest {

    /**
     * This test verifies that a ClassCastException is thrown when the compareTo method
     * is called on items whose internal 'Comparable' objects are of incompatible types.
     *
     * The compareTo method in ComparableObjectItem delegates the comparison to its
     * internal 'comparable' member. This test creates a scenario where this delegation
     * results in an attempt to compare a ComparableObjectItem with a simple String,
     * which correctly causes a ClassCastException.
     */
    @Test(expected = ClassCastException.class)
    public void compareToShouldThrowClassCastExceptionForIncompatibleComparableTypes() {
        // Arrange: Create two items with a nested, incompatible structure.

        // 1. Create a simple comparable object (a String).
        Comparable<String> simpleComparable = "key1";

        // 2. Create the first item using the simple comparable.
        ComparableObjectItem itemWithSimpleComparable = new ComparableObjectItem(simpleComparable, "value1");

        // 3. Create a second item where the 'comparable' part is the *first item* itself.
        // This creates a nested structure: itemWithNestedComparable -> itemWithSimpleComparable -> String
        ComparableObjectItem itemWithNestedComparable = new ComparableObjectItem(itemWithSimpleComparable, "value2");

        // Act: Attempt to compare the two items.
        // This will internally call:
        // itemWithNestedComparable.getComparable().compareTo(itemWithSimpleComparable.getComparable())
        // which translates to:
        // itemWithSimpleComparable.compareTo(simpleComparable)
        //
        // Since 'simpleComparable' is a String and not a ComparableObjectItem,
        // the compareTo method of ComparableObjectItem will throw a ClassCastException.
        itemWithNestedComparable.compareTo(itemWithSimpleComparable);

        // Assert: The @Test(expected) annotation asserts that a ClassCastException is thrown.
        // If no exception is thrown, the test will fail automatically.
    }
}