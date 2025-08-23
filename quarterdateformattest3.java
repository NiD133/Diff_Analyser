package org.jfree.chart.axis;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the cloning functionality of the {@link QuarterDateFormat} class.
 */
class QuarterDateFormatTest {

    private QuarterDateFormat originalFormatter;

    @BeforeEach
    void setUp() {
        // Create a standard formatter instance to be used in tests.
        TimeZone gmt = TimeZone.getTimeZone("GMT");
        originalFormatter = new QuarterDateFormat(gmt, QuarterDateFormat.REGULAR_QUARTERS);
    }

    @Test
    @DisplayName("clone() should create an independent and equal copy")
    void cloneShouldCreateIndependentAndEqualCopy() {
        // Act: Clone the original formatter.
        QuarterDateFormat clonedFormatter = (QuarterDateFormat) originalFormatter.clone();

        // Assert: The clone should be a distinct object but logically equal to the original.
        // This verifies the core contract of the clone() method.
        assertNotSame(originalFormatter, clonedFormatter, "Clone should be a new object instance in memory.");
        assertEquals(originalFormatter, clonedFormatter, "Clone should be logically equal to the original.");
        
        // Additionally, verify that the clone is of the exact same class.
        assertSame(originalFormatter.getClass(), clonedFormatter.getClass(), "Clone should be of the same class as the original.");
    }
}