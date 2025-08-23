package org.joda.time.tz;

import org.joda.time.DateTimeZone;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link CachedDateTimeZone} class.
 */
public class CachedDateTimeZoneTest {

    /**
     * Tests that a CachedDateTimeZone instance is equal to itself,
     * verifying the reflexive property of the equals method.
     */
    @Test
    public void equals_returnsTrue_whenComparedToItself() {
        // Arrange
        // Use a non-fixed, well-known time zone to ensure the test is deterministic.
        DateTimeZone underlyingZone = DateTimeZone.forID("Europe/London");
        CachedDateTimeZone cachedZone = CachedDateTimeZone.forZone(underlyingZone);

        // Act & Assert
        // According to the Java contract for Object.equals(), an object must be equal to itself.
        // Using assertEquals is idiomatic for testing this property.
        assertEquals(cachedZone, cachedZone);
    }
}