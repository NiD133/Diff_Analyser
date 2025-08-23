package org.joda.time;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class MinutesTestTest1 extends TestCase {

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
    public void testConstants() {
        assertEquals(0, Minutes.ZERO.getMinutes());
        assertEquals(1, Minutes.ONE.getMinutes());
        assertEquals(2, Minutes.TWO.getMinutes());
        assertEquals(3, Minutes.THREE.getMinutes());
        assertEquals(Integer.MAX_VALUE, Minutes.MAX_VALUE.getMinutes());
        assertEquals(Integer.MIN_VALUE, Minutes.MIN_VALUE.getMinutes());
    }
}
