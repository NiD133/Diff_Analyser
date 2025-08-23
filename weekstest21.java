package org.joda.time;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class WeeksTestTest21 extends TestCase {

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

    public void testPlus_Weeks() {
        Weeks test2 = Weeks.weeks(2);
        Weeks test3 = Weeks.weeks(3);
        Weeks result = test2.plus(test3);
        assertEquals(2, test2.getWeeks());
        assertEquals(3, test3.getWeeks());
        assertEquals(5, result.getWeeks());
        assertEquals(1, Weeks.ONE.plus(Weeks.ZERO).getWeeks());
        assertEquals(1, Weeks.ONE.plus((Weeks) null).getWeeks());
        try {
            Weeks.MAX_VALUE.plus(Weeks.ONE);
            fail();
        } catch (ArithmeticException ex) {
            // expected
        }
    }
}
