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
 * This class tests the caching and serialization functionality of CachedDateTimeZone.
 * 
 * @author Stephen Colebourne
 */
public class TestCachedDateTimeZone extends TestCase {

    private DateTimeZone originalDateTimeZone = null;

    public TestCachedDateTimeZone(String name) {
        super(name);
    }

    /**
     * Main method to run the test suite.
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    /**
     * Creates a test suite containing all test cases.
     */
    public static TestSuite suite() {
        return new TestSuite(TestCachedDateTimeZone.class);
    }

    /**
     * Sets up the test environment by storing the original default time zone
     * and setting the default time zone to UTC.
     */
    @Override
    protected void setUp() throws Exception {
        originalDateTimeZone = DateTimeZone.getDefault();
        DateTimeZone.setDefault(DateTimeZone.UTC);
    }

    /**
     * Restores the original default time zone after each test.
     */
    @Override
    protected void tearDown() throws Exception {
        DateTimeZone.setDefault(originalDateTimeZone);
    }

    /**
     * Tests that the CachedDateTimeZone correctly caches instances.
     * Two requests for the same time zone should return the same cached instance.
     */
    public void testCaching() throws Exception {
        CachedDateTimeZone parisZone1 = CachedDateTimeZone.forZone(DateTimeZone.forID("Europe/Paris"));
        CachedDateTimeZone parisZone2 = CachedDateTimeZone.forZone(DateTimeZone.forID("Europe/Paris"));
        assertSame("Cached instances should be the same", parisZone1, parisZone2);
    }

    /**
     * Tests the serialization and deserialization of CachedDateTimeZone.
     * The deserialized object should be equal to the original object.
     */
    public void testSerialization() throws Exception {
        CachedDateTimeZone originalZone = CachedDateTimeZone.forZone(DateTimeZone.forID("Europe/Paris"));
        
        // Serialize the CachedDateTimeZone object
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(originalZone);
        objectOutputStream.close();
        byte[] serializedBytes = byteArrayOutputStream.toByteArray();
        
        // Deserialize the CachedDateTimeZone object
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(serializedBytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        CachedDateTimeZone deserializedZone = (CachedDateTimeZone) objectInputStream.readObject();
        objectInputStream.close();
        
        assertEquals("Deserialized object should be equal to the original", originalZone, deserializedZone);
    }
}