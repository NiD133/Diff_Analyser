package org.joda.time.tz;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.joda.time.DateTimeZone;

/**
 * Unit tests for the CachedDateTimeZone class.
 * These tests verify the caching behavior and serialization of CachedDateTimeZone.
 * 
 * @author Stephen Colebourne
 */
public class TestCachedDateTimeZone extends TestCase {

    private DateTimeZone originalDateTimeZone;

    public TestCachedDateTimeZone(String name) {
        super(name);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestCachedDateTimeZone.class);
    }

    @Override
    protected void setUp() throws Exception {
        // Save the original default DateTimeZone and set the default to UTC for testing
        originalDateTimeZone = DateTimeZone.getDefault();
        DateTimeZone.setDefault(DateTimeZone.UTC);
    }

    @Override
    protected void tearDown() throws Exception {
        // Restore the original default DateTimeZone after tests
        DateTimeZone.setDefault(originalDateTimeZone);
    }

    /**
     * Test that the CachedDateTimeZone correctly caches instances.
     * Two requests for the same time zone should return the same instance.
     */
    public void testCachingBehavior() throws Exception {
        CachedDateTimeZone parisZone1 = CachedDateTimeZone.forZone(DateTimeZone.forID("Europe/Paris"));
        CachedDateTimeZone parisZone2 = CachedDateTimeZone.forZone(DateTimeZone.forID("Europe/Paris"));
        assertSame("CachedDateTimeZone should return the same instance for the same ID", parisZone1, parisZone2);
    }

    /**
     * Test the serialization and deserialization of CachedDateTimeZone.
     * The deserialized object should be equal to the original object.
     */
    public void testSerialization() throws Exception {
        CachedDateTimeZone originalZone = CachedDateTimeZone.forZone(DateTimeZone.forID("Europe/Paris"));

        // Serialize the CachedDateTimeZone
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(originalZone);
        objectOutputStream.close();

        // Deserialize the CachedDateTimeZone
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        CachedDateTimeZone deserializedZone = (CachedDateTimeZone) objectInputStream.readObject();
        objectInputStream.close();

        // Verify that the deserialized object is equal to the original
        assertEquals("Deserialized CachedDateTimeZone should be equal to the original", originalZone, deserializedZone);
    }
}