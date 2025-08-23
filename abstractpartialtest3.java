package org.joda.time;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.joda.time.base.AbstractPartial;
import org.joda.time.chrono.BuddhistChronology;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.AbstractPartialFieldProperty;

public class AbstractPartialTestTest3 extends TestCase {

    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");

    private long TEST_TIME_NOW = (31L + 28L + 31L + 30L + 31L + 9L - 1L) * DateTimeConstants.MILLIS_PER_DAY;

    private long TEST_TIME1 = (31L + 28L + 31L + 6L - 1L) * DateTimeConstants.MILLIS_PER_DAY + 12L * DateTimeConstants.MILLIS_PER_HOUR + 24L * DateTimeConstants.MILLIS_PER_MINUTE;

    private long TEST_TIME2 = (365L + 31L + 28L + 31L + 30L + 7L - 1L) * DateTimeConstants.MILLIS_PER_DAY + 14L * DateTimeConstants.MILLIS_PER_HOUR + 28L * DateTimeConstants.MILLIS_PER_MINUTE;

    private DateTimeZone zone = null;

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestAbstractPartial.class);
    }

    @Override
    protected void setUp() throws Exception {
        DateTimeUtils.setCurrentMillisFixed(TEST_TIME_NOW);
        zone = DateTimeZone.getDefault();
        DateTimeZone.setDefault(DateTimeZone.UTC);
    }

    @Override
    protected void tearDown() throws Exception {
        DateTimeUtils.setCurrentMillisSystem();
        DateTimeZone.setDefault(zone);
        zone = null;
    }

    //-----------------------------------------------------------------------
    static class MockPartial extends AbstractPartial {

        int[] val = new int[] { 1970, 1 };

        MockPartial() {
            super();
        }

        @Override
        protected DateTimeField getField(int index, Chronology chrono) {
            switch(index) {
                case 0:
                    return chrono.year();
                case 1:
                    return chrono.monthOfYear();
                default:
                    throw new IndexOutOfBoundsException();
            }
        }

        public int size() {
            return 2;
        }

        public int getValue(int index) {
            return val[index];
        }

        public void setValue(int index, int value) {
            val[index] = value;
        }

        public Chronology getChronology() {
            return BuddhistChronology.getInstanceUTC();
        }
    }

    static class MockProperty0 extends AbstractPartialFieldProperty {

        MockPartial partial = new MockPartial();

        @Override
        public DateTimeField getField() {
            return partial.getField(0);
        }

        @Override
        public ReadablePartial getReadablePartial() {
            return partial;
        }

        @Override
        public int get() {
            return partial.getValue(0);
        }
    }

    static class MockProperty1 extends AbstractPartialFieldProperty {

        MockPartial partial = new MockPartial();

        @Override
        public DateTimeField getField() {
            return partial.getField(1);
        }

        @Override
        public ReadablePartial getReadablePartial() {
            return partial;
        }

        @Override
        public int get() {
            return partial.getValue(1);
        }
    }

    static class MockProperty0Field extends MockProperty0 {

        @Override
        public DateTimeField getField() {
            return BuddhistChronology.getInstanceUTC().hourOfDay();
        }
    }

    static class MockProperty0Val extends MockProperty0 {

        @Override
        public int get() {
            return 99;
        }
    }

    static class MockProperty0Chrono extends MockProperty0 {

        @Override
        public ReadablePartial getReadablePartial() {
            return new MockPartial() {

                @Override
                public Chronology getChronology() {
                    return ISOChronology.getInstanceUTC();
                }
            };
        }
    }

    public void testGetField() throws Throwable {
        MockPartial mock = new MockPartial();
        assertEquals(BuddhistChronology.getInstanceUTC().year(), mock.getField(0));
        assertEquals(BuddhistChronology.getInstanceUTC().monthOfYear(), mock.getField(1));
        try {
            mock.getField(-1);
            fail();
        } catch (IndexOutOfBoundsException ex) {
        }
        try {
            mock.getField(2);
            fail();
        } catch (IndexOutOfBoundsException ex) {
        }
    }
}
