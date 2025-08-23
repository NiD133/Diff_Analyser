package org.joda.time;

import org.joda.time.base.AbstractPartial;
import org.joda.time.chrono.BuddhistChronology;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for the AbstractPartial class.
 * This test suite uses a mock implementation of AbstractPartial to test the
 * concrete methods provided by the abstract class.
 */
public class AbstractPartialTest {

    //-----------------------------------------------------------------------
    // Mock implementation of AbstractPartial used for testing.
    //-----------------------------------------------------------------------

    /**
     * A mock implementation of AbstractPartial for testing purposes.
     * It represents a partial date with two fields: year and monthOfYear.
     * <p>
     * - Field 0: year (value: 1970)
     * - Field 1: monthOfYear (value: 1)
     * <p>
     * It uses the Buddhist chronology in the UTC time zone.
     */
    static class MockPartial extends AbstractPartial {
        private static final int YEAR_VALUE = 1970;
        private static final int MONTH_VALUE = 1;

        // Use a mutable array to allow testing setters if needed in other tests.
        private final int[] iValues = new int[]{YEAR_VALUE, MONTH_VALUE};

        @Override
        protected DateTimeField getField(int index, Chronology chrono) {
            switch (index) {
                case 0:
                    return chrono.year();
                case 1:
                    return chrono.monthOfYear();
                default:
                    throw new IndexOutOfBoundsException();
            }
        }

        @Override
        public int size() {
            return 2;
        }

        @Override
        public int getValue(int index) {
            // This method is part of the mock's contract, not the SUT's implementation.
            // We test it to ensure our mock is reliable for other tests.
            if (index < 0 || index >= size()) {
                throw new IndexOutOfBoundsException("Invalid index: " + index);
            }
            return iValues[index];
        }

        @Override
        public Chronology getChronology() {
            // Using a non-ISO chronology to ensure tests are not coupled to the default.
            return BuddhistChronology.getInstanceUTC();
        }
    }

    //-----------------------------------------------------------------------
    // Tests for the MockPartial implementation itself.
    // These tests ensure the mock is behaving as expected.
    //-----------------------------------------------------------------------

    @Test
    public void mockGetValue_withValidIndex_returnsCorrectValue() {
        ReadablePartial partial = new MockPartial();
        assertEquals("Year value should be correct", 1970, partial.getValue(0));
        assertEquals("Month value should be correct", 1, partial.getValue(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void mockGetValue_withNegativeIndex_throwsException() {
        ReadablePartial partial = new MockPartial();
        partial.getValue(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void mockGetValue_withIndexEqualToSize_throwsException() {
        ReadablePartial partial = new MockPartial();
        partial.getValue(2); // size is 2, so valid indices are 0 and 1
    }

    //-----------------------------------------------------------------------
    // Tests for methods implemented in the AbstractPartial class.
    //-----------------------------------------------------------------------

    @Test
    public void isSupported_returnsTrueForSupportedFieldTypesAndFalseForUnsupported() {
        ReadablePartial partial = new MockPartial();

        // Check supported fields
        assertTrue("year should be supported", partial.isSupported(DateTimeFieldType.year()));
        assertTrue("monthOfYear should be supported", partial.isSupported(DateTimeFieldType.monthOfYear()));

        // Check unsupported fields
        assertFalse("dayOfMonth should not be supported", partial.isSupported(DateTimeFieldType.dayOfMonth()));
        assertFalse("hourOfDay should not be supported", partial.isSupported(DateTimeFieldType.hourOfDay()));
    }

    @Test
    public void isSupported_returnsFalseForNullFieldType() {
        ReadablePartial partial = new MockPartial();
        assertFalse("isSupported(null) should return false", partial.isSupported(null));
    }
}