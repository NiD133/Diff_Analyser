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

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.joda.time.DateTimeZone;

/**
 * Test cases for CachedDateTimeZone.
 *
 * Tests the caching behavior and serialization functionality of CachedDateTimeZone,
 * which wraps DateTimeZone instances to improve performance through caching.
 *
 * @author Stephen Colebourne
 */
public class TestCachedDateTimeZone extends TestCase {

    // Test timezone ID used throughout the tests
    private static final String PARIS_TIMEZONE_ID = "Europe/Paris";

    // Stores the original default timezone to restore after each test
    private DateTimeZone originalDefaultTimezone = null;

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestCachedDateTimeZone.class);
    }

    public TestCachedDateTimeZone(String name) {
        super(name);
    }

    /**
     * Set up test environment by saving the current default timezone 
     * and setting UTC as default to ensure consistent test behavior.
     */
    @Override
    protected void setUp() throws Exception {
        originalDefaultTimezone = DateTimeZone.getDefault();
        DateTimeZone.setDefault(DateTimeZone.UTC);
    }

    /**
     * Clean up test environment by restoring the original default timezone.
     */
    @Override
    protected void tearDown() throws Exception {
        DateTimeZone.setDefault(originalDefaultTimezone);
    }

    /**
     * Test that CachedDateTimeZone.forZone() returns the same cached instance 
     * when called multiple times with the same timezone ID.
     *
     * This verifies the core caching functionality - that identical timezone
     * requests reuse the same CachedDateTimeZone instance rather than creating
     * new ones, which improves memory usage and performance.
     */
    public void testForZone_returnsSameCachedInstanceForIdenticalTimezones() throws Exception {
        // Given: Two requests for the same timezone (Paris)
        DateTimeZone parisTimezone = DateTimeZone.forID(PARIS_TIMEZONE_ID);

        // When: Creating cached timezone instances for the same underlying zone
        CachedDateTimeZone firstCachedTimezone = CachedDateTimeZone.forZone(parisTimezone);
        CachedDateTimeZone secondCachedTimezone = CachedDateTimeZone.forZone(parisTimezone);

        // Then: Both calls should return the exact same cached instance
        assertSame("CachedDateTimeZone.forZone() should return the same instance for identical timezones",
                firstCachedTimezone, secondCachedTimezone);
    }

    /**
     * Test that CachedDateTimeZone instances can be properly serialized and deserialized,
     * maintaining equality after the round-trip process.
     *
     * This ensures that cached timezone objects can be stored/transmitted and reconstructed
     * correctly, which is important for distributed systems and persistence scenarios.
     */
    public void testSerialization_maintainsEqualityAfterRoundTrip() throws Exception {
        // Given: A cached timezone instance for Paris
        DateTimeZone parisTimezone = DateTimeZone.forID(PARIS_TIMEZONE_ID);
        CachedDateTimeZone originalCachedTimezone = CachedDateTimeZone.forZone(parisTimezone);

        // When: Serializing the cached timezone to bytes
        byte[] serializedBytes = serializeToBytes(originalCachedTimezone);

        // And: Deserializing back to an object
        CachedDateTimeZone deserializedCachedTimezone = deserializeFromBytes(serializedBytes);

        // Then: The deserialized instance should be equal to the original
        assertEquals("Deserialized CachedDateTimeZone should be equal to the original",
                originalCachedTimezone, deserializedCachedTimezone);
    }

    /**
     * Helper method to serialize a CachedDateTimeZone instance to a byte array.
     *
     * @param cachedTimezone The CachedDateTimeZone instance to serialize
     * @return Byte array containing the serialized object
     * @throws Exception If serialization fails
     */
    private byte[] serializeToBytes(CachedDateTimeZone cachedTimezone) throws Exception {
        ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteOutputStream);

        try {
            objectOutputStream.writeObject(cachedTimezone);
            objectOutputStream.flush();
            return byteOutputStream.toByteArray();
        } finally {
            objectOutputStream.close();
        }
    }

    /**
     * Helper method to deserialize a CachedDateTimeZone instance from a byte array.
     *
     * @param serializedBytes The byte array containing the serialized object
     * @return The deserialized CachedDateTimeZone instance
     * @throws Exception If deserialization fails
     */
    private CachedDateTimeZone deserializeFromBytes(byte[] serializedBytes) throws Exception {
        ByteArrayInputStream byteInputStream = new ByteArrayInputStream(serializedBytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteInputStream);

        try {
            return (CachedDateTimeZone) objectInputStream.readObject();
        } finally {
            objectInputStream.close();
        }
    }
}