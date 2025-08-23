package org.threeten.extra;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.Serializable;
import java.lang.Comparable;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAdjuster;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests the interface contracts of the {@link DayOfYear} class.
 *
 * This test class verifies that DayOfYear correctly implements the essential
 * interfaces that define its role as a value-based, comparable, and
 * serializable temporal object.
 */
class DayOfYearInterfaceTest {

    @Test
    @DisplayName("DayOfYear should implement its fundamental interfaces")
    void implementsFundamentalInterfaces() {
        // A DayOfYear should be a value type, usable within the java.time framework,
        // comparable, and serializable. This test verifies these characteristics
        // by checking the interfaces it implements.
        Class<DayOfYear> dayOfYearClass = DayOfYear.class;

        assertTrue(Comparable.class.isAssignableFrom(dayOfYearClass),
                "DayOfYear should be comparable to other DayOfYear instances.");

        assertTrue(TemporalAccessor.class.isAssignableFrom(dayOfYearClass),
                "DayOfYear should be accessible as a temporal entity.");

        assertTrue(TemporalAdjuster.class.isAssignableFrom(dayOfYearClass),
                "DayOfYear should be able to adjust other temporal objects.");

        assertTrue(Serializable.class.isAssignableFrom(dayOfYearClass),
                "DayOfYear should be serializable for persistence and data transfer.");
    }
}