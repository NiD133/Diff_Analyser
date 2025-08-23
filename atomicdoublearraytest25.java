package com.google.common.util.concurrent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.J2ktIncompatible;

/**
 * Tests for the serialization and deserialization of {@link AtomicDoubleArray}.
 */
@GwtIncompatible
@J2ktIncompatible
public class AtomicDoubleArraySerializationTest extends JSR166TestCase {

    /**
     * An array of special double values, including infinities, NaN, and zero variations,
     * to ensure they are handled correctly during serialization.
     */
    private static final double[] SPECIAL_VALUES = {
        Double.NEGATIVE_INFINITY,
        -Double.MAX_VALUE,
        (double) Long.MIN_VALUE,
        (double) Integer.MIN_VALUE,
        -Math.PI,
        -1.0,
        -Double.MIN_VALUE,
        -0.0,
        +0.0,
        Double.MIN_VALUE,
        1.0,
        Math.PI,
        (double) Integer.MAX_VALUE,
        (double) Long.MAX_VALUE,
        Double.MAX_VALUE,
        Double.POSITIVE_INFINITY,
        Double.NaN,
        Float.MAX_VALUE
    };

    /**
     * Asserts that two double values are bit-wise equal, which is the equality contract
     * used by {@link AtomicDoubleArray}.
     */
    private static void assertBitEquals(double expected, double actual) {
        assertEquals(Double.doubleToRawLongBits(expected), Double.doubleToRawLongBits(actual));
    }

    /**
     * Verifies that a deserialized AtomicDoubleArray is a distinct object instance
     * but contains an identical copy of the original array's data.
     */
    public void testSerialization_preservesArrayContent() throws Exception {
        // Arrange
        final int arraySize = 10;
        AtomicDoubleArray originalArray = new AtomicDoubleArray(arraySize);
        for (int i = 0; i < arraySize; i++) {
            originalArray.set(i, (double) -i);
        }

        // Act
        AtomicDoubleArray deserializedArray = serialClone(originalArray);

        // Assert
        assertNotSame("Deserialized object should be a new instance", originalArray, deserializedArray);
        assertEquals("Deserialized array should have the same length",
            originalArray.length(), deserializedArray.length());

        for (int i = 0; i < arraySize; i++) {
            assertBitEquals(originalArray.get(i), deserializedArray.get(i));
        }
    }

    /**
     * Verifies that special double values (like NaN, infinities, and -0.0) are
     * correctly preserved after a serialization-deserialization cycle.
     */
    public void testSerialization_preservesSpecialValues() throws Exception {
        // Arrange
        AtomicDoubleArray originalArray = new AtomicDoubleArray(SPECIAL_VALUES);

        // Act
        AtomicDoubleArray deserializedArray = serialClone(originalArray);

        // Assert
        assertNotSame("Deserialized object should be a new instance", originalArray, deserializedArray);

        // AtomicDoubleArray does not override Object.equals(), so it uses reference equality.
        // This assertion confirms that behavior and that we have a new instance.
        assertFalse("equals() should be false for different instances", originalArray.equals(deserializedArray));
        assertFalse("equals() should be symmetric", deserializedArray.equals(originalArray));

        assertEquals("Deserialized array should have the same length",
            originalArray.length(), deserializedArray.length());

        for (int i = 0; i < SPECIAL_VALUES.length; i++) {
            assertBitEquals(originalArray.get(i), deserializedArray.get(i));
        }
    }
}