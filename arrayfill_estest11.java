package org.apache.commons.lang3;

import static org.junit.Assert.assertThrows;

import org.junit.Test;

/**
 * Tests for {@link ArrayFill}.
 */
public class ArrayFillTest {

    /**
     * Tests that calling {@code ArrayFill.fill(T[], T)} with an element of a type
     * incompatible with the array's component type throws an {@link ArrayStoreException}.
     * This behavior is consistent with {@link java.util.Arrays#fill(Object[], Object)}.
     */
    @Test
    public void testFillObjectArrayWithIncompatibleTypeThrowsArrayStoreException() {
        // Arrange: Create an array of a specific reference type.
        final Integer[] integerArray = new Integer[3];

        // Arrange: Create an element of a type that cannot be stored in the array.
        final String incompatibleElement = "I am not an integer";

        // Act & Assert: Attempting to fill the Integer array with a String should fail.
        // The compiler allows this call by inferring the common supertype 'Object' for T,
        // but the JVM throws an ArrayStoreException at runtime.
        assertThrows(ArrayStoreException.class, () -> {
            ArrayFill.fill(integerArray, incompatibleElement);
        });
    }

    /**
     * This is an alternative implementation using JUnit 4's 'expected' parameter,
     * which was common before JUnit 5 and lambda expressions.
     * It achieves the same goal as the test above.
     */
    @Test(expected = ArrayStoreException.class)
    public void testFillObjectArrayWithIncompatibleTypeThrowsArrayStoreException_Junit4Style() {
        // Arrange
        final Integer[] integerArray = new Integer[3];
        final String incompatibleElement = "I am not an integer";

        // Act: This call will throw the expected ArrayStoreException.
        ArrayFill.fill(integerArray, incompatibleElement);
    }
}