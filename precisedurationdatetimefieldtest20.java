package org.joda.time.field;

import java.util.Arrays;
import java.util.Locale;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.TimeOfDay;
import org.joda.time.chrono.ISOChronology;

public class PreciseDurationDateTimeFieldTestTest20 extends TestCase {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestPreciseDurationDateTimeField.class);
    }

    @Override
    protected void setUp() throws Exception {
    }

    @Override
    protected void tearDown() throws Exception {
    }

    //-----------------------------------------------------------------------
    static class MockPreciseDurationDateTimeField extends PreciseDurationDateTimeField {

        protected MockPreciseDurationDateTimeField() {
            super(DateTimeFieldType.secondOfMinute(), new MockCountingDurationField(DurationFieldType.seconds()));
        }

        protected MockPreciseDurationDateTimeField(DateTimeFieldType type, DurationField dur) {
            super(type, dur);
        }

        @Override
        public int get(long instant) {
            return (int) (instant / 60L);
        }

        @Override
        public DurationField getRangeDurationField() {
            return new MockCountingDurationField(DurationFieldType.minutes());
        }

        @Override
        public int getMaximumValue() {
            return 59;
        }
    }

    static class MockStandardBaseDateTimeField extends MockPreciseDurationDateTimeField {

        protected MockStandardBaseDateTimeField() {
            super();
        }

        @Override
        public DurationField getDurationField() {
            return ISOChronology.getInstanceUTC().seconds();
        }

        @Override
        public DurationField getRangeDurationField() {
            return ISOChronology.getInstanceUTC().minutes();
        }
    }

    //-----------------------------------------------------------------------
    static class MockCountingDurationField extends BaseDurationField {

        static int add_int = 0;

        static int add_long = 0;

        static int difference_long = 0;

        protected MockCountingDurationField(DurationFieldType type) {
            super(type);
        }

        @Override
        public boolean isPrecise() {
            return true;
        }

        @Override
        public long getUnitMillis() {
            return 60;
        }

        @Override
        public long getValueAsLong(long duration, long instant) {
            return 0;
        }

        @Override
        public long getMillis(int value, long instant) {
            return 0;
        }

        @Override
        public long getMillis(long value, long instant) {
            return 0;
        }

        @Override
        public long add(long instant, int value) {
            add_int++;
            return instant + (value * 60L);
        }

        @Override
        public long add(long instant, long value) {
            add_long++;
            return instant + (value * 60L);
        }

        @Override
        public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) {
            difference_long++;
            return 30;
        }
    }

    //-----------------------------------------------------------------------
    static class MockZeroDurationField extends BaseDurationField {

        protected MockZeroDurationField(DurationFieldType type) {
            super(type);
        }

        @Override
        public boolean isPrecise() {
            return true;
        }

        @Override
        public long getUnitMillis() {
            // this is zero
            return 0;
        }

        @Override
        public long getValueAsLong(long duration, long instant) {
            return 0;
        }

        @Override
        public long getMillis(int value, long instant) {
            return 0;
        }

        @Override
        public long getMillis(long value, long instant) {
            return 0;
        }

        @Override
        public long add(long instant, int value) {
            return 0;
        }

        @Override
        public long add(long instant, long value) {
            return 0;
        }

        @Override
        public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) {
            return 0;
        }
    }

    //-----------------------------------------------------------------------
    static class MockImpreciseDurationField extends BaseDurationField {

        protected MockImpreciseDurationField(DurationFieldType type) {
            super(type);
        }

        @Override
        public boolean isPrecise() {
            // this is false
            return false;
        }

        @Override
        public long getUnitMillis() {
            return 0;
        }

        @Override
        public long getValueAsLong(long duration, long instant) {
            return 0;
        }

        @Override
        public long getMillis(int value, long instant) {
            return 0;
        }

        @Override
        public long getMillis(long value, long instant) {
            return 0;
        }

        @Override
        public long add(long instant, int value) {
            return 0;
        }

        @Override
        public long add(long instant, long value) {
            return 0;
        }

        @Override
        public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) {
            return 0;
        }
    }

    public void test_add_RP_int_intarray_int() {
        int[] values = new int[] { 10, 20, 30, 40 };
        int[] expected = new int[] { 10, 20, 30, 40 };
        BaseDateTimeField field = new MockStandardBaseDateTimeField();
        int[] result = field.add(new TimeOfDay(), 2, values, 0);
        assertEquals(true, Arrays.equals(expected, result));
        values = new int[] { 10, 20, 30, 40 };
        expected = new int[] { 10, 20, 31, 40 };
        result = field.add(new TimeOfDay(), 2, values, 1);
        assertEquals(true, Arrays.equals(expected, result));
        values = new int[] { 10, 20, 30, 40 };
        expected = new int[] { 10, 21, 0, 40 };
        result = field.add(new TimeOfDay(), 2, values, 30);
        assertEquals(true, Arrays.equals(expected, result));
        values = new int[] { 23, 59, 30, 40 };
        try {
            field.add(new TimeOfDay(), 2, values, 30);
            fail();
        } catch (IllegalArgumentException ex) {
        }
        values = new int[] { 10, 20, 30, 40 };
        expected = new int[] { 10, 20, 29, 40 };
        result = field.add(new TimeOfDay(), 2, values, -1);
        assertEquals(true, Arrays.equals(expected, result));
        values = new int[] { 10, 20, 30, 40 };
        expected = new int[] { 10, 19, 59, 40 };
        result = field.add(new TimeOfDay(), 2, values, -31);
        assertEquals(true, Arrays.equals(expected, result));
        values = new int[] { 0, 0, 30, 40 };
        try {
            field.add(new TimeOfDay(), 2, values, -31);
            fail();
        } catch (IllegalArgumentException ex) {
        }
    }
}
