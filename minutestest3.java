package org.joda.time;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class MinutesTestTest3 extends TestCase {

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
    public void testFactory_minutesBetween_RInstant() {
        DateTime start = new DateTime(2006, 6, 9, 12, 3, 0, 0, PARIS);
        DateTime end1 = new DateTime(2006, 6, 9, 12, 6, 0, 0, PARIS);
        DateTime end2 = new DateTime(2006, 6, 9, 12, 9, 0, 0, PARIS);
        assertEquals(3, Minutes.minutesBetween(start, end1).getMinutes());
        assertEquals(0, Minutes.minutesBetween(start, start).getMinutes());
        assertEquals(0, Minutes.minutesBetween(end1, end1).getMinutes());
        assertEquals(-3, Minutes.minutesBetween(end1, start).getMinutes());
        assertEquals(6, Minutes.minutesBetween(start, end2).getMinutes());
    }
}
