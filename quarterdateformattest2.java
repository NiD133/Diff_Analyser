package org.jfree.chart.axis;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.TimeZone;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for the {@link QuarterDateFormat} class.
 */
class QuarterDateFormatTest {

    /**
     * Verifies that two equal QuarterDateFormat instances have the same hash code,
     * fulfilling the Object.hashCode() contract.
     */
    @Test
    @DisplayName("Equal objects should have equal hash codes")
    void equalObjectsShouldHaveEqualHashCodes() {
        // Arrange: Create two identical formatters using the same timezone and quarter symbols.
        TimeZone gmt = TimeZone.getTimeZone("GMT");
        String[] quarterSymbols = QuarterDateFormat.REGULAR_QUARTERS;
        
        QuarterDateFormat formatter1 = new QuarterDateFormat(gmt, quarterSymbols);
        QuarterDateFormat formatter2 = new QuarterDateFormat(gmt, quarterSymbols);

        // Assert: First, confirm the objects are equal. This is a precondition for the hashCode contract.
        assertEquals(formatter1, formatter2, "Precondition failed: The two formatters should be equal.");

        // Assert: According to the hashCode contract, equal objects must have equal hash codes.
        assertEquals(formatter1.hashCode(), formatter2.hashCode(), "Equal objects must produce the same hash code.");
    }
}