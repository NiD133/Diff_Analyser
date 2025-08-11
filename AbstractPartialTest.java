package org.joda.time;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.joda.time.base.AbstractPartial;
import org.joda.time.chrono.BuddhistChronology;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.AbstractPartialFieldProperty;

/**
 * Tests basic behaviors of AbstractPartial via a small concrete mock.
 * Focus areas:
 * - Field/value lookups by index (happy path + bounds)
 * - Field type arrays
 * - Property equality contract across matching/mismatching cases
 */
public class TestAbstractPartial extends TestCase {

    // Indices and expected values for the mock
    private static final int YEAR_INDEX = 0;
    private static final int MONTH_INDEX = 1;

    private static final int EXPECTED_YEAR_1970 = 1970;
    private static final int EXPECTED_MONTH_1 = 1; // January in BuddhistChronology UTC

    // Out-of-bounds indices used in negative tests
    private static final int INDEX_BEFORE_FIRST = -1;
    private static final int INDEX_AFTER_LAST = 2;

    // Fixed current time for deterministic tests (1970-06-10T00:00Z when interpreted in UTC)
    private long TEST_TIME_NOW =
            (31L + 28L + 31L + 30L + 31L + 9L - 1L) * DateTimeConstants.MILLIS_PER_DAY;

    private DateTimeZone originalZone = null;

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestAbstractPartial.class);
    }

    public TestAbstractPartial(String name) {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
        DateTimeUtils.setCurrentMillisFixed(TEST_TIME_NOW);
        originalZone = DateTimeZone.getDefault();
        DateTimeZone.setDefault(DateTimeZone.UTC);
    }

    @Override
    protected void tearDown() throws Exception {
        DateTimeUtils.setCurrentMillisSystem();
        DateTimeZone.setDefault(originalZone);
        originalZone = null;
    }

    // -----------------------------------------------------------------------
    // Value access
    // -----------------------------------------------------------------------

    public void testGetValue_byIndex_returnsExpected() {
        MockPartial partial = newMockPartial();

        assertEquals("Year value mismatch", EXPECTED_YEAR_1970, partial.getValue(YEAR_INDEX));
        assertEquals("Month value mismatch", EXPECTED_MONTH_1, partial.getValue(MONTH_INDEX));
    }

    public void testGetValue_byIndex_throwsOnOutOfBounds() {
        MockPartial partial = newMockPartial();

        expectIndexOutOfBounds("Negative index must throw", new Runnable() {
            public void run() {
                partial.getValue(INDEX_BEFORE_FIRST);
            }
        });

        expectIndexOutOfBounds("Index equal to size must throw", new Runnable() {
            public void run() {
                partial.getValue(INDEX_AFTER_LAST);
            }
        });
    }

    public void testGetValues_returnsCopyWithExpectedOrderAndContent() {
        MockPartial partial = newMockPartial();

        int[] values = partial.getValues();
        assertEquals("Unexpected values array length", 2, values.length);
        assertEquals("Year position mismatch", EXPECTED_YEAR_1970, values[YEAR_INDEX]);
        assertEquals("Month position mismatch", EXPECTED_MONTH_1, values[MONTH_INDEX]);
    }

    // -----------------------------------------------------------------------
    // Field access
    // -----------------------------------------------------------------------

    public void testGetField_byIndex_returnsExpectedFields() {
        MockPartial partial = newMockPartial();
        BuddhistChronology buddhistUTC = BuddhistChronology.getInstanceUTC();

        assertEquals("Year field mismatch", buddhistUTC.year(), partial.getField(YEAR_INDEX));
        assertEquals("Month field mismatch", buddhistUTC.monthOfYear(), partial.getField(MONTH_INDEX));
    }

    public void testGetField_byIndex_throwsOnOutOfBounds() {
        MockPartial partial = newMockPartial();

        expectIndexOutOfBounds("Negative index must throw", new Runnable() {
            public void run() {
                partial.getField(INDEX_BEFORE_FIRST);
            }
        });

        expectIndexOutOfBounds("Index equal to size must throw", new Runnable() {
            public void run() {
                partial.getField(INDEX_AFTER_LAST);
            }
        });
    }

    // -----------------------------------------------------------------------
    // Field type access
    // -----------------------------------------------------------------------

    public void testGetFieldType_byIndex_returnsExpectedTypes() {
        MockPartial partial = newMockPartial();

        assertEquals("Year type mismatch", DateTimeFieldType.year(), partial.getFieldType(YEAR_INDEX));
        assertEquals("Month type mismatch", DateTimeFieldType.monthOfYear(), partial.getFieldType(MONTH_INDEX));
    }

    public void testGetFieldType_byIndex_throwsOnOutOfBounds() {
        MockPartial partial = newMockPartial();

        expectIndexOutOfBounds("Negative index must throw", new Runnable() {
            public void run() {
                partial.getFieldType(INDEX_BEFORE_FIRST);
            }
        });

        expectIndexOutOfBounds("Index equal to size must throw", new Runnable() {
            public void run() {
                partial.getFieldType(INDEX_AFTER_LAST);
            }
        });
    }

    public void testGetFieldTypes_returnsExpectedOrderAndContent() {
        MockPartial partial = newMockPartial();

        DateTimeFieldType[] types = partial.getFieldTypes();
        assertEquals("Unexpected field type array length", 2, types.length);
        assertEquals("Year type at position 0", DateTimeFieldType.year(), types[YEAR_INDEX]);
        assertEquals("Month type at position 1", DateTimeFieldType.monthOfYear(), types[MONTH_INDEX]);
    }

    // -----------------------------------------------------------------------
    // Property equality contract
    // -----------------------------------------------------------------------

    public void testProperty_equals_contract() {
        MockProperty0 base = new MockProperty0();

        // Reflexive
        assertEquals("Property must equal itself", base, base);

        // Same contents
        assertEquals("Properties with same partial, field, and value should be equal",
                base, new MockProperty0());

        // Different index (field) -> not equal
        assertFalse("Different property index must not be equal", base.equals(new MockProperty1()));

        // Same field but different value -> not equal
        assertFalse("Same field but different value must not be equal", base.equals(new MockProperty0Val()));

        // Different underlying field -> not equal
        assertFalse("Different field instance must not be equal", base.equals(new MockProperty0Field()));

        // Same field/value but different chronology -> not equal
        assertFalse("Different chronology must not be equal", base.equals(new MockProperty0Chrono()));

        // Type safety and null handling
        assertFalse("Must not equal different type", base.equals(""));
        assertFalse("Must not equal null", base.equals(null));
    }

    // -----------------------------------------------------------------------
    // Helpers
    // -----------------------------------------------------------------------

    private static MockPartial newMockPartial() {
        return new MockPartial();
    }

    private static void expectIndexOutOfBounds(String message, Runnable r) {
        try {
            r.run();
            fail(message + " (no exception thrown)");
        } catch (IndexOutOfBoundsException expected) {
            // expected
        }
    }

    // -----------------------------------------------------------------------
    // Test doubles
    // -----------------------------------------------------------------------

    /**
     * Minimal concrete implementation to exercise AbstractPartial behavior.
     * Fields (in order): year, monthOfYear (Buddhist UTC).
     * Values: 1970, 1.
     */
    static class MockPartial extends AbstractPartial {

        private final int[] values = new int[] { EXPECTED_YEAR_1970, EXPECTED_MONTH_1 };

        MockPartial() {
            super();
        }

        @Override
        protected DateTimeField getField(int index, Chronology chrono) {
            switch (index) {
                case YEAR_INDEX:  return chrono.year();
                case MONTH_INDEX: return chrono.monthOfYear();
                default:          throw new IndexOutOfBoundsException("Index: " + index);
            }
        }

        @Override
        public int size() {
            return 2;
        }

        @Override
        public int getValue(int index) {
            return values[index];
        }

        public void setValue(int index, int value) {
            values[index] = value;
        }

        @Override
        public Chronology getChronology() {
            return BuddhistChronology.getInstanceUTC();
        }
    }

    /**
     * Property pointing at YEAR field/value of MockPartial.
     */
    static class MockProperty0 extends AbstractPartialFieldProperty {
        final MockPartial partial = new MockPartial();

        @Override
        public DateTimeField getField() {
            return partial.getField(YEAR_INDEX);
        }

        @Override
        public ReadablePartial getReadablePartial() {
            return partial;
        }

        @Override
        public int get() {
            return partial.getValue(YEAR_INDEX);
        }
    }

    /**
     * Property pointing at MONTH field/value of MockPartial.
     */
    static class MockProperty1 extends AbstractPartialFieldProperty {
        final MockPartial partial = new MockPartial();

        @Override
        public DateTimeField getField() {
            return partial.getField(MONTH_INDEX);
        }

        @Override
        public ReadablePartial getReadablePartial() {
            return partial;
        }

        @Override
        public int get() {
            return partial.getValue(MONTH_INDEX);
        }
    }

    /**
     * Same as MockProperty0 but returns a different field (hourOfDay).
     */
    static class MockProperty0Field extends MockProperty0 {
        @Override
        public DateTimeField getField() {
            return BuddhistChronology.getInstanceUTC().hourOfDay();
        }
    }

    /**
     * Same as MockProperty0 but returns a different value.
     */
    static class MockProperty0Val extends MockProperty0 {
        @Override
        public int get() {
            return 99;
        }
    }

    /**
     * Same as MockProperty0 but the readable partial exposes a different chronology.
     */
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
}