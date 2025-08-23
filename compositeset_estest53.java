package org.apache.commons.collections4.set;

import org.junit.Test;

/**
 * Contains tests for the {@link CompositeSet} class.
 */
public class CompositeSetTest {

    /**
     * Tests that calling toArray(T[] array) with a null array argument
     * throws a NullPointerException, as specified by the general
     * contract of the {@link java.util.Collection#toArray(Object[])} method.
     */
    @Test(expected = NullPointerException.class)
    public void toArrayWithNullArrayShouldThrowNullPointerException() {
        // Arrange: Create an empty CompositeSet.
        final CompositeSet<Object> compositeSet = new CompositeSet<>();

        // Act: Call the toArray method with a null argument.
        // The @Test(expected) annotation will automatically handle the assertion.
        compositeSet.toArray((Object[]) null);
    }
}