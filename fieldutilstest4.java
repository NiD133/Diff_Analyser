package org.joda.time.field;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for the {@link FieldUtils#safeMultiply(long, long)} method.
 */
@DisplayName("FieldUtils.safeMultiply(long, long)")
class FieldUtils_SafeMultiplyLongTest {

    @ParameterizedTest(name = "[{index}] {0} * {1} = {2}")
    @CsvSource({
            "0, 0, 0",
            "1, 1, 1",
            "1, 3, 3",
            "3, 1, 3",
            "2, 3, 6",
            "2, -3, -6",
            "-2, 3, -6",
            "-2, -3, 6"
    })
    @DisplayName("should return correct product for simple values")
    void safeMultiply_shouldReturnCorrectProductForSimpleValues(long val1, long val2, long expectedResult) {
        // Act & Assert
        assertEquals(expectedResult, FieldUtils.safeMultiply(val1, val2));
    }

    @Test
    @DisplayName("should correctly multiply boundary values by 1 or -1")
    void safeMultiply_shouldHandleBoundaryValuesWithIdentityAndNegation() {
        // Test multiplication by 1 (identity)
        assertEquals(Long.MAX_VALUE, FieldUtils.safeMultiply(Long.MAX_VALUE, 1L));
        assertEquals(Long.MIN_VALUE, FieldUtils.safeMultiply(Long.MIN_VALUE, 1L));

        // Test multiplication by -1 (negation)
        assertEquals(-Long.MAX_VALUE, FieldUtils.safeMultiply(Long.MAX_VALUE, -1L));
    }

    @Nested
    @DisplayName("overflow scenarios")
    class OverflowTests {

        @Test
        @DisplayName("should throw ArithmeticException when multiplying Long.MIN_VALUE by -1")
        void safeMultiply_throwsException_whenMultiplyingMinValueByNegativeOne() {
            // This specific case overflows because -Long.MIN_VALUE > Long.MAX_VALUE
            assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Long.MIN_VALUE, -1L));
            // Test commutativity
            assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(-1L, Long.MIN_VALUE));
        }

        @Test
        @DisplayName("should throw ArithmeticException when product exceeds Long.MAX_VALUE")
        void safeMultiply_throwsException_whenResultIsTooLarge() {
            assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Long.MAX_VALUE / 2 + 1, 2L));
        }

        @Test
        @DisplayName("should throw ArithmeticException when product is less than Long.MIN_VALUE")
        void safeMultiply_throwsException_whenResultIsTooSmall() {
            assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Long.MIN_VALUE / 2 - 1, 2L));
            assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Long.MAX_VALUE, Long.MIN_VALUE));
        }
    }
}