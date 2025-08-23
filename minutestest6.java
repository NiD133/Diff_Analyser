package org.joda.time;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class MinutesTestTest6 extends TestCase {

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

    public void testFactory_standardMinutesIn_RPeriod() {
        assertEquals(0, Minutes.standardMinutesIn((ReadablePeriod) null).getMinutes());
        assertEquals(0, Minutes.standardMinutesIn(Period.ZERO).getMinutes());
        assertEquals(1, Minutes.standardMinutesIn(new Period(0, 0, 0, 0, 0, 1, 0, 0)).getMinutes());
        assertEquals(123, Minutes.standardMinutesIn(Period.minutes(123)).getMinutes());
        assertEquals(-987, Minutes.standardMinutesIn(Period.minutes(-987)).getMinutes());
        assertEquals(1, Minutes.standardMinutesIn(Period.seconds(119)).getMinutes());
        assertEquals(2, Minutes.standardMinutesIn(Period.seconds(120)).getMinutes());
        assertEquals(2, Minutes.standardMinutesIn(Period.seconds(121)).getMinutes());
        assertEquals(120, Minutes.standardMinutesIn(Period.hours(2)).getMinutes());
        try {
            Minutes.standardMinutesIn(Period.months(1));
            fail();
        } catch (IllegalArgumentException ex) {
            // expected
        }
    }
}
