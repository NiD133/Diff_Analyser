package org.jfree.chart.axis;

import java.util.TimeZone;

import org.jfree.chart.TestUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link QuarterDateFormat} class.
 */
public class QuarterDateFormatTest {

    /**
     * Tests that the equals method correctly identifies equal and unequal instances.
     */
    @Test
    public void testEqualsMethod() {
        // Test equality with identical time zone and quarter symbols
        QuarterDateFormat qf1 = createQuarterDateFormat("GMT", "1", "2", "3", "4");
        QuarterDateFormat qf2 = createQuarterDateFormat("GMT", "1", "2", "3", "4");
        assertEquals(qf1, qf2, "Instances with the same time zone and symbols should be equal");

        // Test inequality with different time zones
        qf1 = createQuarterDateFormat("PST", "1", "2", "3", "4");
        assertNotEquals(qf1, qf2, "Instances with different time zones should not be equal");

        // Test equality with identical time zone and quarter symbols
        qf2 = createQuarterDateFormat("PST", "1", "2", "3", "4");
        assertEquals(qf1, qf2, "Instances with the same time zone and symbols should be equal");

        // Test inequality with different quarter symbols
        qf1 = createQuarterDateFormat("PST", "A", "2", "3", "4");
        assertNotEquals(qf1, qf2, "Instances with different quarter symbols should not be equal");

        // Test equality with identical time zone and quarter symbols
        qf2 = createQuarterDateFormat("PST", "A", "2", "3", "4");
        assertEquals(qf1, qf2, "Instances with the same time zone and symbols should be equal");

        // Test inequality with different quarterFirst flag
        qf1 = new QuarterDateFormat(TimeZone.getTimeZone("PST"), new String[]{"A", "2", "3", "4"}, true);
        assertNotEquals(qf1, qf2, "Instances with different quarterFirst flags should not be equal");

        // Test equality with identical time zone, quarter symbols, and quarterFirst flag
        qf2 = new QuarterDateFormat(TimeZone.getTimeZone("PST"), new String[]{"A", "2", "3", "4"}, true);
        assertEquals(qf1, qf2, "Instances with the same time zone, symbols, and quarterFirst flag should be equal");
    }

    /**
     * Tests that equal objects have the same hash code.
     */
    @Test
    public void testHashCodeConsistency() {
        QuarterDateFormat qf1 = createQuarterDateFormat("GMT", "1", "2", "3", "4");
        QuarterDateFormat qf2 = createQuarterDateFormat("GMT", "1", "2", "3", "4");
        assertEquals(qf1.hashCode(), qf2.hashCode(), "Equal instances should have the same hash code");
    }

    /**
     * Tests that cloning creates a distinct but equal object.
     */
    @Test
    public void testCloningFunctionality() {
        QuarterDateFormat original = createQuarterDateFormat("GMT", "1", "2", "3", "4");
        QuarterDateFormat clone = (QuarterDateFormat) original.clone();
        assertNotSame(original, clone, "Clone should be a different instance");
        assertEquals(original, clone, "Clone should be equal to the original");
    }

    /**
     * Tests that serialization and deserialization preserve object equality.
     */
    @Test
    public void testSerializationIntegrity() {
        QuarterDateFormat original = createQuarterDateFormat("GMT", "1", "2", "3", "4");
        QuarterDateFormat deserialized = TestUtils.serialised(original);
        assertEquals(original, deserialized, "Deserialized instance should be equal to the original");
    }

    /**
     * Helper method to create a QuarterDateFormat instance with specified time zone and quarter symbols.
     *
     * @param timeZoneId the ID of the time zone
     * @param quarters the quarter symbols
     * @return a new QuarterDateFormat instance
     */
    private QuarterDateFormat createQuarterDateFormat(String timeZoneId, String... quarters) {
        return new QuarterDateFormat(TimeZone.getTimeZone(timeZoneId), quarters);
    }
}