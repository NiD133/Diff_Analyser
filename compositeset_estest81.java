package org.apache.commons.collections4.set;

import org.junit.Test;

/**
 * This test class contains focused tests for the CompositeSet class.
 * The original auto-generated test was refactored for clarity and maintainability.
 */
public class CompositeSetTest {

    /**
     * Tests that calling toArray() with a null argument throws a NullPointerException,
     * as specified by the java.util.Collection interface contract.
     */
    @Test(expected = NullPointerException.class)
    public void toArray_whenGivenNullArray_shouldThrowNullPointerException() {
        // Arrange: Create an empty CompositeSet.
        // The set's contents are irrelevant for this test.
        final CompositeSet<Object> compositeSet = new CompositeSet<>();
        final Object[] nullArray = null;

        // Act: Call the method under test with a null argument.
        compositeSet.toArray(nullArray);

        // Assert: The test succeeds if a NullPointerException is thrown,
        // which is handled by the @Test(expected=...) annotation.
    }
}