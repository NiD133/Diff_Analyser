package org.joda.time;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for the {@link Weeks} class.
 */
@DisplayName("Weeks")
class WeeksTest {

    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");

    @Nested
    @DisplayName("factory method weeksBetween(start, end)")
    class WeeksBetweenFactory {

        @Test
        @DisplayName("should calculate positive weeks when end instant is after start instant")
        void weeksBetween_calculatesPositiveWeeks_forForwardInterval() {
            // Arrange
            DateTime start = new DateTime(2006, 6, 9, 12, 0, 0, 0, PARIS);
            DateTime threeWeeksLater = new DateTime(2006, 6, 30, 12, 0, 0, 0, PARIS);
            DateTime sixWeeksLater = new DateTime(2006, 7, 21, 12, 0, 0, 0, PARIS);

            // Act
            Weeks threeWeeks = Weeks.weeksBetween(start, threeWeeksLater);
            Weeks sixWeeks = Weeks.weeksBetween(start, sixWeeksLater);

            // Assert
            assertThat(threeWeeks.getWeeks()).isEqualTo(3);
            assertThat(sixWeeks.getWeeks()).isEqualTo(6);
        }

        @Test
        @DisplayName("should calculate zero weeks when start and end instants are identical")
        void weeksBetween_calculatesZeroWeeks_forSameInstant() {
            // Arrange
            DateTime instant = new DateTime(2006, 6, 9, 12, 0, 0, 0, PARIS);

            // Act
            Weeks result = Weeks.weeksBetween(instant, instant);

            // Assert
            assertThat(result.getWeeks()).isEqualTo(0);
            // We can also assert that it returns the singleton instance for ZERO
            assertThat(result).isSameAs(Weeks.ZERO);
        }

        @Test
        @DisplayName("should calculate negative weeks when end instant is before start instant")
        void weeksBetween_calculatesNegativeWeeks_forBackwardInterval() {
            // Arrange
            DateTime start = new DateTime(2006, 6, 30, 12, 0, 0, 0, PARIS);
            DateTime end = new DateTime(2006, 6, 9, 12, 0, 0, 0, PARIS);

            // Act
            Weeks result = Weeks.weeksBetween(start, end);

            // Assert
            assertThat(result.getWeeks()).isEqualTo(-3);
        }
    }
}