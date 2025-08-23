package com.google.common.util.concurrent;

/**
 * Tests for {@link AtomicDoubleArray#updateAndGet(int, java.util.function.DoubleUnaryOperator)}.
 */
public class AtomicDoubleArrayUpdateAndGetTest extends JSR166TestCase {

    /**
     * A selection of "interesting" double values for testing, including infinities, zeros, NaN, and
     * other boundary values.
     */
    private static final double[] TEST_DOUBLES = {
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
     * Asserts that two doubles are bit-wise equal, which is the notion of equality used by {@link
     * AtomicDoubleArray}.
     */
    private static void assertBitEquals(double expected, double actual) {
        assertEquals(
            "Bitwise representations should be equal",
            Double.doubleToRawLongBits(expected),
            Double.doubleToRawLongBits(actual));
    }

    /**
     * Verifies that updateAndGet correctly applies a subtraction function, returns the updated value,
     * and stores it in the array. The test is performed on the first and last elements of the array
     * using a wide range of double values to ensure correctness with edge cases.
     */
    public void testUpdateAndGet_withSubtraction() {
        // SIZE is a constant provided by the JSR166TestCase base class.
        AtomicDoubleArray array = new AtomicDoubleArray(SIZE);
        int[] indicesToTest = {0, SIZE - 1};

        for (int index : indicesToTest) {
            for (double initialValue : TEST_DOUBLES) {
                for (double valueToSubtract : TEST_DOUBLES) {
                    // Arrange: Set the initial value in the array.
                    array.set(index, initialValue);
                    double expectedResult = initialValue - valueToSubtract;

                    // Act: Apply the update function.
                    double returnedValue =
                        array.updateAndGet(index, currentValue -> currentValue - valueToSubtract);

                    // Assert: Check that the returned value and the stored value are correct.
                    assertBitEquals(expectedResult, returnedValue);
                    assertBitEquals(expectedResult, array.get(index));
                }
            }
        }
    }
}