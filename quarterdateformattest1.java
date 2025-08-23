package org.jfree.chart.axis;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the equals() method in the QuarterDateFormat class.
 */
@DisplayName("QuarterDateFormat.equals()")
class QuarterDateFormatTestTest1 {

    private static final TimeZone GMT = TimeZone.getTimeZone("GMT");
    private static final TimeZone PST = TimeZone.getTimeZone("PST");
    private static final String[] DEFAULT_QUARTERS = new String[]{"1", "2", "3", "4"};
    private static final String[] ROMAN_QUARTERS = new String[]{"I", "II", "III", "IV"};

    private QuarterDateFormat baseFormat;

    @BeforeEach
    void setUp() {
        baseFormat = new QuarterDateFormat(GMT, DEFAULT_QUARTERS, false);
    }

    @Test
    @DisplayName("should be reflexive")
    void anObjectShouldBeEqualToItself() {
        assertEquals(baseFormat, baseFormat);
    }

    @Test
    @DisplayName("should return true for two identical instances")
    void identicalObjectsShouldBeEqual() {
        // Arrange
        QuarterDateFormat identicalFormat = new QuarterDateFormat(GMT, DEFAULT_QUARTERS, false);

        // Assert
        assertEquals(baseFormat, identicalFormat);
        assertEquals(baseFormat.hashCode(), identicalFormat.hashCode());
    }

    @Test
    @DisplayName("should return false when compared to null")
    void anObjectShouldNotBeEqualToNull() {
        assertNotEquals(null, baseFormat);
    }

    @Test
    @DisplayName("should return false when compared to an object of a different type")
    void shouldNotBeEqualToDifferentType() {
        assertNotEquals("a string", baseFormat);
    }

    @Test
    @DisplayName("should return false for different time zones")
    void objectsWithDifferentTimeZonesShouldNotBeEqual() {
        // Arrange
        QuarterDateFormat differentTimeZoneFormat = new QuarterDateFormat(PST, DEFAULT_QUARTERS, false);

        // Assert
        assertNotEquals(baseFormat, differentTimeZoneFormat);
    }

    @Test
    @DisplayName("should return false for different quarter symbols")
    void objectsWithDifferentQuarterSymbolsShouldNotBeEqual() {
        // Arrange
        QuarterDateFormat differentSymbolsFormat = new QuarterDateFormat(GMT, ROMAN_QUARTERS, false);

        // Assert
        assertNotEquals(baseFormat, differentSymbolsFormat);
    }

    @Test
    @DisplayName("should return false for different 'quarterFirst' flags")
    void objectsWithDifferentQuarterFirstFlagsShouldNotBeEqual() {
        // Arrange
        QuarterDateFormat quarterFirstFormat = new QuarterDateFormat(GMT, DEFAULT_QUARTERS, true);

        // Assert
        assertNotEquals(baseFormat, quarterFirstFormat);
    }
}