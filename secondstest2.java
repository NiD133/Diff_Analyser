package org.joda.time;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class SecondsTestTest2 extends TestCase {

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
    public void testFactory_seconds_int() {
        assertSame(Seconds.ZERO, Seconds.seconds(0));
        assertSame(Seconds.ONE, Seconds.seconds(1));
        assertSame(Seconds.TWO, Seconds.seconds(2));
        assertSame(Seconds.THREE, Seconds.seconds(3));
        assertSame(Seconds.MAX_VALUE, Seconds.seconds(Integer.MAX_VALUE));
        assertSame(Seconds.MIN_VALUE, Seconds.seconds(Integer.MIN_VALUE));
        assertEquals(-1, Seconds.seconds(-1).getSeconds());
        assertEquals(4, Seconds.seconds(4).getSeconds());
    }
}
