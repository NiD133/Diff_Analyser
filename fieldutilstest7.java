package org.joda.time.field;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for the safeDivide(long, long, RoundingMode) method in FieldUtils.
 * This class is structured to test each rounding mode and exceptional case separately for clarity.
 */
@DisplayName("FieldUtils.safeDivide(long, long, RoundingMode)")
class FieldUtilsSafeDivideTest {

    @Test
    void safeDivide_withUnnecessaryRounding_shouldReturnExactResultForExactDivision() {
        // 15 is perfectly divisible by 5.
        assertEquals(3L, FieldUtils.safeDivide(15L, 5L, RoundingMode.UNNECESSARY));
    }

    @Test
    void safeDivide_withFloorRounding_shouldRoundTowardsNegativeInfinity() {
        // 179 / 3 = 59.66... -> floor is 59
        assertEquals(59L, FieldUtils.safeDivide(179L, 3L, RoundingMode.FLOOR));
        // -179 / 3 = -59.66... -> floor is -60
        assertEquals(-60L, FieldUtils.safeDivide(-179L, 3L, RoundingMode.FLOOR));
    }

    @Test
    void safeDivide_withCeilingRounding_shouldRoundTowardsPositiveInfinity() {
        // 179 / 3 = 59.66... -> ceiling is 60
        assertEquals(60L, FieldUtils.safeDivide(179L, 3L, RoundingMode.CEILING));
        // -179 / 3 = -59.66... -> ceiling is -59
        assertEquals(-59L, FieldUtils.safeDivide(-179L, 3L, RoundingMode.CEILING));
    }

    @Test
    void safeDivide_withHalfUpRounding_shouldRoundToNearestNeighborAwayFromZero() {
        // 179 / 3 = 59.66... (fraction > 0.5) -> rounds up to 60
        assertEquals(60L, FieldUtils.safeDivide(179L, 3L, RoundingMode.HALF_UP));
        // -179 / 3 = -59.66... (fraction > 0.5) -> rounds away from zero to -60
        assertEquals(-60L, FieldUtils.safeDivide(-179L, 3L, RoundingMode.HALF_UP));
        // 15 / 2 = 7.5 (midpoint) -> rounds away from zero to 8
        assertEquals(8L, FieldUtils.safeDivide(15L, 2L, RoundingMode.HALF_UP));
    }

    @Test
    void safeDivide_withHalfDownRounding_shouldRoundToNearestNeighborTowardsZero() {
        // 179 / 3 = 59.66... (fraction > 0.5) -> rounds up to 60
        assertEquals(60L, FieldUtils.safeDivide(179L, 3L, RoundingMode.HALF_DOWN));
        // -179 / 3 = -59.66... (fraction > 0.5) -> rounds away from zero to -60
        assertEquals(-60L, FieldUtils.safeDivide(-179L, 3L, RoundingMode.HALF_DOWN));
        // 15 / 2 = 7.5 (midpoint) -> rounds towards zero to 7
        assertEquals(7L, FieldUtils.safeDivide(15L, 2L, RoundingMode.HALF_DOWN));
    }

    @Nested
    @DisplayName("Exceptional Cases")
    class ExceptionalCases {

        @Test
        void safeDivide_whenDividingByZero_shouldThrowArithmeticException() {
            assertThrows(ArithmeticException.class, () -> {
                FieldUtils.safeDivide(1L, 0L, RoundingMode.UNNECESSARY);
            });
        }

        @Test
        void safeDivide_whenDividingMinLongByMinusOne_shouldThrowArithmeticException() {
            // This operation overflows because -Long.MIN_VALUE > Long.MAX_VALUE
            assertThrows(ArithmeticException.class, () -> {
                FieldUtils.safeDivide(Long.MIN_VALUE, -1L, RoundingMode.UNNECESSARY);
            });
        }
    }
    
    @Nested
    @DisplayName("Edge Value Cases")
    class EdgeValueCases {

        @Test
        void safeDivide_withMaxLong_shouldReturnCorrectResult() {
            assertEquals(Long.MAX_VALUE, FieldUtils.safeDivide(Long.MAX_VALUE, 1L, RoundingMode.UNNECESSARY));
            assertEquals(-Long.MAX_VALUE, FieldUtils.safeDivide(Long.MAX_VALUE, -1L, RoundingMode.UNNECESSARY));
        }

        @Test
        void safeDivide_withMinLong_shouldReturnCorrectResult() {
            assertEquals(Long.MIN_VALUE, FieldUtils.safeDivide(Long.MIN_VALUE, 1L, RoundingMode.UNNECESSARY));
        }
    }
}