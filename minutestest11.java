package org.joda.time;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class MinutesTestTest11 extends TestCase {

    // (before the late 90's they were all over the place)
    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestMinutes.class);
    }

    @Override
    protected void setUp() throws Exception {
    }

    @Override
    protected void tearDown() throws Exception {
    }

    //-----------------------------------------------------------------------
    public void testIsGreaterThan() {
        assertEquals(true, Minutes.THREE.isGreaterThan(Minutes.TWO));
        assertEquals(false, Minutes.THREE.isGreaterThan(Minutes.THREE));
        assertEquals(false, Minutes.TWO.isGreaterThan(Minutes.THREE));
        assertEquals(true, Minutes.ONE.isGreaterThan(null));
        assertEquals(false, Minutes.minutes(-1).isGreaterThan(null));
    }
}
