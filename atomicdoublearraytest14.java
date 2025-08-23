package com.google.common.util.concurrent;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link AtomicDoubleArray#getAndAdd(int, double)}.
 */
class AtomicDoubleArrayGetAndAddTest {

    private static final int ARRAY_SIZE = 10;
    private static final int FIRST_INDEX = 0;
    private static final int LAST_INDEX = ARRAY_SIZE - 1;

    /**
     * A representative set of double values to test edge cases, including
     * zero, infinities, NaN, and standard numbers.
     */
    private static final double[] TEST_VALUES = {
        +0.0, -0.0, 1.0, -1.0, Math.PI,
        Double.MAX_VALUE, Double.MIN_VALUE,
        Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY,
        Double.NaN
    };

    /**
     * Provides test cases by creating a cartesian product of indices,
     * initial values, and deltas. This ensures comprehensive testing across
     * various numeric scenarios and array boundaries.
     */
    private static Stream<Arguments> getAndAddTestCases() {
        return Stream.of(FIRST_INDEX, LAST_INDEX)
            .flatMap(index -> Stream.of(TEST_VALUES)
                .flatMap(initialValue -> Stream.of(TEST_VALUES)
                    .map(delta -> Arguments.of(index, initialValue, delta))));
    }

    @DisplayName("getAndAdd() should return the old value and atomically add the delta")
    @ParameterizedTest(name = "at index {0}, for initial={1} and delta={2}")
    @MethodSource("getAndAddTestCases")
    void getAndAdd_returnsOldValueAndAddsDelta(int index, double initialValue, double delta) {
        // Arrange
        AtomicDoubleArray atomicArray = new AtomicDoubleArray(ARRAY_SIZE);
        atomicArray.set(index, initialValue);
        double expectedNewValue = initialValue + delta;

        // Act
        double returnedValue = atomicArray.getAndAdd(index, delta);

        // Assert
        // The core contract of AtomicDoubleArray is to compare values by their bit representation.
        assertBitEquals(
            initialValue,
            returnedValue,
            "getAndAdd should return the value before the addition.");

        assertBitEquals(
            expectedNewValue,
            atomicArray.get(index),
            "The value at the index should be updated to the sum.");
    }

    /**
     * Asserts that two double values are bit-wise equal, which is the
     * equality contract for AtomicDoubleArray.
     */
    private static void assertBitEquals(double expected, double actual, String message) {
        assertEquals(Double.doubleToRawLongBits(expected), Double.doubleToRawLongBits(actual), message);
    }
}