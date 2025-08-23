package org.joda.time.chrono;

import org.joda.time.Chronology;
import org.junit.Test;

import static org.junit.Assert.assertSame;

/**
 * Unit tests for {@link IslamicChronology}.
 */
public class IslamicChronologyTest {

    @Test
    public void withUTC_onAlreadyUtcInstance_returnsSameInstance() {
        // Arrange: Obtain the canonical UTC instance of IslamicChronology.
        // This is the specific instance that the withUTC() method is documented to return.
        IslamicChronology utcChronology = IslamicChronology.getInstanceUTC();

        // Act: Call the withUTC() method on the instance that is already in UTC.
        Chronology result = utcChronology.withUTC();

        // Assert: The method should return the exact same instance, confirming
        // the optimization that avoids creating a new object.
        assertSame("Calling withUTC() on a UTC-based chronology should return the same instance.",
                   utcChronology, result);
    }
}