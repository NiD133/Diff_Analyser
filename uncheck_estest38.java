package org.apache.commons.io.function;

import org.junit.Test;

/**
 * Tests for {@link Uncheck}.
 */
public class UncheckTest {

    /**
     * Tests that {@link Uncheck#compare(IOComparator, Object, Object)} throws a
     * {@link NullPointerException} when the given comparator is null.
     */
    @Test(expected = NullPointerException.class)
    public void compareWithNullComparatorThrowsNullPointerException() {
        // Arrange: Define two arbitrary objects to be compared.
        // Their values are irrelevant for this test, as the null comparator
        // will be dereferenced before they are used.
        final String object1 = "a";
        final String object2 = "b";

        // Act: Call the method under test with a null comparator.
        // Assert: The @Test(expected) annotation asserts that a NullPointerException is thrown.
        Uncheck.compare(null, object1, object2);
    }
}