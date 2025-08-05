package org.jfree.chart.axis;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;
import java.util.stream.Stream;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.*;

/**
 * A suite of modern, understandable tests for the {@link QuarterDateFormat} class.
 */
class QuarterDateFormatTest {

    private static final TimeZone GMT = TimeZone.getTimeZone("GMT");

    /**
     * Creates a {@link Date} object representing the first day of a given quarter in a given year.
     *
     * @param year    The year.
     * @param quarter The quarter (1-4).
     * @return A date instance.
     */
    private Date createDate(int year, int quarter) {
        Calendar calendar = Calendar.getInstance(GMT);
        // Quarters map to months: Q1->Jan(0), Q2->Apr(3), Q3->Jul(6), Q4->Oct(9)
        int month = (quarter - 1) * 3;
        calendar.set(year, month, 1);
        return calendar.getTime();
    }

    @Nested
    @DisplayName("Constructor")
    class ConstructorTests {

        @Test
        @DisplayName("should use default time zone and regular quarters when no arguments are provided")
        void constructor_default() {
            // Act
            QuarterDateFormat formatter = new QuarterDateFormat();

            // Assert
            assertEquals(TimeZone.getDefault(), formatter.getTimeZone());
            // We cannot directly access the quarter symbols, but we can verify the format
            assertEquals("2023 2", formatter.format(createDate(2023, 2)));
        }

