package org.joda.time;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for the {@link Weeks#dividedBy(int)} method.
 */
class WeeksDividedByTest {

    @Test
    @DisplayName("dividedBy should perform integer division correctly")
    void dividedBy_withVariousDivisors_returnsCorrectResult() {
        // Arrange
        Weeks twelveWeeks = Weeks.weeks(12);

        // Act & Assert
        assertEquals(Weeks.weeks(6), twelveWeeks.dividedBy(2));
        assertEquals(Weeks.weeks(4), twelveWeeks.dividedBy(3));
        assertEquals(Weeks.weeks(3), twelveWeeks.dividedBy(4));
        assertEquals(Weeks.weeks(2), twelveWeeks.dividedBy(5), "Integer division should truncate the result (12 / 5 = 2)");
        assertEquals(Weeks.weeks(2), twelveWeeks.dividedBy(6));
    }

    @Test
    @DisplayName("dividedBy should not modify the original Weeks object (immutability)")
    void dividedBy_isImmutable() {
        // Arrange
        Weeks originalWeeks = Weeks.weeks(12);
        int originalValue = originalWeeks.getWeeks();

        // Act
        originalWeeks.dividedBy(4); // Perform an operation

        // Assert
        assertEquals(originalValue, originalWeeks.getWeeks(), "Original Weeks object should not be modified.");
    }

    @Test
    @DisplayName("dividedBy(1) should return the same instance")
    void dividedBy_one_returnsSameInstance() {
        // Arrange
        Weeks twelveWeeks = Weeks.weeks(12);

        // Act
        Weeks result = twelveWeeks.dividedBy(1);

        // Assert
        assertSame(twelveWeeks, result, "Dividing by 1 is a special case that should return the same instance.");
    }

    @Test
    @DisplayName("dividedBy(0) should throw ArithmeticException")
    void dividedBy_zero_throwsArithmeticException() {
        // Arrange
        Weeks oneWeek = Weeks.ONE;

        // Act & Assert
        assertThrows(ArithmeticException.class, () -> {
            oneWeek.dividedBy(0);
        }, "Division by zero is not allowed and should throw an exception.");
    }
}