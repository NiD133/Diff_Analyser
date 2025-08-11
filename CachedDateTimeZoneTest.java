package org.joda.time.tz;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.joda.time.DateTimeZone;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Readable tests for CachedDateTimeZone.
 *
 * What these tests assert:
 * - forZone returns the same cached instance when called repeatedly for the same zone ID
 * - Serialization round-trip preserves equality
 * - getUncachedZone returns the original wrapped zone
 */
public class CachedDateTimeZoneTest {

    private static final String TZ_EUROPE_PARIS = "Europe/Paris";

    private DateTimeZone originalDefault;

    @Before
    public void setUp() {
        // Ensure deterministic behavior across environments
        originalDefault = DateTimeZone.getDefault();
        DateTimeZone.setDefault(DateTimeZone.UTC);
    }

    @After
    public void tearDown() {
        DateTimeZone.setDefault(originalDefault);
    }

    @Test
    public void forZoneReturnsSameInstanceForSameZoneId() {
        CachedDateTimeZone zone1 = CachedDateTimeZone.forZone(DateTimeZone.forID(TZ_EUROPE_PARIS));
        CachedDateTimeZone zone2 = CachedDateTimeZone.forZone(DateTimeZone.forID(TZ_EUROPE_PARIS));

        assertSame(
                "CachedDateTimeZone.forZone should return the same cached instance for the same underlying zone",
                zone1, zone2
        );
    }

    @Test
    public void serializationRoundTripPreservesEquality() throws Exception {
        CachedDateTimeZone original = CachedDateTimeZone.forZone(DateTimeZone.forID(TZ_EUROPE_PARIS));

        CachedDateTimeZone deserialized = serializeAndDeserialize(original);

        assertEquals(
                "A serialized/deserialized CachedDateTimeZone should be equal to the original",
                original, deserialized
        );
    }

    @Test
    public void getUncachedZoneReturnsOriginalZone() {
        DateTimeZone base = DateTimeZone.forID(TZ_EUROPE_PARIS);
        CachedDateTimeZone cached = CachedDateTimeZone.forZone(base);

        assertSame("getUncachedZone should return the wrapped zone", base, cached.getUncachedZone());
    }

    private static <T extends Serializable> T serializeAndDeserialize(T obj) throws Exception {
        byte[] bytes;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(obj);
            oos.flush();
            bytes = baos.toByteArray();
        }

        try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
             ObjectInputStream ois = new ObjectInputStream(bais)) {
            @SuppressWarnings("unchecked")
            T result = (T) ois.readObject();
            return result;
        }
    }
}