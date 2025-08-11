/*
 *  Copyright 2001-2012 Stephen Colebourne
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
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
 * Unit tests for {@link CachedDateTimeZone}.
 */
public class TestCachedDateTimeZone {

    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");

    private DateTimeZone originalSystemZone;

    @Before
    public void setUp() {
        // Preserve the original system default time zone
        originalSystemZone = DateTimeZone.getDefault();
        // Set a known, fixed default time zone for test consistency
        DateTimeZone.setDefault(DateTimeZone.UTC);
    }

    @After
    public void tearDown() {
        // Restore the original system default time zone
        DateTimeZone.setDefault(originalSystemZone);
    }

    @Test
    public void forZone_whenCalledMultipleTimesForSameZone_shouldReturnSameInstance() {
        // Arrange: The main arrangement is the PARIS constant.

        // Act: Request a cached zone for the same underlying zone twice.
        CachedDateTimeZone zone1 = CachedDateTimeZone.forZone(PARIS);
        CachedDateTimeZone zone2 = CachedDateTimeZone.forZone(PARIS);

        // Assert: The factory method should return the exact same cached instance.
        assertSame("Expected the same cached instance for the same time zone", zone1, zone2);
    }

    @Test
    public void serialization_shouldPreserveZoneEquality() throws Exception {
        // Arrange: Create an original zone to be serialized.
        CachedDateTimeZone originalZone = CachedDateTimeZone.forZone(PARIS);

        // Act: Serialize the zone to bytes, then deserialize it back to an object.
        byte[] serializedBytes = serialize(originalZone);
        CachedDateTimeZone deserializedZone = deserialize(serializedBytes);

        // Assert: The deserialized zone should be logically equal to the original.
        assertEquals("Deserialized zone should be equal to the original", originalZone, deserializedZone);
        
        // Further Assert: The underlying, uncached zone should be the same canonical instance.
        // This confirms that DateTimeZone's readResolve() method worked as expected.
        assertSame(
            "Underlying uncached zones should be the same instance after deserialization",
            originalZone.getUncachedZone(),
            deserializedZone.getUncachedZone()
        );
    }

    /** Helper method to serialize an object to a byte array. */
    private byte[] serialize(Object obj) throws java.io.IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(obj);
            return baos.toByteArray();
        }
    }

    /** Helper method to deserialize a byte array to a CachedDateTimeZone. */
    private CachedDateTimeZone deserialize(byte[] bytes) throws java.io.IOException, ClassNotFoundException {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
             ObjectInputStream ois = new ObjectInputStream(bais)) {
            return (CachedDateTimeZone) ois.readObject();
        }
    }
}