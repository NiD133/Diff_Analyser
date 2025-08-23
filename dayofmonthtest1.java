package org.threeten.extra;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.Serializable;
import java.lang.Comparable;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAdjuster;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link DayOfMonth} class, focusing on its type contracts and interface implementations.
 */
@DisplayName("DayOfMonth class structure")
class DayOfMonthTest {

    @Test
    @DisplayName("implements standard Java and java.time interfaces")
    void implementsStandardInterfaces() {
        // A value-based object should be comparable for sorting and ordering.
        assertTrue(Comparable.class.isAssignableFrom(DayOfMonth.class),
                "DayOfMonth should be Comparable.");

        // It should be serializable to allow for storage and transfer.
        assertTrue(Serializable.class.isAssignableFrom(DayOfMonth.class),
                "DayOfMonth should be Serializable.");

        // It should integrate with the java.time API as a temporal object.
        assertTrue(TemporalAccessor.class.isAssignableFrom(DayOfMonth.class),
                "DayOfMonth should be a TemporalAccessor.");
        assertTrue(TemporalAdjuster.class.isAssignableFrom(DayOfMonth.class),
                "DayOfMonth should be a TemporalAdjuster.");
    }
}