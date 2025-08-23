package org.joda.time.chrono;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.joda.time.DateTimeZone;
import org.junit.Test;

/**
 * Unit tests for the EthiopicChronology factory methods.
 */
public class EthiopicChronologyTest {

    // The test focuses on the static factory method getInstanceUTC(), which does not
    // depend on any instance state or global configuration like default time zones or locales.
    // Therefore, the complex setUp() and tearDown() methods and numerous fields from the
    // original test class have been removed to improve clarity and focus.

    @Test
    public void getInstanceUTC_shouldReturnChronologyWithUTCZone() {
        // Arrange: No setup is needed for this static factory method.
        
        // Act: Get an instance of the EthiopicChronology in UTC.
        EthiopicChronology chronology = EthiopicChronology.getInstanceUTC();

        // Assert: The chronology's time zone should be UTC.
        assertEquals(DateTimeZone.UTC, chronology.getZone());
    }

    @Test
    public void getInstanceUTC_shouldReturnSingletonInstance() {
        // Arrange: No setup is needed.
        
        // Act: Call the factory method multiple times.
        EthiopicChronology instance1 = EthiopicChronology.getInstanceUTC();
        EthiopicChronology instance2 = EthiopicChronology.getInstanceUTC();

        // Assert: The factory method should always return the same cached instance.
        // This is a stronger and more precise check than verifying the class type.
        assertSame("The UTC instance of EthiopicChronology should be a singleton", instance1, instance2);
    }
}