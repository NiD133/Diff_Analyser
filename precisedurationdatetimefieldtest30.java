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

public class PreciseDurationDateTimeFieldTestTest30 extends TestCase {

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
        // ...
    }

    //-----------------------------------------------------------------------
    static class MockCountingDurationField extends BaseDurationField {
        // ...
    }

    //-----------------------------------------------------------------------
    static class MockZeroDurationField extends BaseDurationField {
        // ...
    }

    //-----------------------------------------------------------------------
    static class MockImpreciseDurationField extends BaseDurationField {
        // ...
    }

    public void test_convertText() {
        BaseDateTimeField field = new MockPreciseDurationDateTimeField();
        assertEquals(0, field.convertText("0", null));
        assertEquals(29, field.convertText("29", null));
        try {
            field.convertText("2A", null);
            fail();
        } catch (IllegalArgumentException ex) {
        }
        try {
            field.convertText(null, null);
            fail();
        } catch (IllegalArgumentException ex) {
        }
    }
}