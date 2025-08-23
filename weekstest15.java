package org.joda.time;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class WeeksTestTest15 extends TestCase {

    // (before the late 90's they were all over the place)
    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestWeeks.class);
    }

    @Override
    protected void setUp() throws Exception {
    }

    @Override
    protected void tearDown() throws Exception {
    }

    //-----------------------------------------------------------------------
    public void testToStandardDays() {
        Weeks test = Weeks.weeks(2);
        Days expected = Days.days(14);
        assertEquals(expected, test.toStandardDays());
        try {
            Weeks.MAX_VALUE.toStandardDays();
            fail();
        } catch (ArithmeticException ex) {
            // expected
        }
    }
}
