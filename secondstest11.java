package org.joda.time;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class SecondsTestTest11 extends TestCase {

    // (before the late 90's they were all over the place)
    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestSeconds.class);
    }

    @Override
    protected void setUp() throws Exception {
    }

    @Override
    protected void tearDown() throws Exception {
    }

    //-----------------------------------------------------------------------
    public void testIsGreaterThan() {
        assertEquals(true, Seconds.THREE.isGreaterThan(Seconds.TWO));
        assertEquals(false, Seconds.THREE.isGreaterThan(Seconds.THREE));
        assertEquals(false, Seconds.TWO.isGreaterThan(Seconds.THREE));
        assertEquals(true, Seconds.ONE.isGreaterThan(null));
        assertEquals(false, Seconds.seconds(-1).isGreaterThan(null));
    }
}
