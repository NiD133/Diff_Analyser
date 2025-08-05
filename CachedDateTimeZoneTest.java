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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.joda.time.DateTimeZone;
import static org.junit.Assert.*;

public class TestCachedDateTimeZone {

    private DateTimeZone originalDateTimeZone;

    @Before
    public void setUp() {
        // Save original time zone to restore later
        originalDateTimeZone = DateTimeZone.getDefault();
        // Set UTC as default for consistent test environment
        DateTimeZone.setDefault(DateTimeZone.UTC);
    }

    @After
    public void tearDown() {
        // Restore original default time zone
        DateTimeZone.setDefault(originalDateTimeZone);
    }

    @Test
    public void forZone_returnsSameInstanceForSameZone() {
        // Verify caching behavior: requesting same zone returns cached instance
        CachedDateTimeZone zone1 = CachedDateTimeZone.forZone(DateTimeZone.forID("Europe/Paris"));
        CachedDateTimeZone zone2 = CachedDateTimeZone.forZone(DateTimeZone.forID("Europe/Paris"));
        
        assertSame("Subsequent requests for same zone should return cached instance", zone1, zone2);
    }

    @Test
    public void serialization_deserialization_producesEqualInstance() throws Exception {
        // Setup: Create zone instance to serialize
        CachedDateTimeZone originalZone = CachedDateTimeZone.forZone(DateTimeZone.forID("Europe/Paris"));
        
        // Serialization
        byte[] serializedBytes;
        try (ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
             ObjectOutputStream objectStream = new ObjectOutputStream(byteStream)) {
            objectStream.writeObject(originalZone);
            serializedBytes = byteStream.toByteArray();
        }
        
        // Deserialization
        CachedDateTimeZone deserializedZone;
        try (ByteArrayInputStream inputByteStream = new ByteArrayInputStream(serializedBytes);
             ObjectInputStream objectInputStream = new ObjectInputStream(inputByteStream)) {
            deserializedZone = (CachedDateTimeZone) objectInputStream.readObject();
        }
        
        // Verify deserialized instance matches original
        assertEquals("Deserialized instance should be equal to original", originalZone, deserializedZone);
    }
}