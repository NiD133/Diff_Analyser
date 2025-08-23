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

/**
 * Tests for the {@link CachedDateTimeZone} class.
 * This test suite focuses on verifying the behavior of the caching time zone wrapper.
 */
public class CachedDateTimeZoneTest {

    private DateTimeZone originalDefaultZone;

    /**
     * Saves the original default DateTimeZone and sets a fixed one (UTC) for the test.
     * This ensures that test outcomes are consistent and not dependent on the execution environment's
     * time zone settings, which is crucial for reliable date-time tests.
     */
    @Before
    public void setUp() {
        originalDefaultZone = DateTimeZone.getDefault();
        DateTimeZone.setDefault(DateTimeZone.UTC);
    }

    /**
     * Restores the original default DateTimeZone after the test completes.
     * This prevents tests from having side effects that could interfere with other tests.
     */
    @After
    public void tearDown() {
        DateTimeZone.setDefault(originalDefaultZone);
    }

    /**
     * Verifies that a CachedDateTimeZone instance can be serialized and then deserialized
     * back into an object that is equal to the original.
     */
    @Test
    public void serializationRoundTrip_retainsEquality() throws Exception {
        // Arrange: Create a CachedDateTimeZone for a complex, non-fixed time zone.
        DateTimeZone parisZone = DateTimeZone.forID("Europe/Paris");
        CachedDateTimeZone originalCachedZone = CachedDateTimeZone.forZone(parisZone);

        // Act: Serialize the object to a byte array and deserialize it back into a new object.
        byte[] serializedBytes;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(originalCachedZone);
            serializedBytes = baos.toByteArray();
        }

        CachedDateTimeZone deserializedCachedZone;
        try (ByteArrayInputStream bais = new ByteArrayInputStream(serializedBytes);
             ObjectInputStream ois = new ObjectInputStream(bais)) {
            deserializedCachedZone = (CachedDateTimeZone) ois.readObject();
        }

        // Assert: The deserialized zone should be equal to the original.
        assertEquals("Deserialized object should be equal to the original", originalCachedZone, deserializedCachedZone);
    }
}