package org.joda.time.tz;

import org.joda.time.DateTimeZone;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * Tests for the {@link CachedDateTimeZone} class.
 * <p>
 * This class focuses on verifying the caching behavior of DateTimeZone instances.
 */
public class CachedDateTimeZoneTest {

    private DateTimeZone originalDateTimeZone;

    @Before
    public void setup() {
        // Store the original default timezone to restore it later
        originalDateTimeZone = DateTimeZone.getDefault();
        // Set the default timezone to UTC for test consistency
        DateTimeZone.setDefault(DateTimeZone.UTC);
    }

    @After
    public void tearDown() {
        // Restore the original default timezone
        DateTimeZone.setDefault(originalDateTimeZone);
    }

    @Test
    public void testCaching_sameZoneIdReturnsSameInstance() {
        // Arrange: Get two CachedDateTimeZone instances for the same time zone ID.
        String zoneId = "Europe/Paris";
        CachedDateTimeZone zone1 = CachedDateTimeZone.forZone(DateTimeZone.forID(zoneId));
        CachedDateTimeZone zone2 = CachedDateTimeZone.forZone(DateTimeZone.forID(zoneId));

        // Assert: Verify that the two instances are the same object (cached).
        assertSame("CachedDateTimeZone.forZone should return the same instance for the same zone id", zone1, zone2);
    }

    @Test
    public void testSerialization_deserializedInstanceEqualsOriginal() throws Exception {
        // Arrange: Create a CachedDateTimeZone for a specific time zone.
        DateTimeZone originalZone = DateTimeZone.forID("Europe/Paris");
        CachedDateTimeZone test = CachedDateTimeZone.forZone(originalZone);

        // Act: Serialize and deserialize the CachedDateTimeZone.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(test);
        oos.close();
        byte[] bytes = baos.toByteArray();

        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bais);
        CachedDateTimeZone result = (CachedDateTimeZone) ois.readObject();
        ois.close();

        // Assert: Verify that the deserialized object is equal to the original.
        assertEquals("The deserialized CachedDateTimeZone should be equal to the original", test, result);
    }
}