package org.joda.time;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class MinutesTestTest23 extends TestCase {

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

    public void testMinus_Minutes() {
        Minutes test2 = Minutes.minutes(2);
        Minutes test3 = Minutes.minutes(3);
        Minutes result = test2.minus(test3);
        assertEquals(2, test2.getMinutes());
        assertEquals(3, test3.getMinutes());
        assertEquals(-1, result.getMinutes());
        assertEquals(1, Minutes.ONE.minus(Minutes.ZERO).getMinutes());
        assertEquals(1, Minutes.ONE.minus((Minutes) null).getMinutes());
        try {
            Minutes.MIN_VALUE.minus(Minutes.ONE);
            fail();
        } catch (ArithmeticException ex) {
            // expected
        }
    }
}