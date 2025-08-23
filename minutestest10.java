package org.joda.time;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class MinutesTestTest10 extends TestCase {

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

    public void testGetPeriodType() {
        Minutes test = Minutes.minutes(20);
        assertEquals(PeriodType.minutes(), test.getPeriodType());
    }
}