        // Groups the original tests for null time zone into one.
        @DisplayName("should throw IllegalArgumentException for null TimeZone")
        @ParameterizedTest(name = "with constructor: {0}")
        @MethodSource("org.jfree.chart.axis.QuarterDateFormatTest#constructorCallProvider")
        void constructor_shouldThrowException_forNullTimeZone(ConstructorCall constructorCall) {
            // Act & Assert
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, constructorCall::execute);
            assertTrue(exception.getMessage().contains("Null 'zone' argument."));
        }
    }

    @Nested
    @DisplayName("format")
    class FormatTests {

        @Test
        @DisplayName("should format a date as 'YYYY Q' by default")
        void format_shouldUseYearThenQuarterFormat_byDefault() {
            // Arrange
            QuarterDateFormat formatter = new QuarterDateFormat(GMT);
            Date dateInQ2 = createDate(2023, 2);

            // Act
            String result = formatter.format(dateInQ2);

            // Assert
            assertEquals("2023 2", result);
        }

        @Test
        @DisplayName("should format a date using Roman numerals when provided")
        void format_shouldUseRomanNumerals_whenProvided() {
            // Arrange
            QuarterDateFormat formatter = new QuarterDateFormat(GMT, QuarterDateFormat.ROMAN_QUARTERS);
            Date dateInQ4 = createDate(1999, 4);

            // Act
            String result = formatter.format(dateInQ4);

            // Assert
            assertEquals("1999 IV", result);
        }



        @Test
        @DisplayName("should place the quarter first when 'quarterFirst' flag is true")
        void format_shouldPlaceQuarterFirst_whenQuarterFirstIsTrue() {
            // Arrange
            QuarterDateFormat formatter = new QuarterDateFormat(GMT, QuarterDateFormat.ROMAN_QUARTERS, true);
            Date dateInQ1 = createDate(2025, 1);

            // Act
            String result = formatter.format(dateInQ1);

            // Assert
            assertEquals("I 2025", result);
        }

        @Test
        @DisplayName("should throw NullPointerException for a null StringBuffer")
        void format_shouldThrowException_forNullStringBuffer() {
            // Arrange
            QuarterDateFormat formatter = new QuarterDateFormat(GMT);
            Date date = new Date();
            FieldPosition position = new FieldPosition(0);

            // Act & Assert
            assertThrows(NullPointerException.class, () -> {
                formatter.format(date, null, position);
            });
        }

        @Test
        @DisplayName("should throw ArrayIndexOutOfBoundsException if quarter symbols array is too small")
        void format_shouldThrowException_whenFormattingWithInsufficientQuarterSymbols() {
            // Arrange
            // Create a formatter with symbols only for Q1.
            String[] oneQuarter = {"Q1"};
            QuarterDateFormat formatter = new QuarterDateFormat(GMT, oneQuarter);
            Date dateInQ2 = createDate(2023, 2); // A date in the second quarter.
            StringBuffer buffer = new StringBuffer();
            FieldPosition position = new FieldPosition(0);

            // Act & Assert
            assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
                formatter.format(dateInQ2, buffer, position);
            });
        }
    }

    @Nested
    @DisplayName("parse")
    class ParseTests {
        @Test
        @DisplayName("should always return null as it is not implemented")
        void parse_shouldAlwaysReturnNull() {
            // Arrange
            QuarterDateFormat formatter = new QuarterDateFormat();
            ParsePosition position = new ParsePosition(0);

            // Act
            Date result = formatter.parse("2023 Q1", position);

            // Assert
            assertNull(result);
        }
    }

    @Nested
    @DisplayName("equals and hashCode")
    class EqualsAndHashCodeTests {

        private QuarterDateFormat formatterA;
        private QuarterDateFormat formatterB;

        @BeforeEach
        void setUp() {
            formatterA = new QuarterDateFormat(GMT, QuarterDateFormat.REGULAR_QUARTERS, false);
            formatterB = new QuarterDateFormat(GMT, QuarterDateFormat.REGULAR_QUARTERS, false);
        }

        @Test
        @DisplayName("equals should return true for the same instance")
        void equals_shouldReturnTrue_forSameInstance() {
            assertTrue(formatterA.equals(formatterA));
        }

        @Test
        @DisplayName("equals should return true for identical instances")
        void equals_shouldReturnTrue_forIdenticalInstances() {
            assertTrue(formatterA.equals(formatterB));
        }

        @Test
        @DisplayName("equals should return false for null")
        void equals_shouldReturnFalse_forNull() {
            assertFalse(formatterA.equals(null));
        }

        @Test
        @DisplayName("equals should return false for a different class")
        void equals_shouldReturnFalse_forDifferentClass() {
            assertFalse(formatterA.equals("a string"));
        }

        @Test
        @DisplayName("equals should return false when time zones differ")
        void equals_shouldReturnFalse_whenTimeZoneDiffers() {
            // Arrange
            formatterB = new QuarterDateFormat(TimeZone.getTimeZone("PST"), QuarterDateFormat.REGULAR_QUARTERS, false);

            // Assert
            assertFalse(formatterA.equals(formatterB));
        }

        @Test
        @DisplayName("equals should return false when quarter symbols differ")
        void equals_shouldReturnFalse_whenQuarterSymbolsDiffer() {
            // Arrange
            formatterB = new QuarterDateFormat(GMT, QuarterDateFormat.ROMAN_QUARTERS, false);

            // Assert
            assertFalse(formatterA.equals(formatterB));
        }

        @Test
        @DisplayName("equals should return false when 'quarterFirst' flag differs")
        void equals_shouldReturnFalse_whenQuarterFirstFlagDiffers() {
            // Arrange
            formatterB = new QuarterDateFormat(GMT, QuarterDateFormat.REGULAR_QUARTERS, true);

            // Assert
            assertFalse(formatterA.equals(formatterB));
        }

        @Test
        @DisplayName("hashCode should be the same for equal objects")
        void hashCode_shouldBeEqual_forEqualObjects() {
            assertEquals(formatterA.hashCode(), formatterB.hashCode());
        }
    }

    @Nested
    @DisplayName("clone")
    class CloneTests {
        @Test
        @DisplayName("should produce an independent and equal copy")
        void clone_shouldProduceIndependentAndEqualCopy() throws CloneNotSupportedException {
            // Arrange
            QuarterDateFormat original = new QuarterDateFormat(GMT, QuarterDateFormat.ROMAN_QUARTERS, true);

            // Act
            QuarterDateFormat clone = (QuarterDateFormat) original.clone();

            // Assert
            assertNotSame(original, clone, "Clone should be a different instance.");
            assertEquals(original, clone, "Clone should be equal to the original.");
        }
    }

    // Helper functional interface for parameterizing constructor calls
    @FunctionalInterface
    interface ConstructorCall {
        void execute();
    }

    // Provides different constructor calls that should fail with a null TimeZone
    static Stream<ConstructorCall> constructorCallProvider() {
        return Stream.of(
                () -> new QuarterDateFormat(null),
                () -> new QuarterDateFormat(null, QuarterDateFormat.REGULAR_QUARTERS),
                () -> new QuarterDateFormat(null, QuarterDateFormat.REGULAR_QUARTERS, false)
        );
    }
}